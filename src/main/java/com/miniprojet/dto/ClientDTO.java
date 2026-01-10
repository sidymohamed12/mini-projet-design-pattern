package com.miniprojet.dto;

public class ClientDTO implements Cloneable {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;

    private ClientDTO(Builder builder) {
        this.id = builder.id;
        this.nom = builder.nom;
        this.prenom = builder.prenom;
        this.email = builder.email;
        this.telephone = builder.telephone;
        this.adresse = builder.adresse;
    }

    public ClientDTO() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // Builder Pattern
    public static class Builder {
        private int id;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        private String adresse;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder prenom(String prenom) {
            this.prenom = prenom;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public ClientDTO build() {
            return new ClientDTO(this);
        }
    }

    // Prototype Pattern
    @Override
    public ClientDTO clone() {
        try {
            return (ClientDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}