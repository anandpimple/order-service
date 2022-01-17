package com.kraken.orderservice.services;

import com.kraken.orderservice.entities.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {
    private OrderBook underTest = new OrderBook();

    @Test
    void tradeTest() {
        Order order = new Order("1234", 100, 10D, Order.Type.ASK, "dummy");
        Assertions.assertThat(underTest.trade(order)).isEqualByComparingTo(OrderStatus.NOT_SETTLED);
    }

    @Test
    void tradeTest1() {
        Order order = new Order("1234", 100, 10D, Order.Type.ASK, "dummy");
        underTest.trade(order);
        Order order1 = new Order("12345", 102, 10D, Order.Type.ASK, "dummy");
        underTest.trade(order1);

        Order order4 = new Order("123457", 1001, 12D, Order.Type.ASK, "dummy");
        underTest.trade(order4);

        System.out.println(underTest.topTradeOrdersFor(Order.Type.ASK).get(0).getNoOfUnits());

        Order order2 = new Order("123456", 201, 11D, Order.Type.BID, "dummy");
        underTest.trade(order2);
        System.out.println(underTest.topTradeOrdersFor(Order.Type.ASK).get(0).getNoOfUnits());
    }
}