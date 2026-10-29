package com.stocktrading.cli;

import com.stocktrading.models.User;
import com.stocktrading.services.UserService;
import java.util.List;

public class UserCLI {
    private final UserService userService;

    public UserCLI() {
        this(new UserService());
    }

    public UserCLI(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public boolean create(User user) {
        return userService.createUser(user);
    }

    public User view(String userId) {
        return userService.getUser(userId);
    }

    public List<User> viewAll() {
        return userService.getAllUsers();
    }

    public void displayUserDetails(String userId) {
        User user = view(userId);
        if (user == null) {
            System.out.println();
            System.out.println("User not found: " + userId);
            return;
        }
        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("                                      USER PROFILE                                       ");
        System.out.println("=========================================================================================");
        System.out.printf(" User ID:          %s%n", user.getUserId());
        System.out.printf(" Full Name:        %s%n", user.getName());
        System.out.printf(" Email Address:    %s%n", user.getEmail());
        System.out.printf(" Wallet Balance:   Rs. %,.2f%n", user.getWalletBalance());
        System.out.println("=========================================================================================");
    }

    public void displayWalletBalance(String userId) {
        User user = view(userId);
        if (user == null) {
            System.out.println();
            System.out.println("User not found: " + userId);
            return;
        }
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.printf(" Current Wallet Balance: Rs. %,.2f%n", user.getWalletBalance());
        System.out.println("--------------------------------------------------");
    }

    public boolean depositFunds(String userId, double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return false;
        }
        User user = view(userId);
        if (user == null) {
            System.out.println("User not found.");
            return false;
        }
        double newBalance = Math.round((user.getWalletBalance() + amount) * 100.0) / 100.0;
        User updated = new User(user.getUserId(), user.getName(), user.getEmail(), newBalance);
        if (userService.updateUser(updated)) {
            System.out.println();
            System.out.printf("Successfully deposited Rs. %,.2f into your wallet!%n", amount);
            System.out.printf("New Wallet Balance: Rs. %,.2f%n", newBalance);
            return true;
        } else {
            System.out.println("Failed to update wallet balance.");
            return false;
        }
    }

    public boolean update(User user) {
        return userService.updateUser(user);
    }

    public boolean delete(String userId) {
        return userService.deleteUser(userId);
    }
}
