package com.future.booklook.controller;

import com.future.booklook.model.entity.Basket;
import com.future.booklook.model.entity.BasketDetail;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.BasketDetailServiceImpl;
import com.future.booklook.service.impl.BasketServiceImpl;
import com.future.booklook.service.impl.ProductServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
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

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoBucket(@RequestBody String productId){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Product product = productService.findByProductId(productId);

        if(basketService.existsByUser(user)){
            Basket basket = basketService.findByUser(user);
            if(basketDetailService.existsByBasketAndProduct(basket, product)){
                return new ResponseEntity(new ApiResponse(false, "Product already exists in Basket"), HttpStatus.UNPROCESSABLE_ENTITY);
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
        Set<BasketDetail> basketDetails = basketDetailService.findAllByBasket(basket);

        return new ResponseEntity(basketDetails, HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
