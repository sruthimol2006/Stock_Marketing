package com.stocktrading.models;

public class Trade {
    private String tradeId;
    private String orderId;
    private String stockId;
    private int quantity;
    private double executionPrice;

    public Trade() {
    }

    public Trade(String tradeId, String orderId, String stockId,
                 int quantity, double executionPrice) {
        this.tradeId = tradeId;
        this.orderId = orderId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public double getExecutionPrice() {
        return executionPrice;
    }

    public void setExecutionPrice(double executionPrice) {
        this.executionPrice = executionPrice;
    }

    @Override
    public String toString() {
        return "Trade{tradeId='" + tradeId + "', orderId='" + orderId
                + "', stockId='" + stockId + "', quantity=" + quantity
                + ", executionPrice=" + executionPrice + "}";
    }
}
