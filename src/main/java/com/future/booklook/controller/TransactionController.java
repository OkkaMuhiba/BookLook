package com.future.booklook.controller;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.TransactionDetail;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.TransferConfirm;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.TransactionRequest;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.ProductServiceImpl;
import com.future.booklook.service.impl.TransactionDetailServiceImpl;
import com.future.booklook.service.impl.TransactionServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private TransactionDetailServiceImpl transactionDetailService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoTransaction(@RequestBody TransactionRequest transactionRequest){
        if(transactionRequest.getProducts().isEmpty()){
            return new ResponseEntity(new ApiResponse(false, "There's no product to add into Transaction"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Product> products = new HashSet<>();
        for(String productId : transactionRequest.getProducts()){
            if(productService.existsByProductId(productId)){
                return new ResponseEntity(new ApiResponse(false, "There's no product to add into Transaction"), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            products.add(productService.findByProductId(productId));
        }
        Transaction transaction = transactionService.save(new Transaction(transactionRequest.getCheckout(), TransferConfirm.UNPAID, user));

        for(Product product : products){
            if(transactionDetailService.checkIfProductAlreadyExistInTransaction(transaction, product)){
                return new ResponseEntity(new ApiResponse(false, "Some products are already exist in transaction"), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            transactionDetailService.save(new TransactionDetail(transaction, product));
        }

        return new ResponseEntity(new ApiResponse(true, "Products have been added into transaction"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
