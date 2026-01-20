package com.miniprojet;

import com.miniprojet.factory.ReflectionFactory;
import com.miniprojet.service.impl.ClientService;
import com.miniprojet.service.impl.CommandeService;
import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.service.impl.StockService;
import com.miniprojet.view.ClientView;
import com.miniprojet.view.CommandeView;
import com.miniprojet.view.ProduitView;

import java.util.Scanner;

/**
 * Application principale - Système de Gestion Commerciale v3.0
 * 
 * Architecture et Design Patterns:
 * 
 * SINGLETON PATTERN:
 * - Database: instance unique de la base de données en mémoire
 * - Repositories: instances uniques (ProduitRepository, ClientRepository,
 * CommandeRepository)
 * - ReflectionFactory: instance unique pour gérer les beans
 * 
 * FACTORY METHOD PATTERN avec RÉFLEXION:
 * - ReflectionFactory: création automatique avec détection des dépendances
 * - Configuration YAML externe
 * - Cache intelligent des instances (Map-based)
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
 * DEPENDENCY INJECTION via RÉFLEXION:
 * - Détection automatique des dépendances
 * - Injection par constructeur
 * - Résolution récursive
 * 
 * DATA TRANSFER OBJECTS (DTOs):
 * - Séparation entre la couche métier (Entity) et présentation (DTO)
 * - Conversion via Builder Pattern dans les services
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ReflectionFactory factory = ReflectionFactory.getInstance("factory-config.yml");

        StockService stockService = factory.getBean("IStockService");
        ProduitService produitService = factory.getBean("IProduitService");
        ClientService clientService = factory.getBean("IClientService");
        CommandeService commandeService = factory.getBean("ICommandeService");

        ProduitView produitView = new ProduitView(produitService, stockService);
        ClientView clientView = new ClientView(clientService, scanner);
        CommandeView commandeView = new CommandeView(
                commandeService,
                clientService,
                produitService,
                scanner);


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
                    System.out.println("║          Au revoir!                  ║");
                    System.out.println("╚═══════════════════════════════════════╝");
                }
                default -> System.out.println("❌ Choix invalide.");
            }
        }

        scanner.close();
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("         SYSTÈME DE GESTION COMMERCIALE");
        System.out.println("═".repeat(70));
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