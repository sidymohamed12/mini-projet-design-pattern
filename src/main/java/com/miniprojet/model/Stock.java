package com.miniprojet.model;

public class Stock {

    private int quantite;
    private int seuilAlerte;

    public Stock(int quantite, int seuilAlerte) {
        this.quantite = quantite;
        this.seuilAlerte = seuilAlerte;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getSeuilAlerte() {
        return seuilAlerte;
    }

    public boolean estFaible() {
        return quantite <= seuilAlerte;
    }

    @Override
    public String toString() {
        return "Stock{quantite=" + quantite +
                ", seuilAlerte=" + seuilAlerte + '}';
    }
}
