package com.stocktrading.repository;

import com.stocktrading.models.Holding;
import java.util.ArrayList;
import java.util.List;

public class HoldingRepository {
    private final List<Holding> holdings = new ArrayList<>();

    public boolean save(Holding holding) {
        if (holding == null || holding.getHoldingId() == null
                || findById(holding.getHoldingId()) != null) {
            return false;
        }
        return holdings.add(holding);
    }

    public Holding findById(String holdingId) {
        if (holdingId == null) {
            return null;
        }
        for (Holding holding : holdings) {
            if (holdingId.equals(holding.getHoldingId())) {
                return holding;
            }
        }
        return null;
    }

    public List<Holding> findAll() {
        return new ArrayList<>(holdings);
    }

    public boolean update(Holding updatedHolding) {
        if (updatedHolding == null || updatedHolding.getHoldingId() == null) {
            return false;
        }
        for (int i = 0; i < holdings.size(); i++) {
            if (updatedHolding.getHoldingId().equals(holdings.get(i).getHoldingId())) {
                holdings.set(i, updatedHolding);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String holdingId) {
        return holdingId != null
                && holdings.removeIf(holding -> holdingId.equals(holding.getHoldingId()));
    }
}
