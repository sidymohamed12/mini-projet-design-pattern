package com.miniprojet.repository.impl;

import com.miniprojet.model.Client;
import com.miniprojet.repository.IClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Singleton Pattern - Repository pour les clients
 */
public class ClientRepository implements IClientRepository {

    private static final ClientRepository INSTANCE = new ClientRepository();

    private final List<Client> listClients = new ArrayList<>();

    public static ClientRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Client> findAll() {
        return listClients;
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return listClients.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    @Override
    public Client save(Client client) {
        int id = listClients.isEmpty() ? 1
                : listClients.get(listClients.size() - 1).getId() + 1;
        client.setId(id);
        listClients.add(client);
        return client;
    }

    @Override
    public void update(Client client) {
        List<Client> clients = listClients;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == client.getId()) {
                clients.set(i, client);
                return;
            }
        }
        clients.add(client);
    }

    @Override
    public boolean delete(Integer id) {
        return listClients.removeIf(c -> c.getId() == id);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return listClients.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}