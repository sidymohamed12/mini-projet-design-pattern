package com.miniprojet;

import com.miniprojet.factory.ServiceFactory;
import com.miniprojet.model.Produit;
import com.miniprojet.service.IService;
import com.miniprojet.service.IStockService;
import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.service.impl.StockService;
import com.miniprojet.view.ProduitView;

public class Main {
    public static void main(String[] args) {

        IService<Produit> produitService = ServiceFactory.createService(ProduitService.class);
        IStockService stockService = ServiceFactory.createService(StockService.class);

        ProduitView view = new ProduitView(produitService, stockService);

        view.start();

        System.out.println("Entrer 2 entiers");

    }
}