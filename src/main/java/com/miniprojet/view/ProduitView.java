package com.miniprojet.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Produit;
import com.miniprojet.service.IService;

public class ProduitView {
    private IService<Produit> produitService;
    private final Scanner scanner = new Scanner(System.in);

    public void setproduitService(IService<Produit> produitService) {
        this.produitService = produitService;
    }

    public void start() {
        boolean running = true;
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
        System.out.println("Nom: ");
        String name = scanner.nextLine();
        System.out.println("Description: ");
        String desc = scanner.nextLine();
        System.out.println("Prix: ");
        double price = Double.parseDouble(scanner.nextLine());
        Produit product = new Produit.Builder()
                .name(name)
                .description(desc)
                .price(price)
                .build();
        Produit saved = produitService.create(product);
        System.out.println("Produit créé: " + saved);
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
