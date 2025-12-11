package com.miniprojet.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {

    List<T> findAll();

    Optional<T> findById(Integer id);

    T save(T entity);

    void update(T entity);

    boolean delete(Integer id);
}
