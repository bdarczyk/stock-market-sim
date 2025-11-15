package com.stockmarket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    @Test
    @DisplayName("Tworzenie akcji")
    public void testCreateStock() {
        Stock stock = new Stock("CDR", "CD Projekt", 300.0);
        assertEquals("CDR", stock.getSymbol());
        assertEquals("CD Projekt", stock.getName());
        assertEquals(300.0, stock.getInitialPrice());
    }

    @Test
    @DisplayName("equals() — ten sam symbol")
    public void testEqualsSameSymbol() {
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("CDR", "CD Projekt RED", 400.0);
        assertEquals(s1, s2);
    }

    @Test
    @DisplayName("equals() — różne symbole")
    public void testEqualsDifferentSymbol() {
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("PKN", "PKN Orlen", 100.0);
        assertNotEquals(s1, s2);
    }

    @Test
    @DisplayName("hashCode() zgodny z equals")
    public void testHashCodeConsistency() {
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("CDR", "CD Projekt RED", 400.0);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    @DisplayName("Niepoprawne argumenty")
    public void testInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Stock(null, "Name", 100.0));
        assertThrows(IllegalArgumentException.class, () -> new Stock("SYM", null, 100.0));
        assertThrows(IllegalArgumentException.class, () -> new Stock("SYM", "Name", -1.0));
    }
}
