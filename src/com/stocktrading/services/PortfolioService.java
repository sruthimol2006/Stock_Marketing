package com.stocktrading.services;

import com.stocktrading.models.Portfolio;
import com.stocktrading.repository.PortfolioRepository;
import java.util.List;

public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public PortfolioService() {
        this(new PortfolioRepository());
    }

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public boolean createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolio(String portfolioId) {
        return portfolioRepository.findById(portfolioId);
    }

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public boolean updatePortfolio(Portfolio portfolio) {
        return portfolioRepository.update(portfolio);
    }

    public boolean deletePortfolio(String portfolioId) {
        return portfolioRepository.deleteById(portfolioId);
    }
}
