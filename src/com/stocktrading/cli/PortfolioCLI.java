package com.stocktrading.cli;

import com.stocktrading.models.Portfolio;
import com.stocktrading.services.PortfolioService;
import java.util.List;

public class PortfolioCLI {
    private final PortfolioService portfolioService;
    private com.stocktrading.services.HoldingService holdingService;
    private com.stocktrading.services.StockService stockService;
    private com.stocktrading.services.UserService userService;

    public PortfolioCLI() {
        this(new PortfolioService());
    }

    public PortfolioCLI(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public PortfolioCLI(PortfolioService portfolioService,
                        com.stocktrading.services.HoldingService holdingService,
                        com.stocktrading.services.StockService stockService,
                        com.stocktrading.services.UserService userService) {
        this.portfolioService = portfolioService;
        this.holdingService = holdingService;
        this.stockService = stockService;
        this.userService = userService;
    }

    public void setServices(com.stocktrading.services.HoldingService holdingService,
                            com.stocktrading.services.StockService stockService,
                            com.stocktrading.services.UserService userService) {
        this.holdingService = holdingService;
        this.stockService = stockService;
        this.userService = userService;
    }

    public boolean create(Portfolio portfolio) {
        return portfolioService.createPortfolio(portfolio);
    }

    public Portfolio view(String portfolioId) {
        return portfolioService.getPortfolio(portfolioId);
    }

    public List<Portfolio> viewAll() {
        return portfolioService.getAllPortfolios();
    }

    public void displayForUser(String userId) {
        Portfolio userPortfolio = null;
        for (Portfolio portfolio : viewAll()) {
            if (portfolio.getUserId() != null && portfolio.getUserId().equals(userId)) {
                userPortfolio = portfolio;
                break;
            }
        }

        if (userPortfolio == null) {
            System.out.println();
            System.out.println("=========================================================================================");
            System.out.println("No portfolio found for user ID: " + userId);
            System.out.println("=========================================================================================");
            return;
        }

        String userName = "User";
        String userEmail = "N/A";
        double walletBalance = 0.0;
        if (userService != null) {
            com.stocktrading.models.User user = userService.getUser(userId);
            if (user != null) {
                userName = user.getName();
                userEmail = user.getEmail();
                walletBalance = user.getWalletBalance();
            }
        }

        double totalInvested = 0.0;
        double totalStockValue = 0.0;
        int activeHoldingsCount = 0;

        List<com.stocktrading.models.Holding> userHoldings = new java.util.ArrayList<>();
        if (holdingService != null) {
            for (com.stocktrading.models.Holding holding : holdingService.getAllHoldings()) {
                if (holding.getUserId() != null && holding.getUserId().equals(userId) && holding.getQuantity() > 0) {
                    userHoldings.add(holding);
                    activeHoldingsCount++;
                    double currentPrice = holding.getAverageBuyPrice();
                    if (stockService != null) {
                        com.stocktrading.models.Stock stock = stockService.getStock(holding.getStockId());
                        if (stock != null) {
                            currentPrice = stock.getCurrentPrice();
                        }
                    }
                    totalInvested += holding.getQuantity() * holding.getAverageBuyPrice();
                    totalStockValue += holding.getQuantity() * currentPrice;
                }
            }
        }

        double netWorth = walletBalance + totalStockValue;
        double stockPnl = totalStockValue - totalInvested;
        double stockPnlPct = totalInvested > 0 ? (stockPnl / totalInvested) * 100.0 : 0.0;
        String pnlFormatted = String.format("%sRs. %,.2f (%+.2f%%)",
                stockPnl >= 0 ? "+" : "-", Math.abs(stockPnl), stockPnlPct);

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("                                    PORTFOLIO SUMMARY                                   ");
        System.out.println("=========================================================================================");
        System.out.printf(" Portfolio ID:      %-16s  Owner: %-25s%n", userPortfolio.getPortfolioId(), userName);
        System.out.printf(" User ID:           %-16s  Email: %-25s%n", userId, userEmail);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf(" Available Cash (Wallet):          Rs. %,14.2f%n", walletBalance);
        System.out.printf(" Total Invested in Stocks:         Rs. %,14.2f%n", totalInvested);
        System.out.printf(" Current Market Value of Stocks:   Rs. %,14.2f%n", totalStockValue);
        System.out.printf(" Total Portfolio Net Worth:        Rs. %,14.2f%n", netWorth);
        System.out.printf(" Total Unrealized Stock P&L:       %-25s%n", pnlFormatted);
        System.out.printf(" Active Stock Holdings:            %d position(s)%n", activeHoldingsCount);
        System.out.println("=========================================================================================");

        if (!userHoldings.isEmpty()) {
            System.out.println();
            System.out.println("--- PORTFOLIO ASSETS BREAKDOWN ---");
            System.out.printf("| %-8s | %-8s | %-24s | %-8s | %-13s | %-13s | %-14s | %-16s |%n",
                    "Hold ID", "Symbol", "Company Name", "Shares", "Avg Buy Price", "Current Price", "Total Value", "Profit / Loss");
            System.out.println("+----------+----------+--------------------------+----------+---------------+---------------+----------------+------------------+");

            for (com.stocktrading.models.Holding holding : userHoldings) {
                String symbol = "N/A";
                String name = "Unknown";
                double currentPrice = holding.getAverageBuyPrice();

                if (stockService != null) {
                    com.stocktrading.models.Stock stock = stockService.getStock(holding.getStockId());
                    if (stock != null) {
                        symbol = stock.getSymbol();
                        name = stock.getStockName();
                        currentPrice = stock.getCurrentPrice();
                    }
                }

                double invested = holding.getQuantity() * holding.getAverageBuyPrice();
                double currentValue = holding.getQuantity() * currentPrice;
                double pnl = currentValue - invested;
                double pnlPercent = invested > 0 ? (pnl / invested) * 100.0 : 0.0;

                String posPnlStr = String.format("%sRs. %,.2f (%+.2f%%)",
                        pnl >= 0 ? "+" : "-", Math.abs(pnl), pnlPercent);

                System.out.printf("| %-8s | %-8s | %-24s | %,8d | Rs. %,9.2f | Rs. %,9.2f | Rs. %,10.2f | %16s |%n",
                        holding.getHoldingId(),
                        symbol,
                        name.length() > 24 ? name.substring(0, 21) + "..." : name,
                        holding.getQuantity(),
                        holding.getAverageBuyPrice(),
                        currentPrice,
                        currentValue,
                        posPnlStr);
            }
            System.out.println("========================================================================================================================");
        }
    }

    public boolean update(Portfolio portfolio) {
        return portfolioService.updatePortfolio(portfolio);
    }

    public boolean delete(String portfolioId) {
        return portfolioService.deletePortfolio(portfolioId);
    }
}
