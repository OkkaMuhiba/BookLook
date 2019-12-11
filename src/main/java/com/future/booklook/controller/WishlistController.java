package com.future.booklook.controller;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.Wishlist;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.ProductInfoResponse;
import com.future.booklook.payload.WishlistRequest;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.MarketServiceImpl;
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

import java.util.HashSet;
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

    @Autowired
    MarketServiceImpl marketService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoWishlist(@RequestBody WishlistRequest wishlistRequest){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        if(productService.existsByProductId(wishlistRequest.getProductId())){
            Product product = productService.findByProductId(wishlistRequest.getProductId());
            if(wishlistService.existsByUserAndProduct(user, product)){
                return new ResponseEntity(new ApiResponse(false, "Product already exist in Wishlist"), HttpStatus.UNPROCESSABLE_ENTITY);
            } else {
                wishlistService.save(new Wishlist(user, product));
            }
        } else {
            return new ResponseEntity(new ApiResponse(false, "Product does not exist"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(new ApiResponse(true, "Your product has been added into wishlist"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> showAllWishlistFromUser(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Product> products = wishlistService.findAllProductInWishlistByUser(user);
        Set<ProductInfoResponse> productInfoRespons = new HashSet<>();
        for(Product product : products){
            Market market = marketService.findMarketByProduct(product);
            productInfoRespons.add(new ProductInfoResponse(product, market.getMarketId(), market.getMarketName()));
        }

        return new ResponseEntity(productInfoRespons, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductFromWishlist(@RequestBody WishlistRequest wishlistRequest){
        User user = userService.findByUserId(getUserPrincipal().getUserId());

        if(productService.existsByProductId(wishlistRequest.getProductId())){
            Product product = productService.findByProductId(wishlistRequest.getProductId());
            if(wishlistService.existsByUserAndProduct(user, product)){
                wishlistService.deleteByUserAndProduct(user, product);
            } else {
                return new ResponseEntity(new ApiResponse(false, "Product is not available in your Wishlist"), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            return new ResponseEntity(new ApiResponse(false, "Product is not available"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(new ApiResponse(true, "Product has been removed from wishlist"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
