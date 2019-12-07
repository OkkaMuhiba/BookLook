package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Product;
import com.future.booklook.repository.ProductRepository;
import com.future.booklook.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Set<Product> findProductsByCategories(Set<Category> categories){
        return productRepository.findProductsByCategories(categories);
    }

    public Product findByProductId(String productId){
        return productRepository.findByProductId(productId);
    }

    public Product findBySKU(String sku){
        return productRepository.findBySku(sku);
    }
}
