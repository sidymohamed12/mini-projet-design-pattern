package com.miniprojet.observer.impl;

import java.util.ArrayList;
import java.util.List;

import com.miniprojet.model.Produit;
import com.miniprojet.observer.StockObserver;

public class StockSubject {

    private final List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Produit produit) {
        observers.forEach(o -> o.onStockChange(produit));
    }
}