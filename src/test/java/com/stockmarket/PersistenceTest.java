package com.stockmarket;

import com.stockmarket.domain.Commodity;
import com.stockmarket.logic.Portfolio;
import com.stockmarket.logic.io.PortfolioPersistence;
import com.stockmarket.logic.exceptions.DataIntegrityException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveAndLoadPortfolioWithLots() {
        Portfolio portfolio = new Portfolio(1000.0);
        Commodity gold = new Commodity("GOLD", 200.0, 1.0);
        portfolio.executeBuy(gold, 3, LocalDate.of(2023, 5, 10));
        gold.setMarketPrice(250.0);
        portfolio.executeBuy(gold, 1, LocalDate.of(2023, 6, 12));

        Path file = tempDir.resolve("portfolio.txt");
        PortfolioPersistence persistence = new PortfolioPersistence();
        persistence.save(file, portfolio);

        Portfolio loaded = persistence.load(file);
        assertEquals(portfolio.getCash(), loaded.getCash(), 0.0001);
        assertEquals(4, loaded.getTotalQuantity("GOLD"));
    }

    @Test
    void shouldThrowDataIntegrityExceptionWhenLotSumDoesNotMatchDeclaredQuantity() throws Exception {
        Path file = tempDir.resolve("bad.txt");
        java.nio.file.Files.writeString(file, String.join("\n",
                "HEADER|CASH|100.00",
                "ASSET|SHARE|AAPL|10|150.00",
                "LOT|2023-05-10|5|150.00" // suma LOT = 5, declared = 10
        ));

        PortfolioPersistence persistence = new PortfolioPersistence();
        assertThrows(DataIntegrityException.class, () -> persistence.load(file));
    }
}
