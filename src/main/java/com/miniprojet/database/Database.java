package com.miniprojet.database;

import com.miniprojet.model.Client;
import com.miniprojet.model.Commande;
import com.miniprojet.model.Produit;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Pattern - Base de données en mémoire
 * Garantit une seule instance de la base de données dans toute l'application
 */
public class Database {

    private static Database instance;
    private static final Object lock = new Object();

    private final List<Produit> produits;
    private final List<Client> clients;
    private final List<Commande> commandes;

    // Constructeur privé pour empêcher l'instanciation directe
    private Database() {
        this.produits = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.commandes = new ArrayList<>();
        System.out.println("Database initialisée (Singleton)");
    }

    /**
     * Double-Checked Locking pour thread-safety
     */
    public static Database getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    // Getters pour accéder aux collections
    public List<Produit> getProduits() {
        return produits;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    // Méthodes utilitaires
    public void clear() {
        produits.clear();
        clients.clear();
        commandes.clear();
    }
}
