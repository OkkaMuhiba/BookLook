package com.future.booklook.controller;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.CreateProduct;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.MarketServiceImpl;
import com.future.booklook.service.impl.ProductServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductServiceImpl productService;

    @Autowired
    MarketServiceImpl marketService;

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProduct createProduct){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);
        Market market = marketService.findByUser(user);

        Product product = new Product(
                createProduct.getTitle(),
                createProduct.getAuthor(),
                createProduct.getPublisher(),
                market.getMarketSKU()+"-"+createProduct.getSKU(),
                createProduct.getDescription(),
                Long.parseLong(createProduct.getPrice()),
                market
        );

        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product created successfully"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
