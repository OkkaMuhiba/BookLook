package com.future.booklook.repository;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, String> {
    Market findByUser(User user);
}
