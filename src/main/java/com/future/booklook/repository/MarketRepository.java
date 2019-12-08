package com.future.booklook.repository;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MarketRepository extends JpaRepository<Market, String> {
    Market findByUser(User user);

    Market findByProducts(Set<Product> products);
}
