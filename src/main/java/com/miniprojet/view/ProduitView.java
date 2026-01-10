package com.miniprojet.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.miniprojet.dto.ProduitDTO;
import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Produit;
import com.miniprojet.observer.impl.StockAlertObserver;
import com.miniprojet.service.IStockService;
import com.miniprojet.service.impl.ProduitService;
import com.miniprojet.strategy.impl.EntreeStockStrategy;
import com.miniprojet.strategy.impl.SortieStockStrategy;

public class ProduitView {
    private ProduitService produitService;
    private IStockService stockService;

    private final Scanner scanner = new Scanner(System.in);

    public ProduitView(ProduitService produitService, IStockService stockService) {
        this.produitService = produitService;
        this.stockService = stockService;
    }

    public void start() {
        boolean running = true;
        stockService.addObserver(new StockAlertObserver());

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> listProducts();
                    case "2" -> createProduct();
                    case "3" -> cloneProduct();
                    case "4" -> updateProduct();
                    case "5" -> deleteProduct();
                    case "6" -> entreeStock();
                    case "7" -> sortieStock();
                    case "0" -> {
                        running = false;
                        System.out.println("Retour au menu principal");
                    }
                    default -> System.out.println("Choix invalide.");
                }

            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== MENU PRODUITS ===");
        System.out.println("1) Lister les produits");
        System.out.println("2) Créer un produit");
        System.out.println("3) Cloner un produit (Prototype)");
        System.out.println("4) Mettre à jour un produit");
        System.out.println("5) Supprimer un produit");
        System.out.println("6) Entrée de stock");
        System.out.println("7) Sortie de stock");
        System.out.println("0) Retour");
        System.out.print("Choix: ");
    }

    private void listProducts() {
        List<ProduitDTO> list = produitService.listAll();
        if (list.isEmpty()) {
            System.out.println("Aucun produit.");
            return;
        }
        list.forEach(dto -> System.out.println(
                "ID: " + dto.getId() +
                        " | " + dto.getName() +
                        " | " + dto.getDescription() +
                        " | Prix: " + dto.getPrice() +
                        " | Stock: " + dto.getQuantiteStock() +
                        (dto.isStockFaible() ? " ⚠️ FAIBLE" : "")));
    }

    private void createProduct() {
        System.out.println("=== Création produit ===");
        System.out.print("Nom: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.print("Prix: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock initial: ");
        int quantite = Integer.parseInt(scanner.nextLine());

        System.out.print("Seuil d'alerte: ");
        int seuil = Integer.parseInt(scanner.nextLine());

        ProduitDTO dto = new ProduitDTO.Builder()
                .name(name)
                .description(desc)
                .price(price)
                .quantiteStock(quantite)
                .seuilAlerte(seuil)
                .build();

        ProduitDTO saved = produitService.create(dto);
        System.out.println("✓ Produit créé avec ID: " + saved.getId());
    }

    private void entreeStock() {
        System.out.print("ID produit: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<Produit> opt = produitService.findEntityById(id);
        if (opt.isEmpty()) {
            System.out.println("Produit introuvable.");
            return;
        }

        System.out.print("Quantité à ajouter: ");
        int qte = Integer.parseInt(scanner.nextLine());

        stockService.setStrategy(new EntreeStockStrategy());
        stockService.appliquerMouvement(opt.get(), qte);

        System.out.println("✓ Stock mis à jour: " + opt.get().getStock());
    }

    private void sortieStock() {
        System.out.print("ID produit: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<Produit> opt = produitService.findEntityById(id);
        if (opt.isEmpty()) {
            System.out.println("Produit introuvable.");
            return;
        }

        System.out.print("Quantité à retirer: ");
        int qte = Integer.parseInt(scanner.nextLine());

        try {
            stockService.setStrategy(new SortieStockStrategy());
            stockService.appliquerMouvement(opt.get(), qte);
            System.out.println("✓ Stock mis à jour: " + opt.get().getStock());
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void cloneProduct() {
        System.out.print("ID du produit à cloner: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            ProduitDTO cloned = produitService.clone(id);
            System.out.println("✓ Produit cloné avec ID: " + cloned.getId());
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.print("ID du produit à mettre à jour: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<ProduitDTO> opt = produitService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Produit introuvable.");
            return;
        }

        ProduitDTO existing = opt.get();
        System.out.println("Produit actuel: " + existing.getName());

        System.out.print("Nouveau nom (vide = conserver): ");
        String name = scanner.nextLine();
        System.out.print("Nouvelle description (vide = conserver): ");
        String desc = scanner.nextLine();
        System.out.print("Nouveau prix (vide = conserver): ");
        String priceStr = scanner.nextLine();

        String newName = name.isBlank() ? existing.getName() : name;
        String newDesc = desc.isBlank() ? existing.getDescription() : desc;
        double newPrice = priceStr.isBlank() ? existing.getPrice() : Double.parseDouble(priceStr);

        ProduitDTO updated = new ProduitDTO.Builder()
                .id(existing.getId())
                .name(newName)
                .description(newDesc)
                .price(newPrice)
                .quantiteStock(existing.getQuantiteStock())
                .seuilAlerte(existing.getSeuilAlerte())
                .build();

        try {
            ProduitDTO result = produitService.update(id, updated);
            System.out.println("✓ Produit mis à jour: " + result.getName());
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.print("ID du produit à supprimer: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            produitService.delete(id);
            System.out.println("✓ Produit supprimé.");
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}