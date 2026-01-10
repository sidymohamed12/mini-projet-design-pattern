package com.miniprojet.service.impl;

import java.util.List;
import java.util.Optional;

import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Produit;
import com.miniprojet.repository.impl.ProduitRepository;
import com.miniprojet.service.IProduitService;

public class ProduitService implements IProduitService {
    private final ProduitRepository repo = ProduitRepository.getInstance();

    public ProduitService() {

    }

    @Override
    public List<Produit> listAll() {
        return repo.findAll();
    }

    @Override
    public Produit create(Produit product) {
        return repo.save(product);
    }

    @Override
    public Produit clone(int id) throws ProductNotFoundException {
        Produit original = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Produit clone = original.clone(); // Prototype pattern
        // modify cloned name to show it's a clone
        clone.setName(clone.getName() + " (clone)");
        return repo.save(clone);
    }

    @Override
    public Produit update(int id, Produit updated) throws ProductNotFoundException {
        Produit existing = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        repo.update(existing);
        return existing;
    }

    @Override
    public void delete(int id) throws ProductNotFoundException {
        boolean removed = repo.delete(id);
        if (!removed)
            throw new ProductNotFoundException(id);
    }

    @Override
    public Optional<Produit> findById(int id) {
        return repo.findById(id);
    }
}
