package com.example.webShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WebShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}

}
