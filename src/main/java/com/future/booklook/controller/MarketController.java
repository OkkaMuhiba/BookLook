package com.future.booklook.controller;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.EditMarket;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.FileStorageServiceImpl;
import com.future.booklook.service.impl.MarketServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Api
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
                user,
                photoUri
        );

        marketService.save(market);
        return new ResponseEntity(new ApiResponse(true, "Market created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getMarketData(){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        return new ResponseEntity(marketService.findByUser(user), HttpStatus.OK);
    }

    @PutMapping("/edit/profile")
    public ResponseEntity<?> editMarketProfile(@RequestBody EditMarket editMarket){
        String userId = getUserPrincipal().getUserId();
        Market market = userService.findByUserId(userId).getMarket();

        market.setMarketName(editMarket.getMarketName());
        market.setMarketBio(editMarket.getMarketBio());

        marketService.save(market);
        return new ResponseEntity(new ApiResponse(true, "Market has been edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/profile/photo")
    public ResponseEntity<?> editPhotoMarket(@RequestParam MultipartFile picture){
        String userId = getUserPrincipal().getUserId();
        Market market = userService.findByUserId(userId).getMarket();

        MultipartFile pictureFile = picture;
        String fileName = fileStorageService.storeFile(pictureFile, "markets");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/markets/")
                .path(fileName)
                .toUriString();

        market.setMarketPhoto(photoUri);
        marketService.save(market);
        return new ResponseEntity(new ApiResponse(true, "Photo market has been edited successfully"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
