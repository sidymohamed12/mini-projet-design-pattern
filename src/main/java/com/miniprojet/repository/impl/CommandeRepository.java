package com.miniprojet.repository.impl;

import com.miniprojet.model.Commande;
import com.miniprojet.model.StatutCommande;
import com.miniprojet.repository.ICommandeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Singleton Pattern - Repository pour les commandes
 */
public class CommandeRepository implements ICommandeRepository {

    private static final CommandeRepository INSTANCE = new CommandeRepository();
    private final List<Commande> listCommandes = new ArrayList<>();

    public static CommandeRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Commande> findAll() {
        return listCommandes;
    }

    @Override
    public Optional<Commande> findById(Integer id) {
        return listCommandes.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    @Override
    public Commande save(Commande commande) {
        int id = listCommandes.isEmpty() ? 1
                : listCommandes.get(listCommandes.size() - 1).getId() + 1;
        commande.setId(id);
        listCommandes.add(commande);
        return commande;
    }

    @Override
    public void update(Commande commande) {
        List<Commande> commandes = listCommandes;
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
        return listCommandes.removeIf(c -> c.getId() == id);
    }

    @Override
    public List<Commande> findByClientId(int clientId) {
        return listCommandes.stream()
                .filter(c -> c.getClient().getId() == clientId)
                .toList();
    }

    @Override
    public List<Commande> findByStatut(StatutCommande statut) {
        return listCommandes.stream()
                .filter(c -> c.getStatut() == statut)
                .toList();
    }
}
