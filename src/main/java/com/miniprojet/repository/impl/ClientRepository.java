package com.miniprojet.repository.impl;

import com.miniprojet.database.Database;
import com.miniprojet.model.Client;
import com.miniprojet.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Singleton Pattern - Repository pour les clients
 */
public class ClientRepository implements IRepository<Client> {

    private static final ClientRepository INSTANCE = new ClientRepository();
    private final Database database;

    private ClientRepository() {
        this.database = Database.getInstance();
    }

    public static ClientRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(database.getClients());
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return database.getClients().stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    @Override
    public Client save(Client client) {
        int id = database.getClients().isEmpty() ? 1
                : database.getClients().get(database.getClients().size() - 1).getId() + 1;
        client.setId(id);
        database.getClients().add(client);
        return client;
    }

    @Override
    public void update(Client client) {
        List<Client> clients = database.getClients();
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
        return database.getClients().removeIf(c -> c.getId() == id);
    }

    public Optional<Client> findByEmail(String email) {
        return database.getClients().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}