package com.stocktrading.repository;

import com.stocktrading.models.Stock;
import java.util.ArrayList;
import java.util.List;

public class StockRepository {
    private final List<Stock> stocks = new ArrayList<>();

    public boolean save(Stock stock) {
        if (stock == null || stock.getStockId() == null || findById(stock.getStockId()) != null) {
            return false;
        }
        return stocks.add(stock);
    }

    public Stock findById(String stockId) {
        if (stockId == null) {
            return null;
        }
        for (Stock stock : stocks) {
            if (stockId.equals(stock.getStockId())) {
                return stock;
            }
        }
        return null;
    }

    public List<Stock> findAll() {
        return new ArrayList<>(stocks);
    }

    public boolean update(Stock updatedStock) {
        if (updatedStock == null || updatedStock.getStockId() == null) {
            return false;
        }
        for (int i = 0; i < stocks.size(); i++) {
            if (updatedStock.getStockId().equals(stocks.get(i).getStockId())) {
                stocks.set(i, updatedStock);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String stockId) {
        return stockId != null && stocks.removeIf(stock -> stockId.equals(stock.getStockId()));
    }
}
