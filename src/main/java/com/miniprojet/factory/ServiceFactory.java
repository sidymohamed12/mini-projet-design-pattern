package com.miniprojet.factory;

import com.miniprojet.service.impl.ProduitService;

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

        throw new IllegalArgumentException("Unknown service type: " + clazz.getSimpleName());
    }
}