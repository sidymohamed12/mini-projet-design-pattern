package com.miniprojet.service.impl;

import com.miniprojet.dto.ProduitDTO;
import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Produit;
import com.miniprojet.model.Stock;
import com.miniprojet.repository.IProduitRepository;
import com.miniprojet.service.IProduitService;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour les produits
 * Utilise les DTOs pour la couche de présentation
 * Le repository est injecté via constructeur (Dependency Injection)
 */
public class ProduitService implements IProduitService {

    private final IProduitRepository repository;

    public ProduitService(IProduitRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProduitDTO> listAll() {
        return repository.findAll().stream()
                .map(this::toDTO).toList();
    }

    @Override
    public ProduitDTO create(ProduitDTO dto) {
        Produit produit = toEntity(dto);
        Produit saved = repository.save(produit);
        return toDTO(saved);
    }

    @Override
    public ProduitDTO clone(int id) throws ProductNotFoundException {
        Produit original = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Produit clone = original.clone();
        clone.setName(clone.getName() + " (clone)");

        Produit saved = repository.save(clone);
        return toDTO(saved);
    }

    @Override
    public ProduitDTO update(int id, ProduitDTO dto) throws ProductNotFoundException {
        Produit existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());

        // Mise à jour du stock si fourni
        if (existing.getStock() != null) {
            existing.getStock().setQuantite(dto.getQuantiteStock());
        }

        repository.update(existing);
        return toDTO(existing);
    }

    @Override
    public void delete(int id) throws ProductNotFoundException {
        boolean removed = repository.delete(id);
        if (!removed) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public Optional<ProduitDTO> findById(int id) {
        return repository.findById(id)
                .map(this::toDTO);
    }

    /**
     * Récupère l'entité Produit (pour usage interne)
     */
    public Optional<Produit> findEntityById(int id) {
        return repository.findById(id);
    }

    // Mapping DTO <-> Entity
    private ProduitDTO toDTO(Produit produit) {
        return new ProduitDTO.Builder()
                .id(produit.getId())
                .name(produit.getName())
                .description(produit.getDescription())
                .price(produit.getPrice())
                .quantiteStock(produit.getStock() != null ? produit.getStock().getQuantite() : 0)
                .seuilAlerte(produit.getStock() != null ? produit.getStock().getSeuilAlerte() : 0)
                .build();
    }

    private Produit toEntity(ProduitDTO dto) {
        Produit produit = new Produit.Builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();

        // Création du stock
        Stock stock = new Stock(dto.getQuantiteStock(), dto.getSeuilAlerte());
        produit.setStock(stock);

        return produit;
    }
}