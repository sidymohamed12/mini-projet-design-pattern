package com.miniprojet.factory;

import com.miniprojet.repository.IRepository;
import com.miniprojet.service.impl.ClientService;
import com.miniprojet.service.impl.CommandeService;
import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.service.impl.StockService;

/**
 * Factory Method Pattern - Crée des services avec injection de dépendances
 * Les repositories sont injectés dans les services
 */
public final class ServiceFactory {

    private ServiceFactory() {
    }

    /**
     * Crée un service en injectant le repository requis
     */
    public static ProduitService createProduitService(IRepository produitRepo) {
        return new ProduitService(produitRepo);
    }

    public static ClientService createClientService(IRepository clientRepo) {
        return new ClientService(clientRepo);
    }

    public static CommandeService createCommandeService(
            IRepository commandeRepo,
            IRepository produitRepo,
            StockService stockService) {
        return new CommandeService(commandeRepo, produitRepo, stockService);
    }

    public static StockService createStockService() {
        return new StockService();
    }
}