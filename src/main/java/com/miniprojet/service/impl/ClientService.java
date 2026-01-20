package com.miniprojet.service.impl;

import com.miniprojet.dto.ClientDTO;
import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.Client;
import com.miniprojet.repository.IClientRepository;
import com.miniprojet.repository.impl.ClientRepository;
import com.miniprojet.service.IClientService;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour les clients
 * Utilise les DTOs pour la couche de présentation
 * Le repository est injecté via constructeur
 */
public class ClientService implements IClientService {

    private final IClientRepository repository;

    public ClientService(IClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ClientDTO> listAll() {
        return repository.findAll().stream()
                .map(this::toDTO).toList();
    }

    @Override
    public ClientDTO create(ClientDTO dto) {
        ClientRepository clientRepo = (ClientRepository) repository;
        Optional<Client> existing = clientRepo.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new IllegalStateException("Un client avec cet email existe déjà");
        }

        Client client = toEntity(dto);
        Client saved = repository.save(client);
        return toDTO(saved);
    }

    @Override
    public ClientDTO clone(int id) throws ProductNotFoundException {
        Client original = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Client clone = original.clone();
        clone.setEmail(clone.getEmail() + ".copy");

        Client saved = repository.save(clone);
        return toDTO(saved);
    }

    @Override
    public ClientDTO update(int id, ClientDTO dto) throws ProductNotFoundException {
        Client existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setAdresse(dto.getAdresse());

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
    public Optional<ClientDTO> findById(int id) {
        return repository.findById(id)
                .map(this::toDTO);
    }

    public Optional<ClientDTO> findByEmail(String email) {
        ClientRepository clientRepo = (ClientRepository) repository;
        return clientRepo.findByEmail(email)
                .map(this::toDTO);
    }

    /**
     * Récupère l'entité Client (pour usage interne)
     */
    public Optional<Client> findEntityById(int id) {
        return repository.findById(id);
    }

    // Mapping DTO <-> Entity
    private ClientDTO toDTO(Client client) {
        return new ClientDTO.Builder()
                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .adresse(client.getAdresse())
                .build();
    }

    private Client toEntity(ClientDTO dto) {
        return new Client.Builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .adresse(dto.getAdresse())
                .build();
    }
}

// interace pour mapper