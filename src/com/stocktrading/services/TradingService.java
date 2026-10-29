package com.stocktrading.services;

import com.stocktrading.models.Holding;
import com.stocktrading.models.Order;
import com.stocktrading.models.Stock;
import com.stocktrading.models.Trade;
import com.stocktrading.models.User;
import java.util.List;

public class TradingService {
    private final UserService userService;
    private final StockService stockService;
    private final HoldingService holdingService;
    private final OrderService orderService;
    private final TradeService tradeService;

    public TradingService(UserService userService, StockService stockService,
                          HoldingService holdingService, OrderService orderService,
                          TradeService tradeService) {
        this.userService = userService;
        this.stockService = stockService;
        this.holdingService = holdingService;
        this.orderService = orderService;
        this.tradeService = tradeService;
    }

    public Order buy(String userId, String symbol, int quantity) {
        return placeOrder(userId, symbol, quantity, Order.OrderType.BUY);
    }

    public Order sell(String userId, String symbol, int quantity) {
        return placeOrder(userId, symbol, quantity, Order.OrderType.SELL);
    }

    public double previewTotal(String userId, String symbol, int quantity,
                               Order.OrderType orderType) {
        return validateOrder(userId, symbol, quantity, orderType);
    }

    private Order placeOrder(String userId, String symbol, int quantity,
                             Order.OrderType orderType) {
        double total = validateOrder(userId, symbol, quantity, orderType);
        User originalUser = userService.getUser(userId);
        Stock originalStock = findStockBySymbol(symbol);
        Holding originalHolding = findHolding(userId, originalStock.getStockId());

        long updatedStockQuantity = orderType == Order.OrderType.BUY
                ? (long) originalStock.getAvailableQuantity() - quantity
                : (long) originalStock.getAvailableQuantity() + quantity;
        long updatedHoldingQuantity = originalHolding == null
                ? quantity
                : orderType == Order.OrderType.BUY
                        ? (long) originalHolding.getQuantity() + quantity
                        : (long) originalHolding.getQuantity() - quantity;
        double updatedWalletBalance = orderType == Order.OrderType.BUY
                ? originalUser.getWalletBalance() - total
                : originalUser.getWalletBalance() + total;
        updatedWalletBalance = Math.round(updatedWalletBalance * 100.0) / 100.0;

        User updatedUser = new User(originalUser.getUserId(), originalUser.getName(),
                originalUser.getEmail(), updatedWalletBalance);
        Stock updatedStock = new Stock(originalStock.getStockId(),
                originalStock.getStockName(), originalStock.getSymbol(),
                originalStock.getCurrentPrice(), (int) updatedStockQuantity);
        Holding updatedHolding = createUpdatedHolding(originalHolding, originalUser,
                originalStock, (int) updatedHoldingQuantity, total, orderType);

        String orderId = nextOrderId();
        String tradeId = nextTradeId();
        Order order = new Order(orderId, userId, originalStock.getStockId(),
                orderType, quantity, originalStock.getCurrentPrice(),
                Order.OrderStatus.COMPLETED);
        Trade trade = new Trade(tradeId, orderId, originalStock.getStockId(),
                quantity, originalStock.getCurrentPrice());

        if (!userService.updateUser(updatedUser)) {
            throw new IllegalStateException("Could not update the user wallet.");
        }
        if (!stockService.updateStock(updatedStock)) {
            rollback(originalUser, originalStock, originalHolding, updatedHolding,
                    true, false, false);
            throw new IllegalStateException("Could not update available stock quantity.");
        }

        boolean holdingCreated = originalHolding == null;
        boolean holdingSaved = holdingCreated
                ? holdingService.createHolding(updatedHolding)
                : holdingService.updateHolding(updatedHolding);
        if (!holdingSaved) {
            rollback(originalUser, originalStock, originalHolding, updatedHolding,
                    true, true, false);
            throw new IllegalStateException("Could not update the user's holding.");
        }

        if (!orderService.createOrder(order)) {
            rollback(originalUser, originalStock, originalHolding, updatedHolding,
                    true, true, true);
            throw new IllegalStateException("Could not save the order.");
        }
        if (!tradeService.createTrade(trade)) {
            if (!orderService.deleteOrder(orderId)) {
                throw new IllegalStateException("Could not remove an incomplete order.");
            }
            rollback(originalUser, originalStock, originalHolding, updatedHolding,
                    true, true, true);
            throw new IllegalStateException("Could not save the trade.");
        }

        return order;
    }

    private double validateOrder(String userId, String symbol, int quantity,
                                 Order.OrderType orderType) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        User user = userService.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        Stock stock = findStockBySymbol(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("Stock symbol not found.");
        }
        if (stock.getCurrentPrice() <= 0) {
            throw new IllegalArgumentException("Stock price must be greater than zero.");
        }

        Holding holding = findHolding(userId, stock.getStockId());
        if (orderType == Order.OrderType.BUY) {
            if (quantity > stock.getAvailableQuantity()) {
                throw new IllegalArgumentException("Not enough shares are available.");
            }
        } else if (holding == null || quantity > holding.getQuantity()) {
            int ownedQuantity = holding == null ? 0 : holding.getQuantity();
            throw new IllegalArgumentException(
                    "Not enough shares to sell. You own " + ownedQuantity + " share(s).");
        }

        double total = stock.getCurrentPrice() * quantity;
        if (Double.isInfinite(total) || Double.isNaN(total)) {
            throw new IllegalArgumentException("The transaction amount is invalid.");
        }
        if (orderType == Order.OrderType.BUY
                && total > user.getWalletBalance()) {
            throw new IllegalArgumentException("Insufficient wallet balance.");
        }

        long updatedStockQuantity = orderType == Order.OrderType.BUY
                ? (long) stock.getAvailableQuantity() - quantity
                : (long) stock.getAvailableQuantity() + quantity;
        long updatedHoldingQuantity = holding == null
                ? quantity
                : orderType == Order.OrderType.BUY
                        ? (long) holding.getQuantity() + quantity
                        : (long) holding.getQuantity() - quantity;
        if (updatedStockQuantity > Integer.MAX_VALUE
                || updatedHoldingQuantity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The resulting share quantity is too large.");
        }

        double updatedWalletBalance = orderType == Order.OrderType.BUY
                ? user.getWalletBalance() - total
                : user.getWalletBalance() + total;
        if (updatedWalletBalance < 0 || Double.isInfinite(updatedWalletBalance)
                || Double.isNaN(updatedWalletBalance)) {
            throw new IllegalArgumentException("The resulting wallet balance is invalid.");
        }
        return total;
    }

    private Holding createUpdatedHolding(Holding originalHolding, User user,
                                         Stock stock, int quantity, double total,
                                         Order.OrderType orderType) {
        if (originalHolding == null) {
            return new Holding(nextHoldingId(), user.getUserId(), stock.getStockId(),
                    quantity, stock.getCurrentPrice());
        }

        double averageBuyPrice = originalHolding.getAverageBuyPrice();
        if (orderType == Order.OrderType.BUY) {
            averageBuyPrice = (originalHolding.getQuantity() * averageBuyPrice + total)
                    / quantity;
            averageBuyPrice = Math.round(averageBuyPrice * 100.0) / 100.0;
        }
        return new Holding(originalHolding.getHoldingId(), user.getUserId(),
                stock.getStockId(), quantity, averageBuyPrice);
    }

    private Stock findStockBySymbol(String symbol) {
        if (symbol == null) {
            return null;
        }
        for (Stock stock : stockService.getAllStocks()) {
            if (stock.getSymbol() != null && stock.getSymbol().equalsIgnoreCase(symbol.trim())) {
                return stock;
            }
        }
        return null;
    }

    private Holding findHolding(String userId, String stockId) {
        for (Holding holding : holdingService.getAllHoldings()) {
            if (holding.getUserId().equals(userId)
                    && holding.getStockId().equals(stockId)) {
                return holding;
            }
        }
        return null;
    }

    private String nextHoldingId() {
        int number = 1;
        while (holdingService.getHolding(String.format("H%03d", number)) != null) {
            number++;
        }
        return String.format("H%03d", number);
    }

    private String nextOrderId() {
        List<Order> orders = orderService.getAllOrders();
        int number = 1;
        while (containsOrderId(orders, String.format("O%03d", number))) {
            number++;
        }
        return String.format("O%03d", number);
    }

    private boolean containsOrderId(List<Order> orders, String orderId) {
        for (Order order : orders) {
            if (orderId.equals(order.getOrderId())) {
                return true;
            }
        }
        return false;
    }

    private String nextTradeId() {
        List<Trade> trades = tradeService.getAllTrades();
        int number = 1;
        while (containsTradeId(trades, String.format("T%03d", number))) {
            number++;
        }
        return String.format("T%03d", number);
    }

    private boolean containsTradeId(List<Trade> trades, String tradeId) {
        for (Trade trade : trades) {
            if (tradeId.equals(trade.getTradeId())) {
                return true;
            }
        }
        return false;
    }

    private void rollback(User originalUser, Stock originalStock,
                          Holding originalHolding, Holding updatedHolding,
                          boolean userChanged, boolean stockChanged,
                          boolean holdingChanged) {
        boolean restored = true;
        if (holdingChanged) {
            boolean holdingRestored = originalHolding == null
                    ? holdingService.deleteHolding(updatedHolding.getHoldingId())
                    : holdingService.updateHolding(originalHolding);
            if (!holdingRestored) {
                restored = false;
            }
        }
        if (stockChanged && !stockService.updateStock(originalStock)) {
            restored = false;
        }
        if (userChanged && !userService.updateUser(originalUser)) {
            restored = false;
        }
        if (!restored) {
            throw new IllegalStateException("Transaction rollback could not restore all data.");
        }
    }
}
