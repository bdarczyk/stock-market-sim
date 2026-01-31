package com.stockmarket.logic;

import com.stockmarket.domain.Asset;
import java.time.LocalDate;

public class Order {
    private final long sequence;
    private final OrderSide side;
    private final Asset asset;
    private final int quantity;
    private final double limitPrice;
    private final double marketPriceSnapshot;
    private final LocalDate date;

    public Order(long sequence, OrderSide side, Asset asset, int quantity,
                 double limitPrice, double marketPriceSnapshot, LocalDate date) {

        require(side != null, "side null");
        require(asset != null, "asset null");
        require(date != null, "date null");
        require(quantity > 0, "qty <= 0");
        require(limitPrice >= 0 && marketPriceSnapshot >= 0, "price < 0");

        this.sequence = sequence;
        this.side = side;
        this.asset = asset;
        this.quantity = quantity;
        this.limitPrice = limitPrice;
        this.marketPriceSnapshot = marketPriceSnapshot;
        this.date = date;
    }

    private static void require(boolean ok, String msg) {
        if (!ok) throw new IllegalArgumentException(msg);
    }

    public long getSequence() { return sequence; }
    public OrderSide getSide() { return side; }
    public Asset getAsset() { return asset; }
    public int getQuantity() { return quantity; }
    public double getLimitPrice() { return limitPrice; }
    public double getMarketPriceSnapshot() { return marketPriceSnapshot; }
    public LocalDate getDate() { return date; }
}
