package com.miniprojet.dto;

public class ProduitDTO implements Cloneable {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantiteStock;
    private int seuilAlerte;

    private ProduitDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.quantiteStock = builder.quantiteStock;
        this.seuilAlerte = builder.seuilAlerte;
    }

    public ProduitDTO() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public int getSeuilAlerte() {
        return seuilAlerte;
    }

    public boolean isStockFaible() {
        return quantiteStock <= seuilAlerte;
    }

    // Builder Pattern
    public static class Builder {
        private int id;
        private String name;
        private String description;
        private double price;
        private int quantiteStock;
        private int seuilAlerte;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder quantiteStock(int quantiteStock) {
            this.quantiteStock = quantiteStock;
            return this;
        }

        public Builder seuilAlerte(int seuilAlerte) {
            this.seuilAlerte = seuilAlerte;
            return this;
        }

        public ProduitDTO build() {
            return new ProduitDTO(this);
        }
    }

    // Prototype Pattern
    @Override
    public ProduitDTO clone() {
        try {
            return (ProduitDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}