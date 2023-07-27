package com.example.webShop.Controller;

import com.example.webShop.Service.ProductService;
import com.example.webShop.database.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    ProductService productService;

    @GetMapping("/admin/products")
    public ModelAndView getPRoducts(){
        ModelAndView modelAndView = new ModelAndView("/admin/products.html");
        List<Product> productList = productService.findAllProducts();
        modelAndView.addObject("products", productList);
        return modelAndView;
    }

    @PostMapping("/admin/products")
    @ResponseBody
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("category") String category,
                             @RequestParam("price") Double price,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("imgSrc") String imgSrc
                             ){
      return productService.addProduct(name, category, price, quantity, imgSrc);
    }

    @DeleteMapping("/admin/products/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable("id") int id){
        return productService.deleteProduct(id);
    }
}
