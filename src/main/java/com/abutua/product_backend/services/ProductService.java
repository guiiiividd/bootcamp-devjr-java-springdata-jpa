package com.abutua.product_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.abutua.product_backend.models.Category;
import com.abutua.product_backend.models.Product;
import com.abutua.product_backend.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public Product getById(int id){
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public void deleteById(int id){
        Product product = getById(id);
        productRepository.delete(product);
    }

    public void update(int id, Product productUpdate){
        Product product = getById(id);

        if(productUpdate.getCategory() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Can Not Be Empty");
        }

        Category category  = categoryService.getById(productUpdate.getCategory().getId());

        product.setName(productUpdate.getName());
        product.setDescription(productUpdate.getDescription());
        product.setPromotion(productUpdate.isPromotion());
        product.setNewProduct(productUpdate.isNewProduct());
        product.setPrice(productUpdate.getPrice());
        product.setCategory(category);

        productRepository.save(product);
    }
}
