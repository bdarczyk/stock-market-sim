package com.stockmarket;

import java.util.Objects;

public class Stock {

    private final String symbol;
    private final String name;
    private final double initialPrice;

    // Konstruktor
    public Stock(String symbol, String name, double initialPrice) {
        if(symbol == null || name == null) {
            System.out.println("Symbol i nazwa nie mogą być null");
        }
        if(initialPrice < 0) {
            System.out.println("Cena początkowa nie może być ujemna");
        }
        this.symbol = symbol;
        this.name = name;
        this.initialPrice = initialPrice;
    }

    // Gettery
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
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return symbol.equals(stock.symbol);
    }

    public int hashCode() {
        return Objects.hash(symbol);
    }

    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", initialPrice=" + initialPrice +
                '}';
    }
}
