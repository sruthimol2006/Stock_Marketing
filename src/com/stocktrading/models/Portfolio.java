package com.stocktrading.models;

public class Portfolio {
    private String portfolioId;
    private String userId;

    public Portfolio() {
    }

    public Portfolio(String portfolioId, String userId) {
        this.portfolioId = portfolioId;
        this.userId = userId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Portfolio{portfolioId='" + portfolioId + "', userId='"
                + userId + "'}";
    }
}
