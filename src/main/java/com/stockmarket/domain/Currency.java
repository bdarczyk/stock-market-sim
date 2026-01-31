package com.stockmarket.domain;

public class Currency extends Asset {
    private final double spread;

    private static final int THRESHOLD = 1000;
    private static final double SPREAD_DISCOUNT = 0.8;

    public Currency(String ticker, double marketPrice, double spread) {
        super(ticker, marketPrice);
        if (spread < 0) {
            throw new IllegalArgumentException("Spread nie może być ujemny");
        }
        this.spread = spread;
    }

    public double getSpread() {
        return spread;
    }

    @Override
    public AssetType getType() {
        return AssetType.CURRENCY;
    }

    @Override
    public double calculatePurchaseCost(int quantity) {
        double effectiveSpread = calculateEffectiveSpread(quantity);
        double askPrice = getMarketPrice() + effectiveSpread;
        return askPrice * quantity;
    }

    @Override
    public double calculateSellProceeds(int quantity) {
        double effectiveSpread = calculateEffectiveSpread(quantity);
        double bidPrice = getMarketPrice() - effectiveSpread;
        return Math.max(0, bidPrice * quantity);
    }

    @Override
    public double calculateRealValue(int quantity) {
        // Wycena pozycji po Bid (netto od spreadu)
        return calculateSellProceeds(quantity);
    }

    private double calculateEffectiveSpread(int quantity) {
        if (quantity >= THRESHOLD) {
            return spread * SPREAD_DISCOUNT;
        }
        return spread;
    }
}
