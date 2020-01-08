package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.properties.ProductConfirm;
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
        return productRepository.findProductsByCategoriesAndProductConfirm(categories, ProductConfirm.CONFIRMED);
    }

    public Product findByProductId(String productId){
        return productRepository.findByProductId(productId);
    }

    public Boolean existsByProductId(String productId){
        return productRepository.existsByProductId(productId);
    }

    public Set<Product> findAllByMarket(Market market){
        return productRepository.findAllByMarket(market);
    }

    public Set<Product> findAllByMarketAndConfirmed(Market market){
        return productRepository.findAllByMarketAndProductConfirm(market, ProductConfirm.CONFIRMED);
    }

    public Boolean productExistByFilename(String filename){
        return productRepository.existsByProductFileContaining(filename);
    }

    public Product findProductByProductFilename(String filename){
        return productRepository.findByProductFileContaining(filename);
    }

    public Set<Product> findProductWithUnconfirmedStatus(){
        return productRepository.findAllByProductConfirm(ProductConfirm.UNCONFIRMED);
    }

    public Long getAllConfirmedBookInNumber(){
        return productRepository.countByProductConfirm(ProductConfirm.CONFIRMED);
    }

    public Long getAllUnconfirmedBookInNumber(){
        return productRepository.countByProductConfirm(ProductConfirm.UNCONFIRMED);
    }
}
