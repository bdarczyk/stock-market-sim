package com.stockmarket;

public class Portfolio {

    private double cash;
    private StockHolding[] holdings;
    private int holdingsCount;

    // Prywatna statyczna klasa wewnętrzna reprezentująca jedną pozycję w portfelu
    private static class StockHolding {
        private Stock stock;
        private int quantity;

        public StockHolding(Stock stock, int quantity) {
            this.stock = stock;
            this.quantity = quantity;
        }
    }

    // Konstruktor portfela
    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.holdings = new StockHolding[10]; // maksymalnie 10 pozycji
        this.holdingsCount = 0;
    }

    // Dodaje akcje do portfela
    public void addStock(Stock stock, int quantity) {
        if(stock == null) throw new IllegalArgumentException("Stock nie może być null");
        if(quantity <= 0) throw new IllegalArgumentException("Ilość musi być większa od 0");

        for (int i = 0; i < holdingsCount; i++) {
            if (holdings[i].stock.equals(stock)) {
                holdings[i].quantity += quantity;
                return;
            }
        }
        if (holdingsCount < holdings.length) {
            holdings[holdingsCount++] = new StockHolding(stock, quantity);
        } else {
            System.out.println("Brak miejsca w portfelu!");
        }
    }

    // Oblicza wartość wszystkich akcji w portfelu
    public double calculateStockValue() {
        double total = 0.0;
        for (int i = 0; i < holdingsCount; i++) {
            total += holdings[i].quantity * holdings[i].stock.getInitialPrice();
        }
        return total;
    }

    // Oblicza całkowitą wartość portfela (akcje + gotówka)
    public double calculateTotalValue() {
        return cash + calculateStockValue();
    }

    // Getter dla gotówki
    public double getCash() {
        return cash;
    }

    // Liczba różnych akcji w portfelu
    public int getHoldingsCount() {
        return holdingsCount;
    }

    // Ilość danej akcji w portfelu
    public int getStockQuantity(Stock stock) {
        for (int i = 0; i < holdingsCount; i++) {
            if (holdings[i].stock.equals(stock)) {
                return holdings[i].quantity;
            }
        }
        return 0;
    }
}
