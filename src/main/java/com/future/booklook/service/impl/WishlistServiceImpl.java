package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.Wishlist;
import com.future.booklook.repository.WishlistRepository;
import com.future.booklook.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;

    public Wishlist save(Wishlist wishlist){
        return wishlistRepository.save(wishlist);
    }

    public Set<Product> findAllProductInWishlistByUser(User user){
        Set<Wishlist> wishlists =  wishlistRepository.findAllByUser(user);
        Set<Product> products = new HashSet<>();

        for(Wishlist wishlist : wishlists){
            products.add(wishlist.getProduct());
        }

        return products;
    }

    public Boolean existsByUserAndProduct(User user, Product product){
        return wishlistRepository.existsByUserAndProduct(user, product);
    }

    public void deleteByUserAndProduct(User user, Product product){
        wishlistRepository.deleteByUserAndProduct(user, product);
    }
}
