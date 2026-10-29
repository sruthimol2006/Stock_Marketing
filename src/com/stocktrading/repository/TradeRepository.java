package com.stocktrading.repository;

import com.stocktrading.models.Trade;
import java.util.ArrayList;
import java.util.List;

public class TradeRepository {
    private final List<Trade> trades = new ArrayList<>();

    public boolean save(Trade trade) {
        if (trade == null || trade.getTradeId() == null || findById(trade.getTradeId()) != null) {
            return false;
        }
        return trades.add(trade);
    }

    public Trade findById(String tradeId) {
        if (tradeId == null) {
            return null;
        }
        for (Trade trade : trades) {
            if (tradeId.equals(trade.getTradeId())) {
                return trade;
            }
        }
        return null;
    }

    public List<Trade> findAll() {
        return new ArrayList<>(trades);
    }

    public boolean update(Trade updatedTrade) {
        if (updatedTrade == null || updatedTrade.getTradeId() == null) {
            return false;
        }
        for (int i = 0; i < trades.size(); i++) {
            if (updatedTrade.getTradeId().equals(trades.get(i).getTradeId())) {
                trades.set(i, updatedTrade);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String tradeId) {
        return tradeId != null && trades.removeIf(trade -> tradeId.equals(trade.getTradeId()));
    }
}
