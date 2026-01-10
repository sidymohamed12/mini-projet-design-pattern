package com.miniprojet.dto;

public class LigneCommandeDTO {
    private ProduitDTO produit;
    private int quantite;
    private double prixUnitaire;

    public LigneCommandeDTO(ProduitDTO produit, int quantite, double prixUnitaire) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public ProduitDTO getProduit() {
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
}