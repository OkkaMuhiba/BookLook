package com.future.booklook.controller;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.service.impl.ProductServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    ProductServiceImpl productService;

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
}
