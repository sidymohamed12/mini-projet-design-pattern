package com.miniprojet.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeDTO implements Cloneable {
    private int id;
    private ClientDTO client;
    private List<LigneCommandeDTO> lignes;
    private LocalDateTime dateCommande;
    private String statut;
    private double montantTotal;

    private CommandeDTO(Builder builder) {
        this.id = builder.id;
        this.client = builder.client;
        this.lignes = builder.lignes;
        this.dateCommande = builder.dateCommande;
        this.statut = builder.statut;
        this.montantTotal = builder.montantTotal;
    }

    public CommandeDTO() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public ClientDTO getClient() {
        return client;
    }

    public List<LigneCommandeDTO> getLignes() {
        return lignes;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    // Builder Pattern
    public static class Builder {
        private int id;
        private ClientDTO client;
        private List<LigneCommandeDTO> lignes = new ArrayList<>();
        private LocalDateTime dateCommande;
        private String statut;
        private double montantTotal;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder client(ClientDTO client) {
            this.client = client;
            return this;
        }

        public Builder lignes(List<LigneCommandeDTO> lignes) {
            this.lignes = lignes;
            return this;
        }

        public Builder ajouterLigne(LigneCommandeDTO ligne) {
            this.lignes.add(ligne);
            return this;
        }

        public Builder dateCommande(LocalDateTime dateCommande) {
            this.dateCommande = dateCommande;
            return this;
        }

        public Builder statut(String statut) {
            this.statut = statut;
            return this;
        }

        public Builder montantTotal(double montantTotal) {
            this.montantTotal = montantTotal;
            return this;
        }

        public CommandeDTO build() {
            return new CommandeDTO(this);
        }
    }

    // Prototype Pattern
    @Override
    public CommandeDTO clone() {
        try {
            CommandeDTO copy = (CommandeDTO) super.clone();
            copy.lignes = new ArrayList<>(this.lignes);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}