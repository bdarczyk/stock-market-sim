package com.stockmarket.logic;

import com.stockmarket.domain.Asset;
import com.stockmarket.logic.exceptions.InsufficientFundsException;
import com.stockmarket.logic.exceptions.InsufficientHoldingsException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Portfolio {
    private double cash;

    // O(1) dostęp po tickerze
    private final Map<String, Position> positions;

    // gwarancja unikalności
    private final Set<String> watchlist;

    // kolejka zleceń (priorytet "atrakcyjności")
    private final PriorityQueue<Order> pendingOrders;

    private long nextOrderSequence;

    public Portfolio(double initialCash) {
        if (initialCash < 0) {
            throw new IllegalArgumentException("Gotówka początkowa nie może być ujemna");
        }
        this.cash = initialCash;
        this.positions = new HashMap<>();
        this.watchlist = new HashSet<>();
        this.pendingOrders = new PriorityQueue<>(new OrderAttractivenessComparator());
        this.nextOrderSequence = 1L;
    }

    public double getCash() {
        return cash;
    }

    public double calculateTotalNetWorth() {
        double sum = cash;
        for (Position p : positions.values()) {
            sum += p.getRealValue();
        }
        return sum;
    }

    public double calculateTotalMarketWorth() {
        double sum = cash;
        for (Position p : positions.values()) {
            sum += p.getMarketValue();
        }
        return sum;
    }

    public Set<String> getWatchlistSnapshot() {
        return new HashSet<>(watchlist);
    }

    public boolean addToWatchlist(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker nie może być pusty");
        }
        return watchlist.add(ticker);
    }

    public boolean removeFromWatchlist(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker nie może być pusty");
        }
        return watchlist.remove(ticker);
    }

    public List<Position> getPositionsSnapshot() {
        List<Position> list = new ArrayList<>();
        for (Position p : positions.values()) {
            list.add(p);
        }
        return list;
    }

    public void addPositionFromPersistence(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position nie może być null");
        }
        String ticker = position.getAsset().getTicker();
        if (positions.containsKey(ticker)) {
            throw new IllegalArgumentException("Duplikat pozycji w persystencji: " + ticker);
        }
        positions.put(ticker, position);
    }

    public Position getPositionOrNull(String ticker) {
        if (ticker == null) {
            return null;
        }
        return positions.get(ticker);
    }

    public int getTotalQuantity(String ticker) {
        Position p = positions.get(ticker);
        return p == null ? 0 : p.getTotalQuantity();
    }

    public long nextOrderSequence() {
        return nextOrderSequence++;
    }

    public void placeOrder(OrderSide side, Asset asset, int quantity, double limitPrice, LocalDate date) {
        if (side == null) {
            throw new IllegalArgumentException("Side nie może być null");
        }
        if (asset == null) {
            throw new IllegalArgumentException("Asset nie może być null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Data nie może być null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być dodatnia");
        }
        if (limitPrice < 0) {
            throw new IllegalArgumentException("Limit price nie może być ujemny");
        }

        Order order = new Order(nextOrderSequence(), side, asset, quantity, limitPrice, asset.getMarketPrice(), date);
        pendingOrders.add(order);
    }

    public Order peekNextOrder() {
        return pendingOrders.peek();
    }

    public SaleReport processNextExecutableOrder() {
        if (pendingOrders.isEmpty()) {
            return null;
        }

        List<Order> skipped = new ArrayList<>();
        SaleReport result = null;

        while (!pendingOrders.isEmpty()) {
            Order o = pendingOrders.poll(); // zdejmij z HEAD
            if (isExecutable(o)) {
                result = executeOrder(o);
                break;
            }
            skipped.add(o);
        }

        for (Order o : skipped) {
            pendingOrders.add(o);
        }

        return result;
    }


    private boolean isExecutable(Order order) {
        double market = order.getMarketPriceSnapshot();
        if (order.getSide() == OrderSide.BUY) {
            return order.getLimitPrice() >= market;
        }
        return order.getLimitPrice() <= market;
    }

    private SaleReport executeOrder(Order order) {
        order.getAsset().setMarketPrice(order.getMarketPriceSnapshot());

        if (order.getSide() == OrderSide.BUY) {
            executeBuy(order.getAsset(), order.getQuantity(), order.getDate());
            return null;
        }
        return executeSell(order.getAsset(), order.getQuantity(), order.getDate());
    }

    public void executeBuy(Asset asset, int quantity, LocalDate date) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset nie może być null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Data nie może być null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być dodatnia");
        }

        double totalCost = asset.calculatePurchaseCost(quantity);
        if (cash < totalCost) {
            throw new InsufficientFundsException(
                    "Brak środków na zakup " + asset.getTicker() + ". Koszt: " + totalCost + ", gotówka: " + cash);
        }
        cash -= totalCost;

        double effectiveUnitCost = totalCost / quantity;

        Position position = positions.get(asset.getTicker());
        if (position == null) {
            position = new Position(asset);
            positions.put(asset.getTicker(), position);
        }
        position.addLot(date, quantity, effectiveUnitCost);
    }

    public SaleReport executeSell(Asset asset, int quantity, LocalDate date) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset nie może być null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Data nie może być null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być dodatnia");
        }

        Position position = positions.get(asset.getTicker());
        if (position == null || position.getTotalQuantity() <= 0) {
            throw new InsufficientHoldingsException("Brak pozycji do sprzedaży dla " + asset.getTicker());
        }

        double totalProceeds = asset.calculateSellProceeds(quantity);
        double unitSaleProceed = totalProceeds / quantity;

        SaleReport report = position.sellFifo(date, quantity, unitSaleProceed);
        cash += totalProceeds;

        if (position.getTotalQuantity() == 0) {
            positions.remove(asset.getTicker());
        }
        return report;
    }
}
