package com.future.booklook.controller;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.BlockedMarket;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.CreateMarketRequest;
import com.future.booklook.payload.EditMarket;
import com.future.booklook.repository.RoleRepository;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.BlockedMarketServiceImpl;
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/markets")
public class MarketController {
    @Autowired
    private MarketServiceImpl marketService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BlockedMarketServiceImpl blockedMarketService;

    @PostMapping("/create")
    public ResponseEntity<?> createMarket(@RequestBody CreateMarketRequest marketRequest){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        if(marketService.marketNameAlreadyExist(marketRequest.getMarketName())){
            return new ResponseEntity(new ApiResponse(false, "Market name have been taken by another user"), HttpStatus.BAD_REQUEST);
        }

        if(marketService.MarketSKUAlreadyExist(marketRequest.getMarketSKU())){
            return new ResponseEntity(new ApiResponse(false, "Market SKU have been taken by another user"), HttpStatus.BAD_REQUEST);
        }

        Market market = new Market(
                marketRequest.getMarketName(),
                marketRequest.getMarketBio(),
                marketRequest.getMarketSKU(),
                userId,
                user
        );

        Set<Role> roles = user.getRoles();
        Role marketRole = roleRepository.findByName(RoleName.ROLE_MARKET)
                .orElseThrow(() -> new AppException("Market Role not set."));
        roles.add(marketRole);
        user.setRoles(roles);
        userService.save(user);

        marketService.save(market);
        return new ResponseEntity(new ApiResponse(true, "Market created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getMarketData(){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        return new ResponseEntity(marketService.findByUser(user), HttpStatus.OK);
    }

    @GetMapping("/{marketId}")
    public ResponseEntity<?> getMarketDataById(@PathVariable String marketId){
        Market market = marketService.findByMarketId(marketId);
        return new ResponseEntity(market, HttpStatus.OK);
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

    @GetMapping("/block/check")
    public ResponseEntity<?> checkIfMarketIsBlocked(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Role> roles = user.getRoles();
        Role blockRole = roleRepository.findByName(RoleName.ROLE_MARKET_BLOCKED)
                .orElseThrow(() -> new AppException("Market Role not set."));

        if(roles.contains(blockRole)){
            Market market = user.getMarket();
            BlockedMarket blockingStatus = blockedMarketService.findBlockedMarketByMarket(market);

            Date date = new Date();
            Timestamp endTimeBlock = new Timestamp(date.getTime());
            if(endTimeBlock.after(blockingStatus.getEndAt())){
                roles.remove(blockRole);
                user.setRoles(roles);
                userService.save(user);
                blockedMarketService.removeBlockedMarket(blockingStatus);
            } else {
                return new ResponseEntity(new ApiResponse(false, "Market is still blocked by administrator"), HttpStatus.OK);
            }
        }

        return new ResponseEntity(new ApiResponse(true, "Market is allowed to be accessed"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
