package com.stockmarket;

import com.stockmarket.domain.Commodity;
import com.stockmarket.domain.Currency;
import com.stockmarket.domain.Share;
import com.stockmarket.logic.Portfolio;
import com.stockmarket.logic.report.PortfolioReportGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReportSortingTest {

    @Test
    void shouldSortByAssetTypeThenMarketValueDesc() {
        Portfolio p = new Portfolio(100000.0);

        Share aaa = new Share("AAA", 10.0);      // MV 100
        Share ccc = new Share("CCC", 20.0);      // MV 60
        Commodity bbb = new Commodity("BBB", 50.0, 0.0); // MV 50
        Currency eur = new Currency("EUR", 4.0, 0.0);    // MV 40

        p.executeBuy(aaa, 10, LocalDate.of(2023, 1, 1));
        p.executeBuy(ccc, 3, LocalDate.of(2023, 1, 1));
        p.executeBuy(bbb, 1, LocalDate.of(2023, 1, 1));
        p.executeBuy(eur, 10, LocalDate.of(2023, 1, 1));

        String report = new PortfolioReportGenerator().generateReport(p);

        // Oczekujemy kolejności: SHARE (AAA, CCC) -> COMMODITY (BBB) -> CURRENCY (EUR)
        int idxAAA = report.indexOf("ASSET|SHARE|AAA|");
        int idxCCC = report.indexOf("ASSET|SHARE|CCC|");
        int idxBBB = report.indexOf("ASSET|COMMODITY|BBB|");
        int idxEUR = report.indexOf("ASSET|CURRENCY|EUR|");

        assertTrue(idxAAA >= 0 && idxCCC >= 0 && idxBBB >= 0 && idxEUR >= 0);
        assertTrue(idxAAA < idxCCC);
        assertTrue(idxCCC < idxBBB);
        assertTrue(idxBBB < idxEUR);
    }
}
