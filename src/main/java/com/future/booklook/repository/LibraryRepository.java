package com.future.booklook.repository;

import com.future.booklook.model.entity.Library;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LibraryRepository extends JpaRepository<Library, String> {
    Library findByUserAndProduct(User user, Product product);

    Boolean existsByUser(User user);

    Boolean existsByUserAndProduct(User user, Product product);

    Set<Library> findAllByUser(User user);
}
