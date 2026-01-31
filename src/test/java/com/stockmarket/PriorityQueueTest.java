package com.stockmarket;

import com.stockmarket.domain.Share;
import com.stockmarket.logic.Order;
import com.stockmarket.logic.OrderSide;
import com.stockmarket.logic.Portfolio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueTest {

    @Test
    void shouldPrioritizeMoreAttractiveBuyLimitOrder() {
        Portfolio portfolio = new Portfolio(100000.0);
        Share abc = new Share("ABC", 110.0);

        // Market 110; BUY limits: 100 and 105 -> 105 is closer to market => should be HEAD
        portfolio.placeOrder(OrderSide.BUY, abc, 1, 100.0, LocalDate.of(2023, 1, 1));
        portfolio.placeOrder(OrderSide.BUY, abc, 1, 105.0, LocalDate.of(2023, 1, 1));

        Order head = portfolio.peekNextOrder();
        assertNotNull(head);
        assertEquals(OrderSide.BUY, head.getSide());
        assertEquals(105.0, head.getLimitPrice(), 0.0001);
    }
}
