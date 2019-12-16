package com.future.booklook.controller;

import com.future.booklook.model.entity.*;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.model.entity.properties.TransferConfirm;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.TransactionRequest;
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

    @Autowired
    private MarketServiceImpl marketService;

    @Autowired
    private LibraryServiceImpl libraryService;

    @Autowired
    private BasketServiceImpl basketService;

    @Autowired
    private BasketDetailServiceImpl basketDetailService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductIntoTransaction(@RequestBody TransactionRequest transactionRequest){
        if(transactionRequest.getProducts().isEmpty()){
            return new ResponseEntity(new ApiResponse(false, "There's no product to add into Transaction"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Product> products = new HashSet<>();
        Long checkout = new Long(0);
        for(String productId : transactionRequest.getProducts()){
            if(!(productService.existsByProductId(productId))){
                return new ResponseEntity(new ApiResponse(false, "There's no product to add into Transaction"), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Product foundProduct = productService.findByProductId(productId);
            products.add(foundProduct);
            checkout += foundProduct.getPrice();
        }
        Transaction transaction = transactionService.save(new Transaction(checkout, user));

        for(Product product : products){
            if(transactionDetailService.checkIfProductAlreadyExistInTransaction(transaction, product)){
                return new ResponseEntity(new ApiResponse(false, "Some products are already exist in transaction"), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            transactionDetailService.save(new TransactionDetail(transaction, product));
        }

        Basket basket = basketService.findByUser(user);
        for(Product product : products){
            basketDetailService.deleteByBasketAndProduct(basket, product);
        }

        return new ResponseEntity(new ApiResponse(true, "Products have been added into transaction"), HttpStatus.OK);
    }

    @GetMapping("/user/show")
    public ResponseEntity<?> showTransactionsForUser(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Transaction> transactions = transactionService.findAllTransactionByUser(user);

        return new ResponseEntity(transactions, HttpStatus.OK);
    }

    @GetMapping("/user/show/{transactionId}")
    public ResponseEntity<?> showTransactionDetailForUser(@PathVariable String transactionId){
        Transaction transaction = transactionService.findByTransactionId(transactionId);
        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllByTransaction(transaction);
        return new ResponseEntity(transactionDetails, HttpStatus.OK);
    }

    @GetMapping("/market/show")
    public ResponseEntity<?> showTransactionDetailsForMarket(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Market market = marketService.findByUser(user);
        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllTransactionDetailFromMarket(market);

        return new ResponseEntity(transactionDetails, HttpStatus.OK);
    }

    @PutMapping("/market/confirm/{transactionDetailId}")
    public ResponseEntity<?> confirmProductFromMarket(@PathVariable String transactionDetailId){
        TransactionDetail transactionDetail = transactionDetailService.findByTransactionDetailId(transactionDetailId);
        transactionDetail.setProductConfirm(ProductConfirm.CONFIRMED);
        transactionDetailService.save(transactionDetail);

        Transaction transaction = transactionDetail.getTransaction();
        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllByTransaction(transaction);
        Boolean statusTransaction = true;
        for(TransactionDetail checkTransaction : transactionDetails){
            if(checkTransaction.getProductConfirm() != ProductConfirm.CONFIRMED){
                statusTransaction = false;
                break;
            }
        }

        if(statusTransaction){
            transaction.setTransferConfirm(TransferConfirm.SUCCESS);
            transactionService.save(transaction);
        }

        User user = transactionDetail.getTransaction().getUser();
        Product product = transactionDetail.getProduct();
        libraryService.save(new Library(user, product));
        return new ResponseEntity(new ApiResponse(true, "Product have been confirmed"), HttpStatus.ACCEPTED);
    }

    @PutMapping("/user/confirm/{transactionId}")
    public ResponseEntity<?> confirmTransferTransaction(@PathVariable String transactionId){
        Transaction transaction = transactionService.findByTransactionId(transactionId);
        transaction.setTransferConfirm(TransferConfirm.PENDING);
        transactionService.save(transaction);

        return new ResponseEntity(new ApiResponse(true, "Transfer have been confirmed. " +
                "Wait market for confirm your transfer to get the book."), HttpStatus.ACCEPTED);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
