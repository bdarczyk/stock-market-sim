package com.stockmarket;

import com.stockmarket.domain.Commodity;
import com.stockmarket.logic.Portfolio;
import com.stockmarket.logic.SaleLotResult;
import com.stockmarket.logic.SaleReport;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FifoSaleTest {

    @Test
    void shouldCalculateFifoProfitAcrossMultipleLotsAndPartialLot() {
        Portfolio portfolio = new Portfolio(100000.0);

        Commodity xyz = new Commodity("XYZ", 100.0, 0.0);

        // Lot A: 10 @ 100
        portfolio.executeBuy(xyz, 10, LocalDate.of(2023, 1, 1));

        // Lot B: 10 @ 120
        xyz.setMarketPrice(120.0);
        portfolio.executeBuy(xyz, 10, LocalDate.of(2023, 2, 1));

        // Sell 15 @ 150
        xyz.setMarketPrice(150.0);
        SaleReport report = portfolio.executeSell(xyz, 15, LocalDate.of(2023, 3, 1));

        assertNotNull(report);
        assertEquals("XYZ", report.getTicker());
        assertEquals(15, report.getQuantitySold());

        // Expected P&L: 10*(150-100) + 5*(150-120) = 650
        assertEquals(650.0, report.getTotalProfitAndLoss(), 0.0001);

        assertEquals(2, report.getLotResults().size());
        SaleLotResult r1 = report.getLotResults().get(0);
        SaleLotResult r2 = report.getLotResults().get(1);

        assertEquals(LocalDate.of(2023, 1, 1), r1.getLotDate());
        assertEquals(10, r1.getQuantityFromLot());
        assertEquals(500.0, r1.getProfitAndLoss(), 0.0001);

        assertEquals(LocalDate.of(2023, 2, 1), r2.getLotDate());
        assertEquals(5, r2.getQuantityFromLot());
        assertEquals(150.0, r2.getProfitAndLoss(), 0.0001);

        // Remaining should be 5
        assertEquals(5, portfolio.getTotalQuantity("XYZ"));
    }
}
