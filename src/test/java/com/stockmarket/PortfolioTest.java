package com.stockmarket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {

    @Test
    @DisplayName("Pusty portfel")
    void testEmptyPortfolio() {
        Portfolio portfolio = new Portfolio(1000.0);
        assertEquals(1000.0, portfolio.getCash());
        assertEquals(0, portfolio.getHoldingsCount());
        assertEquals(0, portfolio.calculateStockValue());
        assertEquals(1000.0, portfolio.calculateTotalValue());
    }

    @Test
    @DisplayName("Dodanie pierwszej akcji")
    void testAddStockFirstTime() {
        Portfolio portfolio = new Portfolio(1000.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        portfolio.addStock(cdr, 2);

        assertEquals(1, portfolio.getHoldingsCount());
        assertEquals(2, portfolio.getStockQuantity(cdr));
        assertEquals(600.0, portfolio.calculateStockValue());
        assertEquals(1600.0, portfolio.calculateTotalValue());
    }

    @Test
    @DisplayName("Dodawanie tej samej akcji wielokrotnie")
    void testAddSameStockMultipleTimes() {
        Portfolio portfolio = new Portfolio(1000.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);

        portfolio.addStock(cdr, 2);
        portfolio.addStock(cdr, 3);

        assertEquals(1, portfolio.getHoldingsCount());
        assertEquals(5, portfolio.getStockQuantity(cdr));
        assertEquals(1500.0, portfolio.calculateStockValue());
        assertEquals(2500.0, portfolio.calculateTotalValue());
    }

    @Test
    @DisplayName("Dodanie różnych akcji")
    void testAddDifferentStocks() {
        Portfolio portfolio = new Portfolio(1000.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        Stock pkn = new Stock("PKN", "PKN Orlen", 100.0);

        portfolio.addStock(cdr, 2);
        portfolio.addStock(pkn, 5);

        assertEquals(2, portfolio.getHoldingsCount());
        assertEquals(2, portfolio.getStockQuantity(cdr));
        assertEquals(5, portfolio.getStockQuantity(pkn));
        assertEquals(2 * 300 + 5 * 100, portfolio.calculateStockValue());
        assertEquals(1000 + 2 * 300 + 5 * 100, portfolio.calculateTotalValue());
    }

    @Test
    @DisplayName("Portfel pełny — dodanie 11-tej akcji")
    void testAddStockWhenFull() {
        Portfolio portfolio = new Portfolio(1000.0);

        for (int i = 1; i <= 10; i++) {
            portfolio.addStock(new Stock("S" + i, "Stock" + i, 10.0 * i), 1);
        }

        assertEquals(10, portfolio.getHoldingsCount());

        double before = portfolio.calculateTotalValue();

        portfolio.addStock(new Stock("S11", "Stock11", 110.0), 1);

        assertEquals(10, portfolio.getHoldingsCount());
        assertEquals(before, portfolio.calculateTotalValue());
    }

    @Test
    @DisplayName("Sprawdzenie ilosci dla nieistniejącej akcji")
    void testGetStockQuantityForNonExistingStock() {
        Portfolio portfolio = new Portfolio(500.0);
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);

        assertEquals(0, portfolio.getStockQuantity(cdr));
    }
}
