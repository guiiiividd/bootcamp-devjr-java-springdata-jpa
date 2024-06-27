package com.abutua.product_backend.resources;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abutua.product_backend.models.Category;
import com.abutua.product_backend.models.Product;
import com.abutua.product_backend.repositories.CategoryRepository;
import com.abutua.product_backend.repositories.ProductRepository;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("products")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        product = productRepository.save(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        
        return ResponseEntity.ok(product);
    }

    @GetMapping("products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));

        productRepository.delete(product);
        
        return ResponseEntity.noContent().build();
    }

    @PutMapping("products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable int id, @RequestBody Product productUpdate) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));

        if(productUpdate.getCategory() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Can Not Be Empty");
        }

        Category category  = categoryRepository.findById(productUpdate.getCategory().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        product.setName(productUpdate.getName());
        product.setDescription(productUpdate.getDescription());
        product.setPromotion(productUpdate.isPromotion());
        product.setNewProduct(productUpdate.isNewProduct());
        product.setPrice(productUpdate.getPrice());
        product.setCategory(category);

        productRepository.save(product);
        
        return ResponseEntity.ok().build();
    }
}
