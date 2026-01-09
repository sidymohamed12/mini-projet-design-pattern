package com.miniprojet.strategy.impl;

import com.miniprojet.model.Stock;
import com.miniprojet.strategy.StockStrategy;

public class EntreeStockStrategy implements StockStrategy {

    @Override
    public void appliquer(Stock stock, int quantite) {
        stock.setQuantite(stock.getQuantite() + quantite);
    }
}
