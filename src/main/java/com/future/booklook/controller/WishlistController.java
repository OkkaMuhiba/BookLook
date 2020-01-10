package com.future.booklook.controller;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.Wishlist;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.response.ProductInfoResponse;
import com.future.booklook.payload.request.WishlistRequest;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.*;
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
    private MarketServiceImpl marketService;

    @Autowired
    private LibraryServiceImpl libraryService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoWishlist(@RequestBody WishlistRequest wishlistRequest){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        if(productService.existsByProductId(wishlistRequest.getProductId())){
            Product product = productService.findByProductId(wishlistRequest.getProductId());

            if(libraryService.existByUserAndProduct(user, product)){
                return new ResponseEntity<>(new ApiResponse(false, "Product already exist in Library"), HttpStatus.BAD_REQUEST);
            }

            if(wishlistService.existsByUserAndProduct(user, product)){
                return new ResponseEntity<>(new ApiResponse(false, "Product already exist in Wishlist"), HttpStatus.BAD_REQUEST);
            } else {
                wishlistService.save(new Wishlist(user, product));
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse(true, "Your product has been added into wishlist"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> showAllWishlistFromUser(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Product> products = wishlistService.findAllProductInWishlistByUser(user);
        Set<ProductInfoResponse> productInfoResponse = new HashSet<>();
        for(Product product : products){
            Market market = marketService.findMarketByProduct(product);
            productInfoResponse.add(new ProductInfoResponse(product, market.getMarketId(), market.getMarketName()));
        }

        return new ResponseEntity<>(productInfoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductFromWishlist(@RequestBody WishlistRequest wishlistRequest){
        User user = userService.findByUserId(getUserPrincipal().getUserId());

        if(productService.existsByProductId(wishlistRequest.getProductId())){
            Product product = productService.findByProductId(wishlistRequest.getProductId());
            if(wishlistService.existsByUserAndProduct(user, product)){
                wishlistService.deleteByUserAndProduct(user, product);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Product is not available in your Wishlist"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Product is not available"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse(true, "Product has been removed from wishlist"), HttpStatus.OK);
    }

    private UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
}
