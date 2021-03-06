package com.future.booklook.controller;

import com.future.booklook.model.entity.*;
import com.future.booklook.model.entity.properties.MarketConfirm;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.model.entity.properties.TransferConfirm;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.response.TransactionDetailResponse;
import com.future.booklook.payload.request.TransactionRequest;
import com.future.booklook.service.impl.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
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

    @Autowired
    private WishlistServiceImpl wishlistService;

    @PostMapping("/user/add")
    public ResponseEntity<?> addProductIntoTransaction(@RequestBody TransactionRequest transactionRequest){
        if(transactionRequest.getProducts().isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false, "There's no product to add into Transaction"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserFromSession();
        Set<Product> products = new HashSet<>();
        Long checkout = new Long(0);
        for(String productId : transactionRequest.getProducts()){
            if(!(productService.existsByProductId(productId))){
                return new ResponseEntity<>(new ApiResponse(false, "There's no product to add into Transaction"), HttpStatus.BAD_REQUEST);
            }
            Product foundProduct = productService.findByProductId(productId);
            products.add(foundProduct);
            checkout += foundProduct.getPrice();
        }

        Date date = new Date();
        String timeInString = new SimpleDateFormat("yyyyMMdd").format(date.getTime());
        Long numberOfTransaction =  transactionService.getAllTransactionInNumber() + 1;
        StringBuilder endCode = new StringBuilder();

        for(int i = 0; i < (6 - numberOfTransaction.toString().length()); i++){
            endCode.append("0");
        }
        endCode.append(numberOfTransaction.toString());
        String finalEndCode = endCode.toString();

        Transaction transaction = transactionService.save(new Transaction(checkout, user, ("BLK-"+ timeInString + "-" + finalEndCode)));

        for(Product product : products){
            if(transactionDetailService.checkIfProductAlreadyExistInTransaction(transaction, product)){
                return new ResponseEntity<>(new ApiResponse(false, "Some products are already exist in transaction"), HttpStatus.BAD_REQUEST);
            }

            Market market = marketService.findMarketByProduct(product);
            String marketId = market.getMarketId();
            transactionDetailService.save(new TransactionDetail(transaction, product, marketId));
        }

        Basket basket = basketService.findByUser(user);
        for(Product product : products){
            if(basketDetailService.existsByBasketAndProduct(basket, product)){
                basketDetailService.deleteByBasketAndProduct(basket, product);
            }
            if(wishlistService.existsByUserAndProduct(user, product)){
                wishlistService.deleteByUserAndProduct(user, product);
            }
        }

        return new ResponseEntity<>(new ApiResponse(true, transaction.getTransactionCode()), HttpStatus.OK);
    }

    @GetMapping("/user/show")
    public ResponseEntity<?> showTransactionsForUser(){
        User user = userService.getUserFromSession();
        Set<Transaction> transactions = transactionService.findAllTransactionByUser(user);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/user/show/{transactionCode}")
    public ResponseEntity<?> showTransactionDetailForUser(@PathVariable String transactionCode){
        Transaction transaction = transactionService.findByTransactionCode(transactionCode);
        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllByTransaction(transaction);

        TransactionDetailResponse response = new TransactionDetailResponse(transaction, transactionDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/user/confirm/{transactionCode}")
    public ResponseEntity<?> confirmTransferTransaction(@PathVariable String transactionCode){
        User user = userService.getUserFromSession();
        if(!(transactionService.existsByTransactionCodeAndUser(transactionCode, user))){
            return new ResponseEntity<>(new ApiResponse(false, "The transaction cannot be confirmed"), HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = transactionService.findByTransactionCode(transactionCode);
        if(transaction.getTransferConfirm() != TransferConfirm.UNPAID){
            return new ResponseEntity<>(new ApiResponse(false, "The transaction already confirmed"), HttpStatus.BAD_REQUEST);
        }

        transaction.setTransferConfirm(TransferConfirm.PENDING);

        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllByTransaction(transaction);
        Boolean statusTransaction = Boolean.TRUE;
        for(TransactionDetail transactionDetail : transactionDetails){
            Product product = transactionDetail.getProduct();
            if(productIsPassedDirectlyIntoUser(product)){
                transactionDetail.setMarketConfirm(MarketConfirm.CONFIRMED);
                transactionDetailService.save(transactionDetail);

                User buyerUser = transactionService.findUserFromTransaction(transaction);
                Library library = new Library(buyerUser, product);
                buyerUser.setReadKey(generateRandomString());
                libraryService.save(library);
                userService.save(buyerUser);
            }

            if(transactionDetail.getMarketConfirm().equals(MarketConfirm.UNCONFIRMED)){
                statusTransaction = false;
            }
        }

        if(statusTransaction){
            transaction.setTransferConfirm(TransferConfirm.SUCCESS);
        }

        transactionService.save(transaction);

        return new ResponseEntity<>(new ApiResponse(true, "Transfer have been confirmed. " +
                "Wait market for confirm your transfer to get the book."), HttpStatus.ACCEPTED);
    }

    @GetMapping("/market/show")
    public ResponseEntity<?> showTransactionDetailsForMarket(){
        User user = userService.getUserFromSession();
        Market market = marketService.findByUser(user);
        Set<Transaction> transactions = transactionService.findAllTransactionByMarket(market);
        Set<Transaction> selectedTransactions = new HashSet<>();

        for(Transaction transaction : transactions){
            if(!(transaction.getTransferConfirm().equals(TransferConfirm.UNPAID))){
                selectedTransactions.add(transaction);
            }
        }

        return new ResponseEntity<>(selectedTransactions, HttpStatus.OK);
    }

    @GetMapping("/market/show/{transactionCode}")
    public ResponseEntity<?> showTransactionDetailWithProductFromMarket(@PathVariable String transactionCode){
        if(!(transactionService.existsByTransactionCode(transactionCode))){
            return new ResponseEntity<>(new ApiResponse(false, "The transaction doesn't exist"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserFromSession();
        Market market = marketService.findByUser(user);

        Transaction transaction = transactionService.findByTransactionCode(transactionCode);
        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllByTransaction(transaction);
        Set<TransactionDetail> selectedTransactionDetails = new HashSet<>();

        for(TransactionDetail transactionDetail : transactionDetails){
            if(transactionDetail.getMarketId().equals(market.getMarketId())){
                selectedTransactionDetails.add(transactionDetail);
            }
        }

        TransactionDetailResponse response = new TransactionDetailResponse(transaction, selectedTransactionDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/market/confirm/{transactionCode}")
    public ResponseEntity<?> confirmProductFromMarket(@PathVariable String transactionCode){
        if(!(transactionService.existsByTransactionCode(transactionCode))){
            return new ResponseEntity<>(new ApiResponse(false, "There's no transaction be recorded"), HttpStatus.BAD_REQUEST);
        }

        User marketUser = userService.getUserFromSession();
        Market market = marketService.findByUser(marketUser);

        Transaction transaction = transactionService.findByTransactionCode(transactionCode);
        User buyerUser = transactionService.findUserFromTransaction(transaction);
        Set<TransactionDetail> transactionDetails = transactionDetailService.findAllByTransaction(transaction);

        for(TransactionDetail transactionDetail : transactionDetails){
            if(transactionDetail.getMarketId().equals(market.getMarketId())){
                if(transaction.getTransferConfirm() == TransferConfirm.UNPAID){
                    return new ResponseEntity<>(new ApiResponse(false, "The user haven't paid this product"), HttpStatus.BAD_REQUEST);
                }

                if(transactionDetail.getMarketConfirm() == MarketConfirm.CONFIRMED){
                    return new ResponseEntity<>(new ApiResponse(false, "The product already confirmed"), HttpStatus.BAD_REQUEST);
                }

                transactionDetail.setMarketConfirm(MarketConfirm.CONFIRMED);
                transactionDetailService.save(transactionDetail);

                Product product = transactionDetail.getProduct();
                Library library = new Library(buyerUser, product);
                buyerUser.setReadKey(generateRandomString());
                userService.save(buyerUser);
                libraryService.save(library);
            }
        }

        transactionDetails = transactionDetailService.findAllByTransaction(transaction);
        Boolean statusTransaction = Boolean.TRUE;
        for(TransactionDetail checkTransaction : transactionDetails){
            if(checkTransaction.getMarketConfirm() != MarketConfirm.CONFIRMED){
                statusTransaction = false;
                break;
            }
        }

        if(statusTransaction){
            transaction.setTransferConfirm(TransferConfirm.SUCCESS);
            transactionService.save(transaction);
        }

        return new ResponseEntity<>(new ApiResponse(true, "Product have been confirmed"), HttpStatus.ACCEPTED);
    }

    private String generateRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private Boolean productIsPassedDirectlyIntoUser(Product product){
        Market market = marketService.findMarketByProduct(product);
        User user = market.getUser();
        Set<Role> roles = user.getRoles();
        for(Role role : roles){
            if(role.getName().equals(RoleName.ROLE_USER_BLOCKED)){
                return true;
            }
        }

        return false;
    }
}
