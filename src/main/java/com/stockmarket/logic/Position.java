package com.stockmarket.logic;

import com.stockmarket.domain.Asset;
import com.stockmarket.logic.exceptions.InsufficientHoldingsException;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Pozycja w portfelu dla konkretnego aktywa. Trzyma partie zakupowe.
 */
public class Position {
    private final Asset asset;
    private final Deque<PurchaseLot> lots;
    private int totalQuantity;

    public Position(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset nie może być null");
        }
        this.asset = asset;
        this.lots = new ArrayDeque<>();
        this.totalQuantity = 0;
    }

    public Asset getAsset() {
        return asset;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void addLot(LocalDate date, int quantity, double effectiveUnitCost) {
        PurchaseLot lot = new PurchaseLot(date, quantity, effectiveUnitCost);
        lots.addLast(lot);
        totalQuantity += quantity;
    }

    public List<PurchaseLot> getLotsSnapshot() {
        List<PurchaseLot> copy = new ArrayList<>();
        for (PurchaseLot lot : lots) {
            copy.add(new PurchaseLot(lot.getPurchaseDate(), lot.getQuantity(), lot.getUnitPrice()));
        }
        return copy;
    }

    public SaleReport sellFifo(LocalDate saleDate, int quantityToSell, double unitSaleProceed) {
        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("Ilość do sprzedaży musi być dodatnia");
        }
        if (quantityToSell > totalQuantity) {
            throw new InsufficientHoldingsException(
                    "Brak wystarczającej ilości aktywa " + asset.getTicker() + ". Żądane: " + quantityToSell + ", dostępne: " + totalQuantity);
        }

        int remaining = quantityToSell;
        SaleReport report = new SaleReport(asset.getTicker(), saleDate, quantityToSell, unitSaleProceed);

        while (remaining > 0) {
            PurchaseLot lot = lots.peekFirst();
            if (lot == null) {
                throw new InsufficientHoldingsException("Brak partii do sprzedaży dla " + asset.getTicker());
            }

            int availableInLot = lot.getQuantity();
            int taken = Math.min(availableInLot, remaining);

            double pnl = taken * (unitSaleProceed - lot.getUnitPrice());
            report.addLotResult(new SaleLotResult(lot.getPurchaseDate(), taken, lot.getUnitPrice(), pnl));

            lot.reduceQuantity(taken);
            totalQuantity -= taken;
            remaining -= taken;

            if (lot.getQuantity() == 0) {
                lots.removeFirst();
            }
        }

        return report;
    }
    public double getMarketValue() {
        return asset.getMarketValue(totalQuantity);
    }

    public double getRealValue() {
        return asset.calculateRealValue(totalQuantity);
    }
}
