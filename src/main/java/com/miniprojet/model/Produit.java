package com.miniprojet.model;

public class Produit implements Cloneable {
    private int id;
    private String name;
    private String description;
    private double price;
    private Stock stock; // 👈 association

    private Produit(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.description = b.description;
        this.price = b.price;
    }

    public Produit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    public static class Builder {
        private int id;
        private String name;
        private String description;
        private double price;
        private Stock stock;

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

        public Builder stock(Stock stock) {
            this.stock = stock;
            return this;
        }

        public Produit build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException(
                        "le nom du produit ne peut pas être null ou vide, veuillez renseigner le nom du Produit ");
            }
            return new Produit(this);
        }
    }

    @Override
    public Produit clone() {
        try {
            Produit copy = (Produit) super.clone();
            copy.id = 0; // reset id so repo gives a new id
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}
