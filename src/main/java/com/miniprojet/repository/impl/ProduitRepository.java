package com.miniprojet.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.miniprojet.model.Produit;
import com.miniprojet.repository.IRepository;

public class ProduitRepository implements IRepository<Produit> {

    private final List<Produit> produits = new ArrayList<>();
    private static final ProduitRepository INSTANCE = new ProduitRepository();

    public static ProduitRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Produit> findAll() {
        return new ArrayList<>(produits);
    }

    @Override
    public Optional<Produit> findById(Integer id) {
        return produits.stream().filter(p -> p.getId() == id).findFirst();

    }

    @Override
    public Produit save(Produit produit) {
        int id = produits.isEmpty() ? 1 : produits.get(produits.size() - 1).getId() + 1;
        produit.setId(id);
        produits.add(produit);
        return produit;
    }

    @Override
    public void update(Produit produit) {
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
        return produits.removeIf(p -> p.getId() == id);
    }
}