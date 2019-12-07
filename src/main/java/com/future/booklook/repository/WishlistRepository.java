package com.future.booklook.repository;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    Set<Wishlist> findAllByUser(User user);

    Boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);
}
