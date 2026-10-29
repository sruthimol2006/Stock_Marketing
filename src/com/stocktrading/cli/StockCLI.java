package com.stocktrading.cli;

import com.stocktrading.models.Stock;
import com.stocktrading.services.StockService;
import java.util.List;

public class StockCLI {
    private final StockService stockService;

    public StockCLI() {
        this(new StockService());
    }

    public StockCLI(StockService stockService) {
        this.stockService = stockService;
    }

    public boolean create(Stock stock) {
        return stockService.createStock(stock);
    }

    public Stock view(String stockId) {
        return stockService.getStock(stockId);
    }

    public List<Stock> viewAll() {
        return stockService.getAllStocks();
    }

    public Stock findBySymbol(String symbol) {
        if (symbol == null) {
            return null;
        }
        for (Stock stock : viewAll()) {
            if (stock.getSymbol() != null && stock.getSymbol().equalsIgnoreCase(symbol.trim())) {
                return stock;
            }
        }
        return null;
    }

    public StockService getStockService() {
        return stockService;
    }

    public void displayAvailableStocks() {
        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println("                                    AVAILABLE STOCKS                                    ");
        System.out.println("=========================================================================================");
        List<Stock> stocks = viewAll();
        if (stocks.isEmpty()) {
            System.out.println("No stocks currently available in the market.");
            System.out.println("=========================================================================================");
            return;
        }
        System.out.printf("| %-8s | %-8s | %-30s | %-15s | %-16s |%n",
                "Stock ID", "Symbol", "Company Name", "Market Price", "Available Shares");
        System.out.println("+----------+----------+--------------------------------+-----------------+------------------+");
        for (Stock stock : stocks) {
            System.out.printf("| %-8s | %-8s | %-30s | Rs. %11.2f | %,16d |%n",
                    stock.getStockId(),
                    stock.getSymbol(),
                    stock.getStockName(),
                    stock.getCurrentPrice(),
                    stock.getAvailableQuantity());
        }
        System.out.println("=========================================================================================");
    }

    public boolean update(Stock stock) {
        return stockService.updateStock(stock);
    }

    public boolean delete(String stockId) {
        return stockService.deleteStock(stockId);
    }
}
