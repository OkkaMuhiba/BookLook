package com.future.booklook.repository;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MarketRepository extends JpaRepository<Market, String> {
    Market findByUser(User user);

    Market findByProducts(Set<Product> products);

    Market findByMarketId(String marketId);

    Boolean existsByMarketId(String marketId);

    Boolean existsByMarketName(String marketName);

    Boolean existsByMarketCode(String marketCode);

    Boolean existsByUser(User user);
}
