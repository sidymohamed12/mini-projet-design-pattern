package com.miniprojet.view;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Produit;
import com.miniprojet.model.Stock;
import com.miniprojet.observer.impl.StockAlertObserver;
import com.miniprojet.service.IService;
import com.miniprojet.service.IStockService;
import com.miniprojet.strategy.impl.EntreeStockStrategy;
import com.miniprojet.strategy.impl.SortieStockStrategy;

public class ProduitView {
    private IService<Produit> produitService;
    private IStockService stockService;

    private final Scanner scanner = new Scanner(System.in);

    public ProduitView(IService<Produit> produitService,
            IStockService stockService) {

        this.produitService = Objects.requireNonNull(produitService);
        this.stockService = Objects.requireNonNull(stockService);
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
                        System.out.println("Au revoir !");
                    }
                    default -> System.out.println("Choix invalide.");
                }

            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        scanner.close();
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
        System.out.println("0) Quitter");
        System.out.println("Choix: ");
    }

    private void listProducts() {
        List<Produit> list = produitService.listAll();
        if (list.isEmpty()) {
            System.out.println("Aucun produit.");
            return;
        }
        list.forEach(System.out::println);
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

        System.out.print("Seuil d’alerte: ");
        int seuil = Integer.parseInt(scanner.nextLine());

        Produit product = new Produit.Builder()
                .name(name)
                .description(desc)
                .price(price)
                .build();

        product.setStock(new Stock(quantite, seuil));

        Produit saved = produitService.create(product);
        System.out.println("Produit créé: " + saved);
    }

    private void entreeStock() {
        System.out.print("ID produit: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<Produit> opt = produitService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Produit introuvable.");
            return;
        }

        System.out.print("Quantité à ajouter: ");
        int qte = Integer.parseInt(scanner.nextLine());

        stockService.setStrategy(new EntreeStockStrategy());
        stockService.appliquerMouvement(opt.get(), qte);

        System.out.println("Stock mis à jour: " + opt.get().getStock());
    }

    private void sortieStock() {
        System.out.print("ID produit: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<Produit> opt = produitService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Produit introuvable.");
            return;
        }

        System.out.print("Quantité à retirer: ");
        int qte = Integer.parseInt(scanner.nextLine());

        try {
            stockService.setStrategy(new SortieStockStrategy());
            stockService.appliquerMouvement(opt.get(), qte);
            System.out.println("Stock mis à jour: " + opt.get().getStock());
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void cloneProduct() {
        System.out.println("Id du produit à cloner: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            Produit cloned = produitService.clone(id);
            System.out.println("Produit cloné: " + cloned);
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.println("Id du produit à mettre à jour: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Produit> opt = produitService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Produit introuvable.");
            return;
        }
        System.out.println("Nouveau nom (vide = conserver): ");
        String name = scanner.nextLine();
        System.out.println("Nouvelle description (vide = conserver): ");
        String desc = scanner.nextLine();
        System.out.println("Nouveau prix (vide = conserver): ");
        String priceStr = scanner.nextLine();

        Produit existing = opt.get();
        String newName = name.isBlank() ? existing.getName() : name;
        String newDesc = desc.isBlank() ? existing.getDescription() : desc;
        double newPrice = priceStr.isBlank() ? existing.getPrice() : Double.parseDouble(priceStr);

        Produit updated = new Produit.Builder()
                .id(existing.getId())
                .name(newName)
                .description(newDesc)
                .price(newPrice)
                .build();
        try {
            Produit result = produitService.update(id, updated);
            System.out.println("Produit mis à jour: " + result);
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.println("Id du produit à supprimer: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            produitService.delete(id);
            System.out.println("Produit supprimé.");
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
