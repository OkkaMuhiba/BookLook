package com.future.booklook.controller;

import com.future.booklook.model.entity.*;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.request.BucketRequest;
import com.future.booklook.payload.response.ProductInfoResponse;
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
@RequestMapping("/api/buckets")
public class BucketController {
    @Autowired
    BasketServiceImpl basketService;

    @Autowired
    BasketDetailServiceImpl basketDetailService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    MarketServiceImpl marketService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoBucket(@RequestBody BucketRequest bucketRequest){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        if(!(productService.existsByProductId(bucketRequest.getProductId()))){
            return new ResponseEntity(new ApiResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
        }

        Product product = productService.findByProductId(bucketRequest.getProductId());
        if(basketService.existsByUser(user)){
            Basket basket = basketService.findByUser(user);
            if(basketDetailService.existsByBasketAndProduct(basket, product)){
                return new ResponseEntity(new ApiResponse(false, "Product already exists in Basket"), HttpStatus.BAD_REQUEST);
            } else {
                basketDetailService.save(new BasketDetail(basket, product));
            }
        } else {
            Basket basket = basketService.save(new Basket(user));
            basketDetailService.save(new BasketDetail(basket, product));
        }

        return new ResponseEntity(new ApiResponse(true, "Product has been saved in Basket"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProductInBasket(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Basket basket = user.getBasket();
        Set<Product> products = basketDetailService.findAllProductByBasket(basket);
        Set<ProductInfoResponse> productInfoRespons = new HashSet<>();
        for(Product product : products){
            Market market = marketService.findMarketByProduct(product);
            productInfoRespons.add(new ProductInfoResponse(product, market.getMarketId(), market.getMarketName()));
        }

        return new ResponseEntity(productInfoRespons, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductFromBasket(@RequestBody BucketRequest bucketRequest){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        if(!(productService.existsByProductId(bucketRequest.getProductId()))){
            return new ResponseEntity(new ApiResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
        }

        Product product = productService.findByProductId(bucketRequest.getProductId());
        if(basketService.existsByUser(user)){
            Basket basket = basketService.findByUser(user);
            if(basketDetailService.existsByBasketAndProduct(basket, product)){
                basketDetailService.deleteByBasketAndProduct(basket, product);
            } else {
                return new ResponseEntity(new ApiResponse(false, "Product does not exist in your basket"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(new ApiResponse(false, "There's no product on your basket"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new ApiResponse(true, "Product has been removed from basket"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
