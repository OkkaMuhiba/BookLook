package com.future.booklook.controller;

import com.future.booklook.model.entity.*;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.UnconfirmedProductResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.BlockedMarketServiceImpl;
import com.future.booklook.service.impl.MarketServiceImpl;
import com.future.booklook.service.impl.ProductServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MarketServiceImpl marketService;

    @Autowired
    private BlockedMarketServiceImpl blockedMarketService;

    @GetMapping("/products/unconfirmed")
    public ResponseEntity<?> getAllProductWithUnconfirmedStatus(){
        Set<Product> products = productService.findProductWithUnconfirmedStatus();
        Set<UnconfirmedProductResponse> responses = new HashSet<>();
        for(Product product : products){
            responses.add(
                    new UnconfirmedProductResponse(product, product.getMarket().getMarketId(), product.getMarket().getMarketName())
            );
        }

        return new ResponseEntity(responses, HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/confirm")
    public ResponseEntity<?> confirmProduct(@PathVariable String productId){
        if(!(productService.existsByProductId(productId))){
            return new ResponseEntity(new ApiResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
        }
        Product product = productService.findByProductId(productId);

        if(product.getProductConfirm().equals(ProductConfirm.CONFIRMED)){
            return new ResponseEntity(new ApiResponse(false, "Product already confirmed"), HttpStatus.BAD_REQUEST);
        }

        product.setProductConfirm(ProductConfirm.CONFIRMED);
        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product have been confirmed"), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAuthenticatedAdminData(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @GetMapping("/list/market")
    public ResponseEntity<?> getAllMarket(){
        Set<Market> markets = marketService.findAllMarket();
        return new ResponseEntity(markets, HttpStatus.OK);
    }

    @PostMapping("/block/market/{marketId}")
    public ResponseEntity<?> blockMarketFromMarketId(@PathVariable String marketId){
        if(!(marketService.marketExistByMarketId(marketId))){
            return new ResponseEntity(new ApiResponse(false, "Market does not exist"), HttpStatus.BAD_REQUEST);
        }

        Market market = marketService.findByMarketId(marketId);
        Date date = new Date();
        Timestamp endTimeBlock = new Timestamp(date.getTime() + 86400000);
        BlockedMarket blockedMarket = new BlockedMarket(market, endTimeBlock);
        blockedMarketService.saveBlockedMarket(blockedMarket);

        User user = market.getUser();
        Set<Role> roles = user.getRoles();
        roles.add(new Role(RoleName.ROLE_MARKET_BLOCKED));
        user.setRoles(roles);
        userService.save(user);

        return new ResponseEntity(new ApiResponse(true, "Market have been blocked"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/block/market")
    public ResponseEntity<?> listAllBlockedMarket(){
        Set<BlockedMarket> list = blockedMarketService.findAllBlockedMarket();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
