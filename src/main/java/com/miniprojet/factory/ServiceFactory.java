package com.miniprojet.factory;

import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.service.impl.StockService;

/**
 * Factory Method simple pour créer des services.
 * Facile à étendre si vous ajoutez d'autres controllers.
 */
public final class ServiceFactory {

    private ServiceFactory() {
    }

    public static <T> T createService(Class<T> clazz) {

        if (clazz.equals(ProduitService.class)) {
            return (T) new ProduitService();
        }

        if (clazz.equals(StockService.class)) {
            return (T) new StockService();
        }

        throw new IllegalArgumentException("Unknown service type: " + clazz.getSimpleName());
    }
}