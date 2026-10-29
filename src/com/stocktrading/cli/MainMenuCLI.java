package com.stocktrading.cli;

import com.stocktrading.models.Order;
import java.util.Scanner;

public class MainMenuCLI {
    private static final String USER_ID = "U001";

    private final UserCLI userCLI;
    private final StockCLI stockCLI;
    private final PortfolioCLI portfolioCLI;
    private final HoldingCLI holdingCLI;
    private final OrderCLI orderCLI;
    private final TradeCLI tradeCLI;
    private final Scanner scanner;
    private boolean running = true;

    public MainMenuCLI(UserCLI userCLI, StockCLI stockCLI,
                       PortfolioCLI portfolioCLI, HoldingCLI holdingCLI,
                       OrderCLI orderCLI, TradeCLI tradeCLI, Scanner scanner) {
        this.userCLI = userCLI;
        this.stockCLI = stockCLI;
        this.portfolioCLI = portfolioCLI;
        this.holdingCLI = holdingCLI;
        this.orderCLI = orderCLI;
        this.tradeCLI = tradeCLI;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println();
        System.out.println("*****************************************************************************************");
        System.out.println("*                     WELCOME TO THE STOCK TRADING TERMINAL APPLICATION                 *");
        System.out.println("*****************************************************************************************");

        while (running) {
            displayMenu();
            Integer choice = readInteger("Enter your choice (1-10): ");
            if (choice == null) {
                running = false;
                break;
            }

            switch (choice) {
                case 1:
                    stockCLI.displayAvailableStocks();
                    break;
                case 2:
                    userCLI.displayUserDetails(USER_ID);
                    break;
                case 3:
                    handleWallet();
                    break;
                case 4:
                    orderCLI.acceptOrder(Order.OrderType.BUY, USER_ID);
                    break;
                case 5:
                    orderCLI.acceptOrder(Order.OrderType.SELL, USER_ID);
                    break;
                case 6:
                    portfolioCLI.displayForUser(USER_ID);
                    break;
                case 7:
                    holdingCLI.displayForUser(USER_ID);
                    break;
                case 8:
                    orderCLI.displayHistory();
                    break;
                case 9:
                    tradeCLI.displayHistory();
                    break;
                case 10:
                    running = false;
                    System.out.println();
                    System.out.println("=========================================================================================");
                    System.out.println("             Thank you for using the Stock Trading Platform. Goodbye!                   ");
                    System.out.println("=========================================================================================");
                    break;
                default:
                    System.out.println();
                    System.out.println("[!] Invalid choice. Please enter a number between 1 and 10.");
            }
        }
    }

    private void handleWallet() {
        userCLI.displayWalletBalance(USER_ID);
        System.out.print("Would you like to deposit additional funds? (y/n): ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("yes")) {
            System.out.print("Enter deposit amount: Rs. ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String amtStr = scanner.nextLine().trim();
            try {
                double amt = Double.parseDouble(amtStr);
                userCLI.depositFunds(USER_ID, amt);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount entered. Deposit cancelled.");
            }
        }
    }

    private void displayMenu() {
        com.stocktrading.models.User user = userCLI.view(USER_ID);
        String name = user != null ? user.getName() : "Trader";
        double balance = user != null ? user.getWalletBalance() : 0.0;

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("                             STOCK TRADING PLATFORM MENU                                ");
        System.out.println("=========================================================================================");
        System.out.printf(" Account: %-25s | Wallet Balance: Rs. %,.2f%n", name + " (" + USER_ID + ")", balance);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(" 1.  View Available Stocks");
        System.out.println(" 2.  View User Profile");
        System.out.println(" 3.  View Wallet Balance & Deposit Funds");
        System.out.println(" 4.  Buy Stock");
        System.out.println(" 5.  Sell Stock");
        System.out.println(" 6.  View Portfolio Summary");
        System.out.println(" 7.  View Stock Holdings");
        System.out.println(" 8.  View Order History");
        System.out.println(" 9.  View Trade History");
        System.out.println(" 10. Exit Application");
        System.out.println("=========================================================================================");
    }

    private Integer readInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return null;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Please enter a valid number (1-10).");
            }
        }
    }

}
