package com.stockmarket;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    @Test
    public void testCreateStock() {
        // Sprawdza, czy konstruktor poprawnie ustawia symbol, nazwę i cenę
        Stock stock = new Stock("CDR", "CD Projekt", 300.0);
        assertEquals("CDR", stock.getSymbol());
        assertEquals("CD Projekt", stock.getName());
        assertEquals(300.0, stock.getInitialPrice());
    }

    @Test
    public void testEqualsSameSymbol() {
        // Sprawdza, że akcje z tym samym symbolem są równe, nawet jeśli nazwa i cena się różnią
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("CDR", "CD Projekt RED", 400.0);
        assertEquals(s1, s2);
    }

    @Test
    public void testEqualsDifferentSymbol() {
        // Sprawdza, że akcje o różnych symbolach nie są równe
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("PKN", "PKN Orlen", 100.0);
        assertNotEquals(s1, s2);
    }

    @Test
    public void testHashCodeConsistency() {
        // Sprawdza, że hashCode jest zgodny z equals() dla akcji o tym samym symbolu
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("CDR", "CD Projekt RED", 400.0);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
    @Test
    public void testInvalidArguments() {
        new Stock(null, "Name", 100.0);
        new Stock("SYM", null, 100.0);
        new Stock("SYM", "Name", -1.0);
    }

}
