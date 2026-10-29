package com.stocktrading.services;

import com.stocktrading.models.Stock;
import com.stocktrading.repository.StockRepository;
import java.util.List;

public class StockService {
    private final StockRepository stockRepository;

    public StockService() {
        this(new StockRepository());
    }

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public boolean createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock getStock(String stockId) {
        return stockRepository.findById(stockId);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public boolean updateStock(Stock stock) {
        return stockRepository.update(stock);
    }

    public boolean deleteStock(String stockId) {
        return stockRepository.deleteById(stockId);
    }
}
