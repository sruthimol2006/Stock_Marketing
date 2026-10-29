package com.stocktrading.services;

import com.stocktrading.models.Holding;
import com.stocktrading.repository.HoldingRepository;
import java.util.List;

public class HoldingService {
    private final HoldingRepository holdingRepository;

    public HoldingService() {
        this(new HoldingRepository());
    }

    public HoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    public boolean createHolding(Holding holding) {
        return holdingRepository.save(holding);
    }

    public Holding getHolding(String holdingId) {
        return holdingRepository.findById(holdingId);
    }

    public List<Holding> getAllHoldings() {
        return holdingRepository.findAll();
    }

    public boolean updateHolding(Holding holding) {
        return holdingRepository.update(holding);
    }

    public boolean deleteHolding(String holdingId) {
        return holdingRepository.deleteById(holdingId);
    }
}
