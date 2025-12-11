package com.miniprojet.service;

import java.util.List;
import java.util.Optional;

import com.miniprojet.exception.ProductNotFoundException;

public interface IService<T> {
    List<T> listAll();

    T create(T entity);

    T clone(int id) throws ProductNotFoundException;

    T update(int id, T updated) throws ProductNotFoundException;

    void delete(int id) throws ProductNotFoundException;

    Optional<T> findById(int id);
}
