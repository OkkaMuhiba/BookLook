package com.future.booklook.repository;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Set<Product> findProductsByCategories(Set<Category> categories);

    Product findByProductId(String productId);

    Product findBySku(String sku);

    Boolean existsByProductId(String productId);
}
