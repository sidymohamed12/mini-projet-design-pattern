package com.miniprojet.model;

import com.miniprojet.prototype.IPrototype;

public class Client implements IPrototype {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;

    private Client(Builder builder) {
        this.id = builder.id;
        this.nom = builder.nom;
        this.prenom = builder.prenom;
        this.email = builder.email;
        this.telephone = builder.telephone;
        this.adresse = builder.adresse;
    }

    public Client() {
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

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
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

        public Client build() {
            if (nom == null || nom.trim().isEmpty()) {
                throw new IllegalStateException("Le nom du client ne peut pas être null ou vide");
            }
            if (prenom == null || prenom.trim().isEmpty()) {
                throw new IllegalStateException("Le prénom du client ne peut pas être null ou vide");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalStateException("L'email du client ne peut pas être null ou vide");
            }
            return new Client(this);
        }
    }

    // Prototype Pattern
    @Override
    public Client clone() {
        try {
            Client copy = (Client) super.clone();
            copy.id = 0;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}

// creer une interface pour le prototype
// interface ClientProtoype extends Cloneable => T extends Cloneable;

// avoir l'habitude de ne pas dependre des dependances externes, creer une
// couche intermediaire qui se charge de ca pour un couplage faible