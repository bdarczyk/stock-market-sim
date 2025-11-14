package com.stockmarket;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {

    // Testuje pusty portfel: gotówka powinna być ustawiona, brak akcji, wartości = 0
    @Test
    void testEmptyPortfolio() {
        Portfolio portfolio = new Portfolio(1000.0);
        assertEquals(1000.0, portfolio.getCash());        // sprawdzamy gotówkę
        assertEquals(0, portfolio.getHoldingsCount());    // brak akcji
        assertEquals(0, portfolio.calculateStockValue()); // wartość akcji = 0
        assertEquals(1000.0, portfolio.calculateTotalValue()); // całkowita wartość = cash
    }

    // Testuje dodanie pierwszej akcji do portfela
    @Test
    void testAddStockFirstTime() {
        Portfolio portfolio = new Portfolio(1000.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        portfolio.addStock(cdr, 2);

        assertEquals(1, portfolio.getHoldingsCount());    // jedna pozycja w tablicy
        assertEquals(2, portfolio.getStockQuantity(cdr)); // ilość dodanej akcji
        assertEquals(600.0, portfolio.calculateStockValue()); // 2*300
        assertEquals(1600.0, portfolio.calculateTotalValue()); // 1000+600
    }

    // Testuje dodanie tej samej akcji wielokrotnie – quantity powinno się sumować
    @Test
    void testAddSameStockMultipleTimes() {
        Portfolio portfolio = new Portfolio(1000.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);

        portfolio.addStock(cdr, 2);
        portfolio.addStock(cdr, 3);

        assertEquals(1, portfolio.getHoldingsCount());    // nadal jedna pozycja
        assertEquals(5, portfolio.getStockQuantity(cdr)); // suma quantity = 5
        assertEquals(1500.0, portfolio.calculateStockValue()); // 5*300
        assertEquals(2500.0, portfolio.calculateTotalValue()); // 1000+1500
    }

    // Testuje dodanie różnych typów akcji – każda powinna być osobną pozycją
    @Test
    void testAddDifferentStocks() {
        Portfolio portfolio = new Portfolio(1000.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        Stock pkn = new Stock("PKN", "PKN Orlen", 100.0);

        portfolio.addStock(cdr, 2);
        portfolio.addStock(pkn, 5);

        assertEquals(2, portfolio.getHoldingsCount());    // dwie pozycje
        assertEquals(2, portfolio.getStockQuantity(cdr)); // ilość CDR
        assertEquals(5, portfolio.getStockQuantity(pkn)); // ilość PKN
        assertEquals(2*300 + 5*100, portfolio.calculateStockValue()); // suma wartości akcji
        assertEquals(1000 + 2*300 + 5*100, portfolio.calculateTotalValue()); // cash + akcje
    }

    // Testuje sytuację brzegową: portfel pełny (10 pozycji), nie dodaje kolejnej akcji
    @Test
    void testAddStockWhenFull() {
        Portfolio portfolio = new Portfolio(1000.0);
        // Dodajemy 10 różnych akcji
        for (int i = 1; i <= 10; i++) {
            portfolio.addStock(new Stock("S" + i, "Stock" + i, 10.0 * i), 1);
        }

        assertEquals(10, portfolio.getHoldingsCount()); // portfel pełny

        // Próba dodania kolejnej akcji nie powinna zmienić liczby pozycji
        portfolio.addStock(new Stock("S11", "Stock11", 110.0), 1);
        assertEquals(10, portfolio.getHoldingsCount());
    }

    // Testuje, czy getStockQuantity zwraca 0 dla akcji, której nie ma w portfelu
    @Test
    void testGetStockQuantityForNonExistingStock() {
        Portfolio portfolio = new Portfolio(500.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);

        assertEquals(0, portfolio.getStockQuantity(cdr)); // brak akcji w portfelu
    }
    // Testuje dodanie akcji o zerowej ilości
    @Test
    void testAddZeroQuantityStock() {
        Portfolio portfolio = new Portfolio(500.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);

        portfolio.addStock(cdr, 0);

        assertEquals(1, portfolio.getHoldingsCount());      // pozycja dodana
        assertEquals(0, portfolio.getStockQuantity(cdr));   // quantity = 0
        assertEquals(0, portfolio.calculateStockValue());   // wartość akcji = 0
    }
}
