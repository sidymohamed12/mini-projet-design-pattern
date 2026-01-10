package com.miniprojet.model;

public enum StatutCommande {
    EN_ATTENTE("En attente"),
    VALIDEE("Validée"),
    EN_PREPARATION("En préparation"),
    EXPEDIEE("Expédiée"),
    LIVREE("Livrée"),
    ANNULEE("Annulée");

    private final String libelle;

    StatutCommande(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}