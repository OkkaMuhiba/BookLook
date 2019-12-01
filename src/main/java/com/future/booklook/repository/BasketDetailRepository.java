package com.future.booklook.repository;

import com.future.booklook.model.entity.BasketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketDetailRepository extends JpaRepository<BasketDetail, String> {
}
