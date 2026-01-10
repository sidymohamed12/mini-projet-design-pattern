package com.miniprojet.factory;

import com.miniprojet.repository.impl.ClientRepository;
import com.miniprojet.repository.impl.CommandeRepository;
import com.miniprojet.repository.impl.ProduitRepository;

/**
 * Factory Method Pattern - Crée des instances Singleton de repositories
 * Garantit qu'une seule instance de chaque repository existe
 */
public final class RepositoryFactory {

    private RepositoryFactory() {
    }

    /**
     * Crée ou retourne l'instance Singleton du repository demandé
     */
    @SuppressWarnings("unchecked")
    public static <T> T createRepository(Class<T> clazz) {

        if (clazz.equals(ProduitRepository.class)) {
            return (T) ProduitRepository.getInstance();
        }

        if (clazz.equals(ClientRepository.class)) {
            return (T) ClientRepository.getInstance();
        }

        if (clazz.equals(CommandeRepository.class)) {
            return (T) CommandeRepository.getInstance();
        }

        throw new IllegalArgumentException("Unknown repository type: " + clazz.getSimpleName());
    }
}