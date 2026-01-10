package com.miniprojet;

import com.miniprojet.database.Database;
import com.miniprojet.factory.RepositoryFactory;
import com.miniprojet.factory.ServiceFactory;
import com.miniprojet.model.Client;
import com.miniprojet.model.Commande;
import com.miniprojet.model.Produit;
import com.miniprojet.repository.IRepository;
import com.miniprojet.repository.impl.ClientRepository;
import com.miniprojet.repository.impl.CommandeRepository;
import com.miniprojet.repository.impl.ProduitRepository;
import com.miniprojet.service.impl.ClientService;
import com.miniprojet.service.impl.CommandeService;
import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.service.impl.StockService;
import com.miniprojet.view.ClientView;
import com.miniprojet.view.CommandeView;
import com.miniprojet.view.ProduitView;

import java.util.Scanner;

/**
 * Application principale - Système de Gestion Commerciale
 * 
 * Architecture et Design Patterns:
 * 
 * SINGLETON PATTERN:
 * - Database: instance unique de la base de données en mémoire
 * - ProduitRepository, ClientRepository, CommandeRepository: instances uniques
 * 
 * FACTORY METHOD PATTERN:
 * - RepositoryFactory: crée les repositories Singleton
 * - ServiceFactory: crée les services avec injection de dépendances
 * 
 * BUILDER PATTERN:
 * - Toutes les entités (Produit, Client, Commande)
 * - Tous les DTOs (ProduitDTO, ClientDTO, CommandeDTO)
 * 
 * PROTOTYPE PATTERN:
 * - Clone pour toutes les entités et DTOs
 * 
 * OBSERVER PATTERN:
 * - StockAlertObserver surveille les changements de stock
 * 
 * STRATEGY PATTERN:
 * - EntreeStockStrategy, SortieStockStrategy
 * 
 * DEPENDENCY INJECTION:
 * - Les repositories sont injectés dans les services
 * - Les services utilisent des interfaces (IRepository)
 * 
 * DATA TRANSFER OBJECTS (DTOs):
 * - Séparation entre la couche métier (Entity) et présentation (DTO)
 * - Conversion via Builder Pattern dans les services
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE GESTION COMMERCIALE - v2.0      ║");
        System.out.println("║   Architecture: SOLID + Design Patterns       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // ========================================
        // ÉTAPE 1: Initialisation de la Database (Singleton)
        // ========================================
        Database database = Database.getInstance();
        System.out.println("✓ Database initialisée (Singleton Pattern)");

        // ========================================
        // ÉTAPE 2: Création des Repositories via RepositoryFactory
        // ========================================
        System.out.println("\n[Factory Pattern] Création des Repositories...");

        IRepository<Produit> produitRepository = RepositoryFactory.createRepository(ProduitRepository.class);
        System.out.println("  ✓ ProduitRepository créé (Singleton)");

        IRepository<Client> clientRepository = RepositoryFactory.createRepository(ClientRepository.class);
        System.out.println("  ✓ ClientRepository créé (Singleton)");

        IRepository<Commande> commandeRepository = RepositoryFactory.createRepository(CommandeRepository.class);
        System.out.println("  ✓ CommandeRepository créé (Singleton)");

        // ========================================
        // ÉTAPE 3: Création des Services via ServiceFactory avec injection
        // ========================================
        System.out.println("\n[Dependency Injection] Création des Services...");

        // Service Stock (sans dépendance)
        StockService stockService = ServiceFactory.createStockService();
        System.out.println("  ✓ StockService créé");

        // Service Produit (injection: ProduitRepository)
        ProduitService produitService = ServiceFactory.createProduitService(produitRepository);
        System.out.println("  ✓ ProduitService créé avec injection de ProduitRepository");

        // Service Client (injection: ClientRepository)
        ClientService clientService = ServiceFactory.createClientService(clientRepository);
        System.out.println("  ✓ ClientService créé avec injection de ClientRepository");

        // Service Commande (injection: CommandeRepository, ProduitRepository,
        // StockService)
        CommandeService commandeService = ServiceFactory.createCommandeService(
                commandeRepository,
                produitRepository,
                stockService);
        System.out.println("  ✓ CommandeService créé avec injection de:");
        System.out.println("    - CommandeRepository");
        System.out.println("    - ProduitRepository");
        System.out.println("    - StockService");

        // ========================================
        // ÉTAPE 4: Création des Vues (Couche Présentation)
        // ========================================
        System.out.println("\n[View Layer] Initialisation des vues...");

        Scanner scanner = new Scanner(System.in);

        ProduitView produitView = new ProduitView(produitService, stockService);
        ClientView clientView = new ClientView(clientService, scanner);
        CommandeView commandeView = new CommandeView(
                commandeService,
                clientService,
                produitService,
                scanner);

        System.out.println("  ✓ Vues initialisées");
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Application prête! Les services utilisent des DTOs.");
        System.out.println("=".repeat(50));

        // ========================================
        // BOUCLE PRINCIPALE DE L'APPLICATION
        // ========================================
        boolean running = true;

        while (running) {
            afficherMenuPrincipal();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> gererProduits(produitView);
                case "2" -> gererClients(clientView, scanner);
                case "3" -> gererCommandes(commandeView, scanner);
                case "0" -> {
                    running = false;
                    System.out.println("\n╔═══════════════════════════════════════╗");
                    System.out.println("║   Merci d'avoir utilisé le système   ║");
                    System.out.println("║          Au revoir! 👋                ║");
                    System.out.println("╚═══════════════════════════════════════╝");
                }
                default -> System.out.println("❌ Choix invalide.");
            }
        }

        scanner.close();
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("         SYSTÈME DE GESTION COMMERCIALE");
        System.out.println("═".repeat(50));
        System.out.println("1) 📦 Gestion des Produits");
        System.out.println("2) 👥 Gestion des Clients");
        System.out.println("3) 🛒 Gestion des Commandes");
        System.out.println("0) 🚪 Quitter");
        System.out.println("═".repeat(50));
        System.out.print("Votre choix: ");
    }

    private static void gererProduits(ProduitView view) {
        view.start();
    }

    private static void gererClients(ClientView view, Scanner scanner) {
        boolean continuer = true;
        while (continuer) {
            view.displayMenu();
            String choice = scanner.nextLine().trim();
            if ("0".equals(choice)) {
                continuer = false;
            } else {
                view.handleChoice(choice);
            }
        }
    }

    private static void gererCommandes(CommandeView view, Scanner scanner) {
        boolean continuer = true;
        while (continuer) {
            view.displayMenu();
            String choice = scanner.nextLine().trim();
            if ("0".equals(choice)) {
                continuer = false;
            } else {
                view.handleChoice(choice);
            }
        }
    }

}