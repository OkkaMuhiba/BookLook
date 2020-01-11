package com.future.booklook.controller;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.*;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.response.DashboardAdminResponse;
import com.future.booklook.payload.response.UnconfirmedProductResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
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
    private BlockedUserServiceImpl blockedUserService;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @GetMapping("/products/unconfirmed")
    public ResponseEntity<?> getAllProductWithUnconfirmedStatus(){
        Set<Product> products = productService.findProductWithUnconfirmedStatus();
        Set<UnconfirmedProductResponse> responses = new HashSet<>();
        for(Product product : products){
            responses.add(
                    new UnconfirmedProductResponse(product, product.getMarket().getMarketId(), product.getMarket().getMarketName())
            );
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/confirm")
    public ResponseEntity<?> confirmProduct(@PathVariable String productId){
        if(!(productService.existsByProductId(productId))){
            return new ResponseEntity<>(new ApiResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
        }
        Product product = productService.findByProductId(productId);

        if(product.getProductConfirm().equals(ProductConfirm.CONFIRMED)){
            return new ResponseEntity<>(new ApiResponse(false, "Product already confirmed"), HttpStatus.BAD_REQUEST);
        }

        product.setProductConfirm(ProductConfirm.CONFIRMED);
        productService.save(product);
        return new ResponseEntity<>(new ApiResponse(true, "Product have been confirmed"), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAuthenticatedAdminData(){
        return new ResponseEntity<>(userService.getUserFromSession(), HttpStatus.OK);
    }

    @GetMapping("/list/users")
    public ResponseEntity<?> getAllUsers(){
        Set<User> users = userService.findAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/block/user/{userId}/{days}")
    public ResponseEntity<?> blockUserFromUserId(@PathVariable String userId, @PathVariable Long days){
        if(!(userService.userExistByUserId(userId))){
            return new ResponseEntity<>(new ApiResponse(false, "Market does not exist"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUserId(userId);
        Set<Role> roles = user.getRoles();

        Role blockRole = roleService.findByRoleName(RoleName.ROLE_USER_BLOCKED)
                .orElseThrow(() -> new AppException("Market Role not set."));
        
        if(roles.contains(blockRole)){
            return new ResponseEntity<>(new ApiResponse(false, "User already blocked"), HttpStatus.BAD_REQUEST);
        }

        roles.add(blockRole);
        user.setRoles(roles);
        userService.save(user);

        Date date = new Date();
        Timestamp endTimeBlock = new Timestamp(date.getTime() + (days * 86400000));
        BlockedUser blockedUser = new BlockedUser(user, endTimeBlock);
        blockedUserService.saveBlockedUser(blockedUser);

        return new ResponseEntity<>(new ApiResponse(true, "User have been blocked"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/block/user")
    public ResponseEntity<?> listAllBlockedUser(){
        Set<BlockedUser> list = blockedUserService.findAllBlockedMarket();
        return new ResponseEntity<>(list, HttpStatus.OK);
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

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/unblock/user/{userId}")
    public ResponseEntity<?> unblockUser(@PathVariable String userId){
        if(!(userService.userExistByUserId(userId))){
            return new ResponseEntity<>(new ApiResponse(false, "User does not exist"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUserId(userId);
        if(!(blockedUserService.userAlreadyBlockedBefore(user))){
            return new ResponseEntity<>(new ApiResponse(false, "User never be blocked"), HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = user.getRoles();
        Role blockRole = roleService.findByRoleName(RoleName.ROLE_USER_BLOCKED)
                .orElseThrow(() -> new AppException("Market Role not set."));
        roles.remove(blockRole);
        user.setRoles(roles);
        userService.save(user);

        BlockedUser blockedUser = blockedUserService.findBlockedUserByUser(user);
        blockedUserService.removeBlockedUser(blockedUser);

        return new ResponseEntity<>(new ApiResponse(true, "User has been unblocked successfully"), HttpStatus.OK);
    }

    @GetMapping("/check-book/{userId}/{key}/{fileName}")
    public ResponseEntity<?> checkBooksFromAdmin(@PathVariable String userId, @PathVariable String key, @PathVariable String fileName, HttpServletRequest request){
        if(userService.userExistByUserIdAndReadKey(userId, key)){
            User user = userService.findByUserId(userId);
            if(productService.productExistByFilename(fileName)){
                user.setReadKey(generateRandomString());
                userService.save(user);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource("books/"+fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    private String generateRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
