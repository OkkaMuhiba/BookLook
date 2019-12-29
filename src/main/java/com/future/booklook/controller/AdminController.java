package com.future.booklook.controller;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.security.UserPrincipal;
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
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    ProductServiceImpl productService;

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/products/unconfirmed")
    public Set<Product> getAllProductWithUnconfirmedStatus(){
        return productService.findProductWithUnconfirmedStatus();
    }

    @PostMapping("/products/{productId}/confirm")
    public ResponseEntity<?> confirmProduct(@PathVariable String productId){
        if(!(productService.existsByProductId(productId))){
            return new ResponseEntity(new ApiResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
        }
        Product product = productService.findByProductId(productId);
        product.setProductConfirm(ProductConfirm.CONFIRMED);
        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product have been confirmed"), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAuthenticatedAdminData(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        return new ResponseEntity(user, HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
