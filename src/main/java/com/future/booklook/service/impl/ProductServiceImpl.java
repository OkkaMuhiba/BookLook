package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Product;
import com.future.booklook.repository.ProductRepository;
import com.future.booklook.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product save(Product product){
        return productRepository.save(product);
    }
}
