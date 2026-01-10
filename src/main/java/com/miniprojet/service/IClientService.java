package com.miniprojet.service;

import com.miniprojet.dto.ClientDTO;
import com.miniprojet.exception.ProductNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour les clients
 * Travaille avec des DTOs pour la couche de présentation
 */
public interface IClientService {

    List<ClientDTO> listAll();

    ClientDTO create(ClientDTO dto);

    ClientDTO clone(int id) throws ProductNotFoundException;

    ClientDTO update(int id, ClientDTO dto) throws ProductNotFoundException;

    void delete(int id) throws ProductNotFoundException;

    Optional<ClientDTO> findById(int id);
}