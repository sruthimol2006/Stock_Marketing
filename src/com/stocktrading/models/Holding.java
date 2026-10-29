package com.stocktrading.models;

public class Holding {
    private String holdingId;
    private String userId;
    private String stockId;
    private int quantity;
    private double averageBuyPrice;

    public Holding() {
    }

    public Holding(String holdingId, String userId, String stockId,
                   int quantity, double averageBuyPrice) {
        this.holdingId = holdingId;
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

    public String getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(String holdingId) {
        this.holdingId = holdingId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(double averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }

    @Override
    public String toString() {
        return "Holding{holdingId='" + holdingId + "', userId='" + userId
                + "', stockId='" + stockId + "', quantity=" + quantity
                + ", averageBuyPrice=" + averageBuyPrice + "}";
    }
}
