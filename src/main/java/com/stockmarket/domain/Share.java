package com.stockmarket.domain;

public class Share extends Asset {

    private static final double MIN_COMMISSION = 5.0;
    private static final double COMMISSION_RATE = 0.005;

    public Share(String ticker, double marketPrice) {
        super(ticker, marketPrice);
    }

    @Override
    public AssetType getType() {
        return AssetType.SHARE;
    }

    @Override
    public double calculatePurchaseCost(int quantity) {
        double tradeValue = getMarketValue(quantity);
        double fee = Math.max(tradeValue * COMMISSION_RATE, MIN_COMMISSION);
        return tradeValue + fee;
    }

    @Override
    public double calculateSellProceeds(int quantity) {
        double tradeValue = getMarketValue(quantity);
        double fee = Math.max(tradeValue * COMMISSION_RATE, MIN_COMMISSION);
        return Math.max(0, tradeValue - fee);
    }

    @Override
    public double calculateRealValue(int quantity) {
        // Wycena pozycji = ile realnie przy sprzedaży całości po bieżącej cenie.
        return calculateSellProceeds(quantity);
    }
}
