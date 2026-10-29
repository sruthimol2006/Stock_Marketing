package com.stocktrading.services;

import com.stocktrading.models.Trade;
import com.stocktrading.repository.TradeRepository;
import java.util.List;

public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService() {
        this(new TradeRepository());
    }

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public boolean createTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    public Trade getTrade(String tradeId) {
        return tradeRepository.findById(tradeId);
    }

    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    public boolean updateTrade(Trade trade) {
        return tradeRepository.update(trade);
    }

    public boolean deleteTrade(String tradeId) {
        return tradeRepository.deleteById(tradeId);
    }
}
