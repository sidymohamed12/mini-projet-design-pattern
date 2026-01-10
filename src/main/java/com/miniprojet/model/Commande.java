package com.miniprojet.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Commande implements Cloneable {
    private int id;
    private Client client;
    private List<LigneCommande> lignes;
    private LocalDateTime dateCommande;
    private StatutCommande statut;
    private double montantTotal;

    private Commande(Builder builder) {
        this.id = builder.id;
        this.client = builder.client;
        this.lignes = builder.lignes;
        this.dateCommande = builder.dateCommande;
        this.statut = builder.statut;
        this.montantTotal = builder.montantTotal;
    }

    public Commande() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void calculerMontantTotal() {
        this.montantTotal = lignes.stream()
                .mapToDouble(LigneCommande::getSousTotal)
                .sum();
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", client=" + (client != null ? client.getNom() + " " + client.getPrenom() : "null") +
                ", nombreLignes=" + (lignes != null ? lignes.size() : 0) +
                ", dateCommande=" + dateCommande +
                ", statut=" + statut +
                ", montantTotal=" + montantTotal +
                '}';
    }

    // Builder Pattern
    public static class Builder {
        private int id;
        private Client client;
        private List<LigneCommande> lignes = new ArrayList<>();
        private LocalDateTime dateCommande = LocalDateTime.now();
        private StatutCommande statut = StatutCommande.EN_ATTENTE;
        private double montantTotal;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder lignes(List<LigneCommande> lignes) {
            this.lignes = lignes;
            return this;
        }

        public Builder ajouterLigne(LigneCommande ligne) {
            this.lignes.add(ligne);
            return this;
        }

        public Builder dateCommande(LocalDateTime dateCommande) {
            this.dateCommande = dateCommande;
            return this;
        }

        public Builder statut(StatutCommande statut) {
            this.statut = statut;
            return this;
        }

        public Builder montantTotal(double montantTotal) {
            this.montantTotal = montantTotal;
            return this;
        }

        public Commande build() {
            if (client == null) {
                throw new IllegalStateException("Une commande doit avoir un client");
            }
            if (lignes.isEmpty()) {
                throw new IllegalStateException("Une commande doit avoir au moins une ligne");
            }
            Commande commande = new Commande(this);
            commande.calculerMontantTotal();
            return commande;
        }
    }

    // Prototype Pattern
    @Override
    public Commande clone() {
        try {
            Commande copy = (Commande) super.clone();
            copy.id = 0;
            copy.lignes = new ArrayList<>(this.lignes);
            copy.dateCommande = LocalDateTime.now();
            copy.statut = StatutCommande.EN_ATTENTE;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}