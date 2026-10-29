package com.stocktrading.cli;

import com.stocktrading.models.Trade;
import com.stocktrading.services.TradeService;
import java.util.List;

public class TradeCLI {
    private final TradeService tradeService;
    private com.stocktrading.services.StockService stockService;

    public TradeCLI() {
        this(new TradeService());
    }

    public TradeCLI(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public TradeCLI(TradeService tradeService, com.stocktrading.services.StockService stockService) {
        this.tradeService = tradeService;
        this.stockService = stockService;
    }

    public void setStockService(com.stocktrading.services.StockService stockService) {
        this.stockService = stockService;
    }

    public boolean create(Trade trade) {
        return tradeService.createTrade(trade);
    }

    public Trade view(String tradeId) {
        return tradeService.getTrade(tradeId);
    }

    public List<Trade> viewAll() {
        return tradeService.getAllTrades();
    }

    public void displayHistory() {
        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("                                      TRADE HISTORY                                      ");
        System.out.println("=========================================================================================");
        List<Trade> trades = viewAll();
        if (trades.isEmpty()) {
            System.out.println("No trades executed yet.");
            System.out.println("=========================================================================================");
            return;
        }

        System.out.printf("| %-8s | %-8s | %-8s | %-8s | %-14s | %-16s |%n",
                "Trade ID", "Order ID", "Symbol", "Quantity", "Execution Price", "Total Amount");
        System.out.println("+----------+----------+----------+----------+----------------+------------------+");

        for (Trade trade : trades) {
            String symbol = trade.getStockId();
            if (stockService != null) {
                com.stocktrading.models.Stock stock = stockService.getStock(trade.getStockId());
                if (stock != null) {
                    symbol = stock.getSymbol();
                }
            }
            double total = trade.getQuantity() * trade.getExecutionPrice();
            System.out.printf("| %-8s | %-8s | %-8s | %,8d | Rs. %10.2f | Rs. %,12.2f |%n",
                    trade.getTradeId(),
                    trade.getOrderId(),
                    symbol,
                    trade.getQuantity(),
                    trade.getExecutionPrice(),
                    total);
        }
        System.out.println("=========================================================================================");
    }

    public boolean update(Trade trade) {
        return tradeService.updateTrade(trade);
    }

    public boolean delete(String tradeId) {
        return tradeService.deleteTrade(tradeId);
    }
}
