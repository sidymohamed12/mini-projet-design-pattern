package com.miniprojet.view;

import com.miniprojet.dto.ClientDTO;
import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.service.impl.ClientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientView {

    private final ClientService clientService;
    private final Scanner scanner;

    public ClientView(ClientService clientService, Scanner scanner) {
        this.clientService = clientService;
        this.scanner = scanner;
    }

    public void displayMenu() {
        System.out.println("\n=== GESTION CLIENTS ===");
        System.out.println("1) Lister les clients");
        System.out.println("2) Créer un client");
        System.out.println("3) Cloner un client");
        System.out.println("4) Mettre à jour un client");
        System.out.println("5) Supprimer un client");
        System.out.println("0) Retour");
        System.out.print("Choix: ");
    }

    public void handleChoice(String choice) {
        try {
            switch (choice) {
                case "1" -> listerClients();
                case "2" -> creerClient();
                case "3" -> clonerClient();
                case "4" -> mettreAJourClient();
                case "5" -> supprimerClient();
                case "0" -> {
                }
                default -> System.out.println("Choix invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void listerClients() {
        List<ClientDTO> clients = clientService.listAll();
        if (clients.isEmpty()) {
            System.out.println("Aucun client.");
            return;
        }
        clients.forEach(dto -> System.out.println(
                "ID: " + dto.getId() +
                        " | " + dto.getNomComplet() +
                        " | " + dto.getEmail() +
                        " | " + dto.getTelephone()));
    }

    private void creerClient() {
        System.out.println("\n=== Création client ===");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();

        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();

        System.out.print("Adresse: ");
        String adresse = scanner.nextLine();

        ClientDTO dto = new ClientDTO.Builder()
                .nom(nom)
                .prenom(prenom)
                .email(email)
                .telephone(telephone)
                .adresse(adresse)
                .build();

        ClientDTO saved = clientService.create(dto);
        System.out.println("✓ Client créé avec ID: " + saved.getId());
    }

    private void clonerClient() {
        System.out.print("ID du client à cloner: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            ClientDTO cloned = clientService.clone(id);
            System.out.println("✓ Client cloné avec ID: " + cloned.getId());
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mettreAJourClient() {
        System.out.print("ID du client à mettre à jour: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<ClientDTO> opt = clientService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Client introuvable.");
            return;
        }

        ClientDTO existing = opt.get();
        System.out.println("Client actuel: " + existing.getNomComplet());

        System.out.print("Nouveau nom (vide = conserver): ");
        String nom = scanner.nextLine();
        System.out.print("Nouveau prénom (vide = conserver): ");
        String prenom = scanner.nextLine();
        System.out.print("Nouvel email (vide = conserver): ");
        String email = scanner.nextLine();
        System.out.print("Nouveau téléphone (vide = conserver): ");
        String telephone = scanner.nextLine();
        System.out.print("Nouvelle adresse (vide = conserver): ");
        String adresse = scanner.nextLine();

        ClientDTO updated = new ClientDTO.Builder()
                .id(existing.getId())
                .nom(nom.isBlank() ? existing.getNom() : nom)
                .prenom(prenom.isBlank() ? existing.getPrenom() : prenom)
                .email(email.isBlank() ? existing.getEmail() : email)
                .telephone(telephone.isBlank() ? existing.getTelephone() : telephone)
                .adresse(adresse.isBlank() ? existing.getAdresse() : adresse)
                .build();

        try {
            ClientDTO result = clientService.update(id, updated);
            System.out.println("✓ Client mis à jour: " + result.getNomComplet());
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void supprimerClient() {
        System.out.print("ID du client à supprimer: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            clientService.delete(id);
            System.out.println("✓ Client supprimé.");
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}