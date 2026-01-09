package com.miniprojet.strategy.impl;

import com.miniprojet.model.Stock;
import com.miniprojet.strategy.StockStrategy;

public class SortieStockStrategy implements StockStrategy {

    @Override
    public void appliquer(Stock stock, int quantite) {
        if (stock.getQuantite() < quantite) {
            throw new IllegalStateException("Stock insuffisant !");
        }
        stock.setQuantite(stock.getQuantite() - quantite);
    }
}
