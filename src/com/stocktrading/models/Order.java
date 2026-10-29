package com.stocktrading.models;

public class Order {
    public enum OrderType {
        BUY,
        SELL
    }

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED,
        REJECTED
    }

    private String orderId;
    private String userId;
    private String stockId;
    private OrderType orderType;
    private int quantity;
    private double price;
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(String orderId, String userId, String stockId,
                 OrderType orderType, int quantity, double price,
                 OrderStatus orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.stockId = stockId;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', userId='" + userId
                + "', stockId='" + stockId + "', orderType=" + orderType
                + ", quantity=" + quantity + ", price=" + price
                + ", orderStatus=" + orderStatus + "}";
    }
}
