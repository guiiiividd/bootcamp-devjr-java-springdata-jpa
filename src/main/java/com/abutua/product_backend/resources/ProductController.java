package com.abutua.product_backend.resources;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abutua.product_backend.models.Product;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ProductController {

    private List<Product> products = Arrays.asList( 
        new Product(1, "Nome do Produto 01", 100, "Descrição 01", 1, false, false), 
        new Product(2, "Nome do Produto 02", 200, "Descrição 02", 2, true, true), 
        new Product(3, "Nome do Produto 03", 300, "Descrição 03", 3, false, true));

    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = products.stream().filter(p -> p.getId() == id).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        
        return ResponseEntity.ok(product);
    }

    @GetMapping("products")
    public List<Product> getProducts() {
        return products;
    }
}
