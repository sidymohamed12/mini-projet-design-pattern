package com.miniprojet.service;

import com.miniprojet.model.Produit;
import com.miniprojet.observer.StockObserver;
import com.miniprojet.strategy.StockStrategy;

public interface IStockService {
    void setStrategy(StockStrategy strategy);

    void addObserver(StockObserver observer);

    void appliquerMouvement(Produit produit, int quantite);
}
