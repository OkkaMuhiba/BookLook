package com.future.booklook.controller;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.FileStorageServiceImpl;
import com.future.booklook.service.impl.MarketServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/markets")
public class MarketController {
    @Autowired
    MarketServiceImpl marketService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    FileStorageServiceImpl fileStorageService;

    @PostMapping("/create")
    public ResponseEntity<?> createMarket(
            @RequestParam("marketName") String marketName,
            @RequestParam("marketSKU") String marketSKU,
            @RequestParam("marketBio") String marketBio,
            @RequestParam("marketPhoto") MultipartFile marketPhoto
    ){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        String fileName = "";
        MultipartFile pictureFile = marketPhoto;
        fileName = fileStorageService.storeFile(pictureFile, "markets");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/markets/")
                .path(fileName)
                .toUriString();

        Market market = new Market(
                marketName,
                marketBio,
                marketSKU,
                userId,
                user
        );

        marketService.save(market);
        return new ResponseEntity(new ApiResponse(true, "Market created successfully"), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getMarketData(){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        return new ResponseEntity(marketService.findByUser(user), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
