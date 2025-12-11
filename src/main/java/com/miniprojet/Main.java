package com.miniprojet;

import com.miniprojet.factory.ServiceFactory;
import com.miniprojet.model.Produit;
import com.miniprojet.service.IService;
import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.view.ProduitView;

public class Main {
    public static void main(String[] args) {
        ProduitView view = new ProduitView();

        IService<Produit> produitService = ServiceFactory.createService(ProduitService.class);

        view.setproduitService(produitService);
        view.start();
    }
}