package com.miniprojet.service;

import com.miniprojet.dto.ProduitDTO;
import com.miniprojet.exception.ProductNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour les produits
 * Travaille avec des DTOs pour la couche de présentation
 */
public interface IProduitService {

    List<ProduitDTO> listAll();

    ProduitDTO create(ProduitDTO dto);

    ProduitDTO clone(int id) throws ProductNotFoundException;

    ProduitDTO update(int id, ProduitDTO dto) throws ProductNotFoundException;

    void delete(int id) throws ProductNotFoundException;

    Optional<ProduitDTO> findById(int id);
}