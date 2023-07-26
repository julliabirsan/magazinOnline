package com.example.webShop.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;

@Component
@SessionScope
public class UserSession {
    private int id;
    private HashMap<Integer, Integer> cart = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, Integer> getCart() {
        return cart;
    }

    public void setCart(HashMap<Integer, Integer> cart) {
        this.cart = cart;
    }
    public void addToCart(int idProdus){
        if (cart.containsKey(idProdus)){
            int cantitateNoua = cart.get(idProdus);
            cantitateNoua+=1;
            cart.put(idProdus, cantitateNoua);
        } else {
            cart.put(idProdus,1);
        }
    }

    public int getCartSize(){
        int size =0;
        for (Integer i:cart.values()){
            size+=i;
        }
        return size;
    }
}
