package com.example.webShop.Service;

import com.example.webShop.database.Order;
import com.example.webShop.database.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    public List<Order> getOrdersByUserId(int id){
        return orderDao.findAllByUserId(id);
    }
}
