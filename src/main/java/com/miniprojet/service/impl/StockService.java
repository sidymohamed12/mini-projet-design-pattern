package com.miniprojet.service.impl;

import com.miniprojet.model.Produit;
import com.miniprojet.observer.StockObserver;
import com.miniprojet.observer.impl.StockSubject;
import com.miniprojet.service.IStockService;
import com.miniprojet.strategy.StockStrategy;

public class StockService implements IStockService {

    private StockStrategy strategy;
    private final StockSubject subject = new StockSubject();

    @Override
    public void setStrategy(StockStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void addObserver(StockObserver observer) {
        subject.addObserver(observer);
    }

    @Override
    public void appliquerMouvement(Produit produit, int quantite) {
        strategy.appliquer(produit.getStock(), quantite);
        subject.notifyObservers(produit);
    }
}
