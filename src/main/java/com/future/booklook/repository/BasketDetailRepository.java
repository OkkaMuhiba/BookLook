package com.future.booklook.repository;

import com.future.booklook.model.entity.Basket;
import com.future.booklook.model.entity.BasketDetail;
import com.future.booklook.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface BasketDetailRepository extends JpaRepository<BasketDetail, String> {
    Boolean existsByBasketAndProduct(Basket basket, Product product);

    Set<BasketDetail> findAllByBasket(Basket basket);

    @Transactional
    void deleteByBasketAndProduct(Basket basket, Product product);
}
