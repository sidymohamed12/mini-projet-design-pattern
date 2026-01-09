package com.miniprojet.strategy;

import com.miniprojet.model.Stock;

public interface StockStrategy {
    void appliquer(Stock stock, int quantite);
}
