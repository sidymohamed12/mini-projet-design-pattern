package com.miniprojet.model;

public class LigneCommande {
    private Produit produit;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.getPrice();
    }

    public Produit getProduit() {
        return produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public double getSousTotal() {
        return prixUnitaire * quantite;
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "produit=" + produit.getName() +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", sousTotal=" + getSousTotal() +
                '}';
    }
}