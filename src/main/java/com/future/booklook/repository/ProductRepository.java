package com.future.booklook.repository;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.TransactionDetail;
import com.future.booklook.model.entity.properties.ProductConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Set<Product> findProductsByCategoriesAndProductConfirm(Set<Category> categories, ProductConfirm productConfirm);

    Product findByProductId(String productId);

    Boolean existsByProductId(String productId);

    Set<Product> findAllByMarketAndProductConfirm(Market market, ProductConfirm productConfirm);

    Product findByProductFileContaining(String fileName);

    Product findByTransactionDetails(Set<TransactionDetail> transactionDetails);

    Set<Product> findAllByProductConfirm(ProductConfirm productConfirm);
}
