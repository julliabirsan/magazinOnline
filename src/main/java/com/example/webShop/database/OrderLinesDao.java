package com.example.webShop.database;

import org.springframework.data.repository.CrudRepository;

public interface OrderLinesDao extends CrudRepository<OrderLines, Integer> {
}
