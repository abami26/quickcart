package com.niit.quickcart.controller;

import com.niit.quickcart.data.ProductCatalog;
import com.niit.quickcart.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return ProductCatalog.PRODUCTS;
    }
}
