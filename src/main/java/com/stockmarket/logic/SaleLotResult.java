package com.stockmarket.logic;

import java.time.LocalDate;

public class SaleLotResult {
    private final LocalDate lotDate;
    private final int quantityFromLot;
    private final double buyUnitPrice;
    private final double profitAndLoss;

    public SaleLotResult(LocalDate lotDate, int quantityFromLot, double buyUnitPrice, double profitAndLoss) {
        this.lotDate = lotDate;
        this.quantityFromLot = quantityFromLot;
        this.buyUnitPrice = buyUnitPrice;
        this.profitAndLoss = profitAndLoss;
    }

    public LocalDate getLotDate() { return lotDate; }
    public int getQuantityFromLot() { return quantityFromLot; }
    public double getBuyUnitPrice() { return buyUnitPrice; }
    public double getProfitAndLoss() { return profitAndLoss; }
}
