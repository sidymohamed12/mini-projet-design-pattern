package com.miniprojet.observer;

import com.miniprojet.model.Produit;

public interface StockObserver {
    void onStockChange(Produit produit);
}
