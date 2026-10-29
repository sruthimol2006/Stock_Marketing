package com.stocktrading.cli;

import com.stocktrading.models.Holding;
import com.stocktrading.services.HoldingService;
import com.stocktrading.services.StockService;
import java.util.List;

public class HoldingCLI {
    private final HoldingService holdingService;
    private StockService stockService;

    public HoldingCLI() {
        this(new HoldingService());
    }

    public HoldingCLI(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    public HoldingCLI(HoldingService holdingService, StockService stockService) {
        this.holdingService = holdingService;
        this.stockService = stockService;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public boolean hasHoldings(String userId) {
        for (Holding holding : viewAll()) {
            if (holding.getUserId() != null && holding.getUserId().equals(userId) && holding.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean create(Holding holding) {
        return holdingService.createHolding(holding);
    }

    public Holding view(String holdingId) {
        return holdingService.getHolding(holdingId);
    }

    public List<Holding> viewAll() {
        return holdingService.getAllHoldings();
    }

    public void displayForUser(String userId) {
        System.out.println();
        System.out.println("========================================================================================================================");
        System.out.println("                                                     CURRENT HOLDINGS                                                   ");
        System.out.println("========================================================================================================================");

        List<Holding> allHoldings = viewAll();
        boolean found = false;
        double totalInvested = 0.0;
        double totalCurrentValue = 0.0;

        for (Holding holding : allHoldings) {
            if (holding.getUserId() != null && holding.getUserId().equals(userId) && holding.getQuantity() > 0) {
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("You currently do not have any active stock holdings.");
            System.out.println("========================================================================================================================");
            return;
        }

        System.out.printf("| %-8s | %-8s | %-24s | %-8s | %-13s | %-13s | %-14s | %-16s |%n",
                "Hold ID", "Symbol", "Company Name", "Shares", "Avg Buy Price", "Current Price", "Total Value", "Profit / Loss");
        System.out.println("+----------+----------+--------------------------+----------+---------------+---------------+----------------+------------------+");

        for (Holding holding : allHoldings) {
            if (holding.getUserId() != null && holding.getUserId().equals(userId) && holding.getQuantity() > 0) {
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

                totalInvested += invested;
                totalCurrentValue += currentValue;

                String pnlStr = String.format("%sRs. %,.2f (%+.2f%%)",
                        pnl >= 0 ? "+" : "-", Math.abs(pnl), pnlPercent);

                System.out.printf("| %-8s | %-8s | %-24s | %,8d | Rs. %,9.2f | Rs. %,9.2f | Rs. %,10.2f | %16s |%n",
                        holding.getHoldingId(),
                        symbol,
                        name.length() > 24 ? name.substring(0, 21) + "..." : name,
                        holding.getQuantity(),
                        holding.getAverageBuyPrice(),
                        currentPrice,
                        currentValue,
                        pnlStr);
            }
        }

        double totalPnl = totalCurrentValue - totalInvested;
        double totalPnlPct = totalInvested > 0 ? (totalPnl / totalInvested) * 100.0 : 0.0;
        String totalPnlStr = String.format("%sRs. %,.2f (%+.2f%%)",
                totalPnl >= 0 ? "+" : "-", Math.abs(totalPnl), totalPnlPct);

        System.out.println("+----------+----------+--------------------------+----------+---------------+---------------+----------------+------------------+");
        System.out.printf("| TOTALS:             Invested: Rs. %,12.2f     Current Value: Rs. %,12.2f     P&L: %-23s |%n",
                totalInvested, totalCurrentValue, totalPnlStr);
        System.out.println("========================================================================================================================");
    }

    public boolean update(Holding holding) {
        return holdingService.updateHolding(holding);
    }

    public boolean delete(String holdingId) {
        return holdingService.deleteHolding(holdingId);
    }
}
