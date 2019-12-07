package com.future.booklook.controller;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.Wishlist;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.ProductServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import com.future.booklook.service.impl.WishlistServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api
@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {
    @Autowired
    private WishlistServiceImpl wishlistService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoWishlist(@RequestBody String productId){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Product product = productService.findByProductId(productId);

        wishlistService.save(new Wishlist(user, product));

        return new ResponseEntity(new ApiResponse(true, "Your product has been added into wishlist"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> showAllWishlistFromUser(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Wishlist> wishlists = wishlistService.findAllByUser(user);

        return new ResponseEntity(wishlists, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductFromWishlist(@RequestBody String productId){
        User user = userService.findByUserId(getUserPrincipal().getUserId());

        if(productService.existsByProductId(productId)){
            Product product = productService.findByProductId(productId);
            if(wishlistService.existsByUserAndProduct(user, product)){
                wishlistService.deleteByUserAndProduct(user, product);
            } else {
                return new ResponseEntity(new ApiResponse(true, "Product is not available in your Wishlist"), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            return new ResponseEntity(new ApiResponse(true, "Product is not available"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(new ApiResponse(true, "Product has been removed from wishlist"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
