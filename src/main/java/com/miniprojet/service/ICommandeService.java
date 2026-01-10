package com.miniprojet.service;

import com.miniprojet.dto.CommandeDTO;
import com.miniprojet.exception.ProductNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour les commandes
 * Travaille avec des DTOs pour la couche de présentation
 */
public interface ICommandeService {

    List<CommandeDTO> listAll();

    CommandeDTO create(CommandeDTO dto);

    CommandeDTO clone(int id) throws ProductNotFoundException;

    CommandeDTO update(int id, CommandeDTO dto) throws ProductNotFoundException;

    void delete(int id) throws ProductNotFoundException;

    Optional<CommandeDTO> findById(int id);
}