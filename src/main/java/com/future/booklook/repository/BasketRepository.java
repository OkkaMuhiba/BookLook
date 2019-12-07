package com.future.booklook.repository;

import com.future.booklook.model.entity.Basket;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, String> {
    Boolean existsByUser(User user);

    Basket findByUser(User user);
}
