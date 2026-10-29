package com.stocktrading;

import com.stocktrading.cli.HoldingCLI;
import com.stocktrading.cli.MainMenuCLI;
import com.stocktrading.cli.OrderCLI;
import com.stocktrading.cli.PortfolioCLI;
import com.stocktrading.cli.StockCLI;
import com.stocktrading.cli.TradeCLI;
import com.stocktrading.cli.UserCLI;
import com.stocktrading.models.Holding;
import com.stocktrading.models.Portfolio;
import com.stocktrading.models.Stock;
import com.stocktrading.models.User;
import com.stocktrading.repository.HoldingRepository;
import com.stocktrading.repository.OrderRepository;
import com.stocktrading.repository.PortfolioRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.TradeRepository;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.services.HoldingService;
import com.stocktrading.services.OrderService;
import com.stocktrading.services.PortfolioService;
import com.stocktrading.services.StockService;
import com.stocktrading.services.TradeService;
import com.stocktrading.services.TradingService;
import com.stocktrading.services.UserService;
import java.util.Scanner;

public class Main {
    private static final String USER_ID = "U001";

    public static void main(String[] args) {
        UserService userService = new UserService(new UserRepository());
        StockService stockService = new StockService(new StockRepository());
        PortfolioService portfolioService = new PortfolioService(new PortfolioRepository());
        HoldingService holdingService = new HoldingService(new HoldingRepository());
        OrderService orderService = new OrderService(new OrderRepository());
        TradeService tradeService = new TradeService(new TradeRepository());
        TradingService tradingService = new TradingService(userService, stockService,
                holdingService, orderService, tradeService);

        UserCLI userCLI = new UserCLI(userService);
        StockCLI stockCLI = new StockCLI(stockService);
        PortfolioCLI portfolioCLI = new PortfolioCLI(portfolioService, holdingService, stockService, userService);
        HoldingCLI holdingCLI = new HoldingCLI(holdingService, stockService);
        Scanner scanner = new Scanner(System.in);
        OrderCLI orderCLI = new OrderCLI(orderService, tradingService,
                stockCLI, holdingCLI, userCLI, scanner, stockService);
        TradeCLI tradeCLI = new TradeCLI(tradeService, stockService);

        // Seed initial user and portfolio
        userCLI.create(new User(USER_ID, "Alex Morgan", "alex@example.com", 10000.00));
        portfolioCLI.create(new Portfolio("P001", USER_ID));

        // Seed available stocks in the market
        stockCLI.create(new Stock("S001", "Apple Inc.", "AAPL", 182.50, 5000));
        stockCLI.create(new Stock("S002", "Alphabet Inc.", "GOOGL", 138.25, 3500));
        stockCLI.create(new Stock("S003", "Microsoft Corporation", "MSFT", 328.75, 4000));
        stockCLI.create(new Stock("S004", "Amazon.com Inc.", "AMZN", 129.50, 4500));
        stockCLI.create(new Stock("S005", "Tesla Inc.", "TSLA", 242.60, 2500));
        stockCLI.create(new Stock("S006", "NVIDIA Corporation", "NVDA", 460.00, 2000));
        stockCLI.create(new Stock("S007", "Example Corporation", "EXM", 125.50, 1000));

        // Seed initial holding and corresponding order/trade history for the user
        holdingCLI.create(new Holding("H001", USER_ID, "S007", 10, 120.00));
        orderService.createOrder(new com.stocktrading.models.Order("O001", USER_ID, "S007",
                com.stocktrading.models.Order.OrderType.BUY, 10, 120.00,
                com.stocktrading.models.Order.OrderStatus.COMPLETED));
        tradeService.createTrade(new com.stocktrading.models.Trade("T001", "O001", "S007", 10, 120.00));

        MainMenuCLI menu = new MainMenuCLI(userCLI, stockCLI, portfolioCLI,
                holdingCLI, orderCLI, tradeCLI, scanner);
        menu.run();
    }
}
