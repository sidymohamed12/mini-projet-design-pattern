package com.miniprojet.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.miniprojet.database.Database;
import com.miniprojet.model.Produit;
import com.miniprojet.repository.IRepository;

/**
 * Singleton Pattern - Repository pour les produits
 * Utilise maintenant la Database singleton
 */
public class ProduitRepository implements IRepository<Produit> {

    private static final ProduitRepository INSTANCE = new ProduitRepository();
    private final Database database;

    private ProduitRepository() {
        this.database = Database.getInstance();
    }

    public static ProduitRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Produit> findAll() {
        return new ArrayList<>(database.getProduits());
    }

    @Override
    public Optional<Produit> findById(Integer id) {
        return database.getProduits().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public Produit save(Produit produit) {
        int id = database.getProduits().isEmpty() ? 1
                : database.getProduits().get(database.getProduits().size() - 1).getId() + 1;
        produit.setId(id);
        database.getProduits().add(produit);
        return produit;
    }

    @Override
    public void update(Produit produit) {
        List<Produit> produits = database.getProduits();
        for (int i = 0; i < produits.size(); i++) {
            if (produits.get(i).getId() == produit.getId()) {
                produits.set(i, produit);
                return;
            }
        }
        produits.add(produit);
    }

    @Override
    public boolean delete(Integer id) {
        return database.getProduits().removeIf(p -> p.getId() == id);
    }
}