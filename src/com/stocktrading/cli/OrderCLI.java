package com.stocktrading.cli;

import com.stocktrading.models.Order;
import com.stocktrading.services.OrderService;
import com.stocktrading.services.TradingService;
import java.util.List;
import java.util.Scanner;

public class OrderCLI {
    private final OrderService orderService;
    private TradingService tradingService;
    private StockCLI stockCLI;
    private HoldingCLI holdingCLI;
    private UserCLI userCLI;
    private Scanner scanner;

    private com.stocktrading.services.StockService stockService;

    public OrderCLI() {
        this(new OrderService());
    }

    public OrderCLI(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderCLI(OrderService orderService, TradingService tradingService,
                    StockCLI stockCLI, HoldingCLI holdingCLI, UserCLI userCLI,
                    Scanner scanner) {
        this(orderService, tradingService, stockCLI, holdingCLI, userCLI, scanner, null);
    }

    public OrderCLI(OrderService orderService, TradingService tradingService,
                    StockCLI stockCLI, HoldingCLI holdingCLI, UserCLI userCLI,
                    Scanner scanner, com.stocktrading.services.StockService stockService) {
        this.orderService = orderService;
        this.tradingService = tradingService;
        this.stockCLI = stockCLI;
        this.holdingCLI = holdingCLI;
        this.userCLI = userCLI;
        this.scanner = scanner;
        this.stockService = stockService;
    }

    public void setStockService(com.stocktrading.services.StockService stockService) {
        this.stockService = stockService;
    }

    public boolean create(Order order) {
        return orderService.createOrder(order);
    }

    public Order view(String orderId) {
        return orderService.getOrder(orderId);
    }

    public List<Order> viewAll() {
        return orderService.getAllOrders();
    }

    public void displayHistory() {
        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("                                      ORDER HISTORY                                      ");
        System.out.println("=========================================================================================");
        List<Order> orders = viewAll();
        if (orders.isEmpty()) {
            System.out.println("No orders placed yet.");
            System.out.println("=========================================================================================");
            return;
        }

        System.out.printf("| %-8s | %-8s | %-6s | %-8s | %-14s | %-15s | %-10s |%n",
                "Order ID", "Symbol", "Type", "Quantity", "Price / Share", "Total Amount", "Status");
        System.out.println("+----------+----------+--------+----------+----------------+-----------------+------------+");

        for (Order order : orders) {
            String symbol = order.getStockId();
            if (stockService != null) {
                com.stocktrading.models.Stock stock = stockService.getStock(order.getStockId());
                if (stock != null) {
                    symbol = stock.getSymbol();
                }
            }
            double total = order.getQuantity() * order.getPrice();
            System.out.printf("| %-8s | %-8s | %-6s | %,8d | Rs. %10.2f | Rs. %,11.2f | %-10s |%n",
                    order.getOrderId(),
                    symbol,
                    order.getOrderType(),
                    order.getQuantity(),
                    order.getPrice(),
                    total,
                    order.getOrderStatus());
        }
        System.out.println("=========================================================================================");
    }

    public void acceptOrder(Order.OrderType orderType, String userId) {
        if (orderType == Order.OrderType.SELL) {
            if (holdingCLI != null && !holdingCLI.hasHoldings(userId)) {
                System.out.println();
                System.out.println("------------------------------------------------------------------");
                System.out.println("You currently do not have any stock holdings to sell.");
                System.out.println("Please purchase stocks first using the 'Buy Stock' menu option.");
                System.out.println("------------------------------------------------------------------");
                return;
            }
        }

        if (orderType == Order.OrderType.BUY) {
            if (stockCLI != null) {
                stockCLI.displayAvailableStocks();
            }
        } else {
            if (holdingCLI != null) {
                holdingCLI.displayForUser(userId);
            }
        }

        System.out.println();
        System.out.print("Enter stock symbol (or 'c' to cancel): ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String symbol = scanner.nextLine().trim();
        if (symbol.isEmpty() || symbol.equalsIgnoreCase("c") || symbol.equalsIgnoreCase("cancel")) {
            System.out.println("Order cancelled.");
            return;
        }

        if (stockCLI != null) {
            com.stocktrading.models.Stock matchedStock = stockCLI.findBySymbol(symbol);
            if (matchedStock == null) {
                System.out.println();
                System.out.println("[ERROR] Stock symbol '" + symbol.toUpperCase() + "' was not found in the market.");
                return;
            }
            symbol = matchedStock.getSymbol();
        }

        Integer quantity = readInteger("Enter quantity to " + orderType + " (or '0' to cancel): ");
        if (quantity == null || quantity <= 0) {
            System.out.println("Order cancelled.");
            return;
        }

        double total;
        try {
            total = tradingService.previewTotal(userId, symbol, quantity, orderType);
        } catch (IllegalArgumentException exception) {
            System.out.println();
            System.out.println("[ERROR] Transaction cannot proceed: " + exception.getMessage());
            return;
        }

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.printf(" TRANSACTION PREVIEW (%s)%n", orderType);
        System.out.println("--------------------------------------------------");
        System.out.printf(" Stock Symbol:    %s%n", symbol.toUpperCase());
        System.out.printf(" Order Type:      %s%n", orderType);
        System.out.printf(" Shares:          %,d%n", quantity);
        System.out.printf(" Total Amount:    Rs. %,.2f%n", total);
        System.out.println("--------------------------------------------------");

        if (!confirmTransaction()) {
            System.out.println("Transaction cancelled by user.");
            return;
        }

        try {
            Order order = orderType == Order.OrderType.BUY
                    ? tradingService.buy(userId, symbol, quantity)
                    : tradingService.sell(userId, symbol, quantity);

            System.out.println();
            System.out.println("=========================================================================================");
            System.out.println("                               ORDER EXECUTED SUCCESSFULLY!                             ");
            System.out.println("=========================================================================================");
            System.out.printf(" Order ID:        %s%n", order.getOrderId());
            System.out.printf(" Order Type:      %s%n", order.getOrderType());
            System.out.printf(" Symbol:          %s%n", symbol.toUpperCase());
            System.out.printf(" Shares Executed: %,d%n", order.getQuantity());
            System.out.printf(" Executed Price:  Rs. %,.2f%n", order.getPrice());
            System.out.printf(" Total Value:     Rs. %,.2f%n", total);
            System.out.printf(" Order Status:    %s%n", order.getOrderStatus());
            System.out.println("=========================================================================================");

            if (userCLI != null) {
                userCLI.displayWalletBalance(userId);
            }
            if (holdingCLI != null) {
                holdingCLI.displayForUser(userId);
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            System.out.println();
            System.out.println("[ERROR] Transaction failed: " + exception.getMessage());
        }
    }

    private Integer readInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return null;
            }
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("0")) {
                return 0;
            }
            try {
                int val = Integer.parseInt(input);
                if (val < 0) {
                    System.out.println("Quantity cannot be negative. Enter a positive number (or '0' to cancel):");
                    continue;
                }
                return val;
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Enter a whole number (or '0' to cancel).");
            }
        }
    }

    private boolean confirmTransaction() {
        while (true) {
            System.out.print("Confirm order execution? (y/n): ");
            if (!scanner.hasNextLine()) {
                return false;
            }
            String answer = scanner.nextLine().trim();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                return true;
            }
            if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Please enter 'y' for yes or 'n' for no.");
        }
    }

    public boolean update(Order order) {
        return orderService.updateOrder(order);
    }

    public boolean delete(String orderId) {
        return orderService.deleteOrder(orderId);
    }
}
