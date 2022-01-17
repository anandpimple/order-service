package com.kraken.orderservice.entities;

import com.kraken.orderservice.services.OrderStatus;

import java.io.Serializable;

public class Order implements Serializable {
    public enum Type {
        ASK, BID
    }

    private final String orderId;
    private final Double perUnitValue;
    private final Type orderType;
    private final String instrument;

    private Integer noOfUnits;
    private OrderStatus status = OrderStatus.NOT_SETTLED;

    public Order(String orderId, Integer noOfUnits, Double perUnitValue, Type orderType, String instrument) {
        this.orderId = orderId;
        this.noOfUnits = noOfUnits;
        this.perUnitValue = perUnitValue;
        this.orderType = orderType;
        this.instrument = instrument;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Integer noOfUnits){
        this.noOfUnits = noOfUnits;
    }

    public Double getPerUnitValue() {
        return perUnitValue;
    }

    public Type getOrderType() {
        return orderType;
    }

    public String getInstrument() {
        return instrument;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }
}
