package com.example.webShop.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDao extends CrudRepository<Order, Integer> {
    List<Order> findAllByUserId(int id);
}
