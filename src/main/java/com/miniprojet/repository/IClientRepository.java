package com.miniprojet.repository;

import java.util.Optional;

import com.miniprojet.model.Client;

public interface IClientRepository extends IRepository<Client> {
    Optional<Client> findByEmail(String email);
}
