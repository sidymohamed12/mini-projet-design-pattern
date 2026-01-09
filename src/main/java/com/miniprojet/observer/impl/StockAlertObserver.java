package com.miniprojet.observer.impl;

import com.miniprojet.model.Produit;
import com.miniprojet.observer.StockObserver;

public class StockAlertObserver implements StockObserver {

    @Override
    public void onStockChange(Produit produit) {
        if (produit.getStock().estFaible()) {
            System.out.println("⚠️ ALERTE : Stock faible pour "
                    + produit.getName()
                    + " (" + produit.getStock().getQuantite() + ")");
        }
    }
}
