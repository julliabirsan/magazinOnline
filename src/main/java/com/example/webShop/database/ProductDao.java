package com.example.webShop.database;

import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Integer> {
}
