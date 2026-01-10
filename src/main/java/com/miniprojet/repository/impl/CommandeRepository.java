package com.miniprojet.repository.impl;

import com.miniprojet.database.Database;
import com.miniprojet.model.Commande;
import com.miniprojet.model.StatutCommande;
import com.miniprojet.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Singleton Pattern - Repository pour les commandes
 */
public class CommandeRepository implements IRepository<Commande> {

    private static final CommandeRepository INSTANCE = new CommandeRepository();
    private final Database database;

    private CommandeRepository() {
        this.database = Database.getInstance();
    }

    public static CommandeRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Commande> findAll() {
        return new ArrayList<>(database.getCommandes());
    }

    @Override
    public Optional<Commande> findById(Integer id) {
        return database.getCommandes().stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    @Override
    public Commande save(Commande commande) {
        int id = database.getCommandes().isEmpty() ? 1 
                : database.getCommandes().get(database.getCommandes().size() - 1).getId() + 1;
        commande.setId(id);
        database.getCommandes().add(commande);
        return commande;
    }

    @Override
    public void update(Commande commande) {
        List<Commande> commandes = database.getCommandes();
        for (int i = 0; i < commandes.size(); i++) {
            if (commandes.get(i).getId() == commande.getId()) {
                commandes.set(i, commande);
                return;
            }
        }
        commandes.add(commande);
    }

    @Override
    public boolean delete(Integer id) {
        return database.getCommandes().removeIf(c -> c.getId() == id);
    }

    public List<Commande> findByClientId(int clientId) {
        return database.getCommandes().stream()
                .filter(c -> c.getClient().getId() == clientId)
                .collect(Collectors.toList());
    }

    public List<Commande> findByStatut(StatutCommande statut) {
        return database.getCommandes().stream()
                .filter(c -> c.getStatut() == statut)
                .collect(Collectors.toList());
    }
}
