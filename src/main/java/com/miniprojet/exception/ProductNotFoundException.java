package com.miniprojet.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(int id) {
        super("Product not found with id = " + id);
    }
}
