package com.stockmarket.domain;

public class Commodity extends Asset {
    private final double storageCostPerUnit;

    private static final int STORAGE_THRESHOLD = 100;
    private static final double STORAGE_PENALTY_MULTIPLIER = 1.2;

    public Commodity(String ticker, double marketPrice, double storageCostPerUnit) {
        super(ticker, marketPrice);
        if (storageCostPerUnit < 0) {
            throw new IllegalArgumentException("Koszt magazynowania nie może być ujemny");
        }
        this.storageCostPerUnit = storageCostPerUnit;
    }

    public double getStorageCostPerUnit() {
        return storageCostPerUnit;
    }

    @Override
    public AssetType getType() {
        return AssetType.COMMODITY;
    }

    @Override
    public double calculatePurchaseCost(int quantity) {
        return getMarketValue(quantity);
    }

    @Override
    public double calculateSellProceeds(int quantity) {
        return getMarketValue(quantity);
    }

    @Override
    public double calculateRealValue(int quantity) {
        double value = getMarketValue(quantity) - calculateComplexStorageCost(quantity);
        return Math.max(0, value);
    }

    private double calculateComplexStorageCost(int quantity) {
        double baseCost = storageCostPerUnit * quantity;
        if (quantity > STORAGE_THRESHOLD) {
            return baseCost * STORAGE_PENALTY_MULTIPLIER;
        }
        return baseCost;
    }
}
