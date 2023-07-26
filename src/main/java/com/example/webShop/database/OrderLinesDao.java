package com.example.webShop.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderLinesDao extends CrudRepository<OrderLines, Integer> {
}
