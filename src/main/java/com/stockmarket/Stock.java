package com.stockmarket;

public class Stock {

    private final String symbol;
    private final String name;
    private final double initialPrice;

    // Konstruktor
    public Stock(String symbol, String name, double initialPrice) {
        if(symbol == null || name == null) {
            throw new IllegalArgumentException("Symbol i nazwa nie mogą być null");
        }
        if(initialPrice < 0) {
            throw new IllegalArgumentException("Cena początkowa nie może być ujemna");
        }

        this.symbol = symbol;
        this.name = name;
        this.initialPrice = initialPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    // equals i hashCode — porównanie tylko po symbolu
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return symbol.equals(stock.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}