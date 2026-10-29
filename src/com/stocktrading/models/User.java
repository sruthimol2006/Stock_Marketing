package com.stocktrading.models;

public class User {
    private String userId;
    private String name;
    private String email;
    private double walletBalance;

    public User() {
    }

    public User(String userId, String name, String email, double walletBalance) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.walletBalance = walletBalance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name + "', email='"
                + email + "', walletBalance=" + walletBalance + "}";
    }
}
