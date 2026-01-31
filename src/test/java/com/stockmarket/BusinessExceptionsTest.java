package com.stockmarket;

import com.stockmarket.domain.Share;
import com.stockmarket.logic.Portfolio;
import com.stockmarket.logic.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionsTest {

    @Test
    void shouldThrowInsufficientFundsExceptionOnBuy() {
        Portfolio portfolio = new Portfolio(10.0);
        Share tesla = new Share("TSLA", 100.0);

        assertThrows(InsufficientFundsException.class, () ->
                portfolio.executeBuy(tesla, 1, LocalDate.of(2023, 1, 1))
        );
    }
}
