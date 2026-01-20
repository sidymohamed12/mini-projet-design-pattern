package com.miniprojet.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.miniprojet.model.Produit;
import com.miniprojet.repository.IProduitRepository;

/**
 * Singleton Pattern - Repository pour les produits
 * Utilise maintenant la Database singleton
 */
public class ProduitRepository implements IProduitRepository {

    private static final ProduitRepository INSTANCE = new ProduitRepository();
    private final List<Produit> listProduits = new ArrayList<>();

    public static ProduitRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Produit> findAll() {
        return listProduits;
    }

    @Override
    public Optional<Produit> findById(Integer id) {
        return listProduits.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public Produit save(Produit produit) {
        int id = listProduits.isEmpty() ? 1
                : listProduits.get(listProduits.size() - 1).getId() + 1;
        produit.setId(id);
        listProduits.add(produit);
        return produit;
    }

    @Override
    public void update(Produit produit) {
        List<Produit> produits = listProduits;
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
        return listProduits.removeIf(p -> p.getId() == id);
    }
}