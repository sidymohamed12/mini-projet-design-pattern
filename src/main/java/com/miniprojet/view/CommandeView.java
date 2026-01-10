package com.miniprojet.view;

import com.miniprojet.dto.ClientDTO;
import com.miniprojet.dto.CommandeDTO;
import com.miniprojet.dto.LigneCommandeDTO;
import com.miniprojet.dto.ProduitDTO;
import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Client;
import com.miniprojet.model.Produit;
import com.miniprojet.model.StatutCommande;
import com.miniprojet.service.impl.ClientService;
import com.miniprojet.service.impl.CommandeService;
import com.miniprojet.service.impl.ProduitService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CommandeView {

    private final CommandeService commandeService;
    private final ClientService clientService;
    private final ProduitService produitService;
    private final Scanner scanner;

    public CommandeView(CommandeService commandeService,
            ClientService clientService,
            ProduitService produitService,
            Scanner scanner) {
        this.commandeService = commandeService;
        this.clientService = clientService;
        this.produitService = produitService;
        this.scanner = scanner;
    }

    public void displayMenu() {
        System.out.println("\n=== GESTION COMMANDES ===");
        System.out.println("1) Lister les commandes");
        System.out.println("2) Créer une commande");
        System.out.println("3) Valider une commande");
        System.out.println("4) Annuler une commande");
        System.out.println("5) Voir les commandes d'un client");
        System.out.println("6) Voir les commandes par statut");
        System.out.println("7) Cloner une commande");
        System.out.println("0) Retour");
        System.out.print("Choix: ");
    }

    public void handleChoice(String choice) {
        try {
            switch (choice) {
                case "1" -> listerCommandes();
                case "2" -> creerCommande();
                case "3" -> validerCommande();
                case "4" -> annulerCommande();
                case "5" -> commandesParClient();
                case "6" -> commandesParStatut();
                case "7" -> clonerCommande();
                case "0" -> {
                }
                default -> System.out.println("Choix invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void listerCommandes() {
        List<CommandeDTO> commandes = commandeService.listAll();
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande.");
            return;
        }
        commandes.forEach(this::afficherCommande);
    }

    private void creerCommande() {
        System.out.println("\n=== Création commande ===");

        // Sélection du client
        List<ClientDTO> clients = clientService.listAll();
        if (clients.isEmpty()) {
            System.out.println("Aucun client disponible. Créez d'abord un client.");
            return;
        }

        System.out.println("Clients disponibles:");
        clients.forEach(c -> System.out.println(c.getId() + " - " + c.getNomComplet()));
        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        Optional<ClientDTO> clientOpt = clientService.findById(clientId);
        if (clientOpt.isEmpty()) {
            System.out.println("Client introuvable.");
            return;
        }

        // Récupérer l'entité Client pour la commande
        Optional<Client> clientEntity = clientService.findEntityById(clientId);
        if (clientEntity.isEmpty()) {
            System.out.println("Erreur lors de la récupération du client.");
            return;
        }

        // Ajout des lignes de commande
        List<LigneCommandeDTO> lignes = new ArrayList<>();
        boolean ajouterLignes = true;

        while (ajouterLignes) {
            List<ProduitDTO> produits = produitService.listAll();
            if (produits.isEmpty()) {
                System.out.println("Aucun produit disponible.");
                break;
            }

            System.out.println("\nProduits disponibles:");
            produits.forEach(p -> System.out.println(
                    p.getId() + " - " + p.getName() +
                            " (Stock: " + p.getQuantiteStock() + ", Prix: " + p.getPrice() + " FCFA)"));

            System.out.print("ID du produit (0 pour terminer): ");
            int produitId = Integer.parseInt(scanner.nextLine());

            if (produitId == 0) {
                ajouterLignes = false;
                continue;
            }

            Optional<ProduitDTO> produitOpt = produitService.findById(produitId);
            if (produitOpt.isEmpty()) {
                System.out.println("Produit introuvable.");
                continue;
            }

            ProduitDTO produit = produitOpt.get();
            System.out.print("Quantité: ");
            int quantite = Integer.parseInt(scanner.nextLine());

            if (quantite > produit.getQuantiteStock()) {
                System.out.println("Stock insuffisant!");
                continue;
            }

            lignes.add(new LigneCommandeDTO(produit, quantite, produit.getPrice()));
            System.out.println("✓ Ligne ajoutée!");
        }

        if (lignes.isEmpty()) {
            System.out.println("Commande annulée: aucune ligne.");
            return;
        }

        // Création de la commande DTO
        CommandeDTO dto = new CommandeDTO.Builder()
                .client(clientOpt.get())
                .lignes(lignes)
                .statut(StatutCommande.EN_ATTENTE.name())
                .build();

        CommandeDTO saved = commandeService.create(dto);

        System.out.println("\n✓ Commande créée avec succès!");
        afficherCommande(saved);
    }

    private void validerCommande() {
        System.out.print("ID de la commande à valider: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            commandeService.validerCommande(id);
            System.out.println("✓ Commande validée et stock déduit!");
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void annulerCommande() {
        System.out.print("ID de la commande à annuler: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            commandeService.annulerCommande(id);
            System.out.println("✓ Commande annulée!");
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void commandesParClient() {
        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        List<CommandeDTO> commandes = commandeService.findByClientId(clientId);
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande pour ce client.");
            return;
        }

        commandes.forEach(this::afficherCommande);
    }

    private void commandesParStatut() {
        System.out.println("\nStatuts disponibles:");
        for (StatutCommande statut : StatutCommande.values()) {
            System.out.println("- " + statut.name());
        }
        System.out.print("Statut: ");
        String statutStr = scanner.nextLine().toUpperCase();

        try {
            StatutCommande statut = StatutCommande.valueOf(statutStr);
            List<CommandeDTO> commandes = commandeService.findByStatut(statut);

            if (commandes.isEmpty()) {
                System.out.println("Aucune commande avec ce statut.");
                return;
            }

            commandes.forEach(this::afficherCommande);
        } catch (IllegalArgumentException e) {
            System.out.println("Statut invalide.");
        }
    }

    private void clonerCommande() {
        System.out.print("ID de la commande à cloner: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            CommandeDTO cloned = commandeService.clone(id);
            System.out.println("✓ Commande clonée:");
            afficherCommande(cloned);
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void afficherCommande(CommandeDTO commande) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Commande #" + commande.getId());
        System.out.println("Client: " + commande.getClient().getNomComplet());
        System.out.println("Email: " + commande.getClient().getEmail());
        System.out.println("Date: " + commande.getDateCommande());
        System.out.println("Statut: " + commande.getStatut());
        System.out.println("\nLignes:");
        commande.getLignes().forEach(ligne -> System.out.println("  - " + ligne.getProduit().getName() +
                " x" + ligne.getQuantite() +
                " = " + ligne.getSousTotal() + " FCFA"));
        System.out.println("\nMontant total: " + commande.getMontantTotal() + " FCFA");
        System.out.println("=".repeat(50));
    }
}