package com.stocktrading.repository;

import com.stocktrading.models.Portfolio;
import java.util.ArrayList;
import java.util.List;

public class PortfolioRepository {
    private final List<Portfolio> portfolios = new ArrayList<>();

    public boolean save(Portfolio portfolio) {
        if (portfolio == null || portfolio.getPortfolioId() == null
                || findById(portfolio.getPortfolioId()) != null) {
            return false;
        }
        return portfolios.add(portfolio);
    }

    public Portfolio findById(String portfolioId) {
        if (portfolioId == null) {
            return null;
        }
        for (Portfolio portfolio : portfolios) {
            if (portfolioId.equals(portfolio.getPortfolioId())) {
                return portfolio;
            }
        }
        return null;
    }

    public List<Portfolio> findAll() {
        return new ArrayList<>(portfolios);
    }

    public boolean update(Portfolio updatedPortfolio) {
        if (updatedPortfolio == null || updatedPortfolio.getPortfolioId() == null) {
            return false;
        }
        for (int i = 0; i < portfolios.size(); i++) {
            if (updatedPortfolio.getPortfolioId().equals(portfolios.get(i).getPortfolioId())) {
                portfolios.set(i, updatedPortfolio);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String portfolioId) {
        return portfolioId != null
                && portfolios.removeIf(portfolio -> portfolioId.equals(portfolio.getPortfolioId()));
    }
}
