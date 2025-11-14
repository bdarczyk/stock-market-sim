package com.stockmarket;

public class main {
    public static void main(String[] args) {
        // Tworzymy kilka akcji
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        Stock pkn = new Stock("PKN", "PKN Orlen", 100.0);
        Stock allegro = new Stock("ALE", "Allegro", 50.0);

        // Tworzymy portfel z 1000 zł
        Portfolio portfolio = new Portfolio(1000.0);

        // Sprawdzamy początkowy stan portfela
        System.out.println("Gotówka w portfelu: " + portfolio.getCash());
        System.out.println("Liczba pozycji w portfelu: " + portfolio.getHoldingsCount());

        // Dodajemy akcje
        portfolio.addStock(cdr, 2);      // 2 akcje CD Projekt
        portfolio.addStock(pkn, 5);      // 5 akcji PKN Orlen
        portfolio.addStock(cdr, 3);      // dodajemy kolejne 3 akcje CD Projekt

        // Dodajemy nowy typ akcji
        portfolio.addStock(allegro, 4);  // 4 akcje Allegro

        // Wyświetlamy szczegóły
        System.out.println("Ilość akcji CDR: " + portfolio.getStockQuantity(cdr)); // 5
        System.out.println("Ilość akcji PKN: " + portfolio.getStockQuantity(pkn)); // 5
        System.out.println("Ilość akcji ALE: " + portfolio.getStockQuantity(allegro)); // 4

        // Obliczenia wartości
        System.out.println("Wartość akcji w portfelu: " + portfolio.calculateStockValue());
        System.out.println("Całkowita wartość portfela: " + portfolio.calculateTotalValue());
        System.out.println("Liczba różnych pozycji w portfelu: " + portfolio.getHoldingsCount());
    }
}
