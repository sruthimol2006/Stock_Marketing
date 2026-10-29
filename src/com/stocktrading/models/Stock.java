package com.stocktrading.models;

public class Stock {
    private String stockId;
    private String stockName;
    private String symbol;
    private double currentPrice;
    private int availableQuantity;

    public Stock() {
    }

    public Stock(String stockId, String stockName, String symbol,
                 double currentPrice, int availableQuantity) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.availableQuantity = availableQuantity;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Stock{stockId='" + stockId + "', stockName='" + stockName
                + "', symbol='" + symbol + "', currentPrice=" + currentPrice
                + ", availableQuantity=" + availableQuantity + "}";
    }
}
