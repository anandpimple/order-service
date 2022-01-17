package com.kraken.orderservice.services;

import com.kraken.orderservice.entities.Order;

import java.util.*;

public class OrderBook {
    private final EnumMap<Order.Type, TreeMap<Double, LinkedList<Order>>> orderBook= new EnumMap<>(Order.Type.class);

    public OrderBook() {
      orderBook.put(Order.Type.BID, new TreeMap<>(Comparator.reverseOrder()));
      orderBook.put(Order.Type.ASK, new TreeMap<>());
    }


    public OrderStatus trade(Order order) {
        Order.Type counterType = order.getOrderType() == Order.Type.ASK ? Order.Type.BID : Order.Type.ASK;

        if(!orderBook.get(counterType).isEmpty()) {
                List<Double> emptyLevelList = new ArrayList<>();
                for( Double level : orderBook.get(counterType).navigableKeySet()) {
                    if(counterType == Order.Type.ASK ? level.compareTo(order.getPerUnitValue()) > 0 : level.compareTo(order.getPerUnitValue()) < 0){
                        break;
                    } else {
                        settleTradeUnitsFromLevel(order, counterType, level);
                        if(orderBook.get(counterType).get(level).isEmpty()){
                            emptyLevelList.add(level);
                        }
                    }
                }

            emptyLevelList.stream().forEach(removeLevel -> orderBook.get(counterType).remove(removeLevel));
        }

        if(order.getStatus() != OrderStatus.SETTLED) addOrder(order);

        return order.getStatus();
    }

    private void settleTradeUnitsFromLevel(Order order, Order.Type counterType, Double level) {
        List<Order> ordersForRemoval = new ArrayList<>();

        for(Order counterOrder : orderBook.get(counterType).get(level)) {
            if(counterOrder.getNoOfUnits() >= order.getNoOfUnits()) {
                counterOrder.setNoOfUnits(counterOrder.getNoOfUnits() - order.getNoOfUnits());
                if(counterOrder.getNoOfUnits() == 0) ordersForRemoval.add(counterOrder);
                order.setNoOfUnits(0);
                order.setStatus(OrderStatus.SETTLED);
                counterOrder.setStatus(OrderStatus.PARTIALLY_SETTLED);
                break;
            } else {
                order.setNoOfUnits(order.getNoOfUnits() - counterOrder.getNoOfUnits());
                ordersForRemoval.add(counterOrder);
                order.setStatus(OrderStatus.PARTIALLY_SETTLED);
            }
        }

        orderBook.get(counterType).get(level).removeAll(ordersForRemoval);
    }

    public List<Order> topTradeOrdersFor(Order.Type orderType) {
        return orderBook.get(orderType).firstEntry().getValue();
    }

    private void addOrder(Order order){
        orderBook
                .get(order.getOrderType()).computeIfAbsent(order.getPerUnitValue(), key -> new LinkedList<>())
                .add(order);
    }
}