package com.future.booklook.controller;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.*;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.DashboardAdminResponse;
import com.future.booklook.payload.UnconfirmedProductResponse;
import com.future.booklook.repository.RoleRepository;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.*;
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

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private RoleRepository roleRepository;

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

    @PostMapping("/block/market/{marketId}/{days}")
    public ResponseEntity<?> blockMarketFromMarketId(@PathVariable String marketId, @PathVariable Long days){
        if(!(marketService.marketExistByMarketId(marketId))){
            return new ResponseEntity(new ApiResponse(false, "Market does not exist"), HttpStatus.BAD_REQUEST);
        }

        Market market = marketService.findByMarketId(marketId);
        User user = market.getUser();
        Set<Role> roles = user.getRoles();

        Role blockRole = roleRepository.findByName(RoleName.ROLE_MARKET_BLOCKED)
                .orElseThrow(() -> new AppException("Market Role not set."));
        
        if(roles.contains(blockRole)){
            return new ResponseEntity(new ApiResponse(false, "Market already blocked"), HttpStatus.BAD_REQUEST);
        }

        roles.add(blockRole);
        user.setRoles(roles);
        userService.save(user);

        Date date = new Date();
        Timestamp endTimeBlock = new Timestamp(date.getTime() + (days * 86400000));
        BlockedMarket blockedMarket = new BlockedMarket(market, endTimeBlock);
        blockedMarketService.saveBlockedMarket(blockedMarket);

        return new ResponseEntity(new ApiResponse(true, "Market have been blocked"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/block/market")
    public ResponseEntity<?> listAllBlockedMarket(){
        Set<BlockedMarket> list = blockedMarketService.findAllBlockedMarket();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/dashboard/statistic")
    public ResponseEntity<?> getStatisticDataForAdminDashboard(){
        DashboardAdminResponse response = new DashboardAdminResponse(
                userService.getTotalUserInNumber(),
                marketService.getTotalMarketInNumber(),
                productService.getAllConfirmedBookInNumber(),
                productService.getAllUnconfirmedBookInNumber(),
                transactionService.getAllTransactionInNumber()
        );

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/unblock/market/{marketId}")
    public ResponseEntity<?> unblockMarket(@PathVariable String marketId){
        if(!(marketService.marketExistByMarketId(marketId))){
            return new ResponseEntity(new ApiResponse(false, "Market does not exist"), HttpStatus.BAD_REQUEST);
        }

        Market market = marketService.findByMarketId(marketId);
        if(!(blockedMarketService.marketAlreadyBlockedBefore(market))){
            return new ResponseEntity(new ApiResponse(false, "Market never be blocked"), HttpStatus.BAD_REQUEST);
        }

        User user = market.getUser();
        Set<Role> roles = user.getRoles();
        Role blockRole = roleRepository.findByName(RoleName.ROLE_MARKET_BLOCKED)
                .orElseThrow(() -> new AppException("Market Role not set."));
        roles.remove(blockRole);
        user.setRoles(roles);
        userService.save(user);

        BlockedMarket blockedMarket = blockedMarketService.findBlockedMarketByMarket(market);
        blockedMarketService.removeBlockedMarket(blockedMarket);

        return new ResponseEntity(new ApiResponse(true, "Market has been unblocked successfully"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
