package com.miniprojet.repository;

import java.util.List;

import com.miniprojet.model.Commande;
import com.miniprojet.model.StatutCommande;

public interface ICommandeRepository extends IRepository<Commande> {
    List<Commande> findByClientId(int clientId);

    List<Commande> findByStatut(StatutCommande statut);
}
