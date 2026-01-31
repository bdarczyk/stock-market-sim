package com.stockmarket.domain;

/**
 * Instrument rynkowy ("aktywo") z parametrami domenowymi.
 *
 * Portfolio nie przechowuje tutaj stanu posiadania (ilości). Ilości trzymane są w partiach zakupowych.
 */
public abstract class Asset {
    private final String ticker;
    private double marketPrice;

    protected Asset(String ticker, double marketPrice) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker nie może być pusty");
        }
        if (marketPrice < 0) {
            throw new IllegalArgumentException("Cena rynkowa nie może być ujemna");
        }
        this.ticker = ticker;
        this.marketPrice = marketPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        if (marketPrice < 0) {
            throw new IllegalArgumentException("Cena rynkowa nie może być ujemna");
        }
        this.marketPrice = marketPrice;
    }

    public double getMarketValue(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Ilość nie może być ujemna");
        }
        return marketPrice * quantity;
    }

    public abstract AssetType getType();

    /**
     * Całkowity koszt zakupu (uwzględnia ukryte koszty, np. prowizję/spread).
     */
    public abstract double calculatePurchaseCost(int quantity);

    /**
     * Całkowite wpływy ze sprzedaży (uwzględnia ukryte koszty, np. prowizję/spread).
     */
    public abstract double calculateSellProceeds(int quantity);

    /**
     * Wartość pozycji po uwzględnieniu specyfiki typu aktywa (np. spread, magazynowanie).
     * Jest to wartość "realna" (net liquidation value).
     */
    public abstract double calculateRealValue(int quantity);
}
