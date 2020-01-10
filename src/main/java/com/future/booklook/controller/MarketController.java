package com.future.booklook.controller;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.BlockedMarket;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.request.CreateMarketRequest;
import com.future.booklook.payload.request.EditMarketRequest;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
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
    private RoleServiceImpl roleService;

    @Autowired
    private BlockedMarketServiceImpl blockedMarketService;

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/create")
    public ResponseEntity<?> createMarket(@RequestBody CreateMarketRequest marketRequest){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        if(marketService.marketExistByUser(user)){
            return new ResponseEntity(new ApiResponse(false, "User already have market"), HttpStatus.BAD_REQUEST);
        }

        if(marketService.marketNameAlreadyExist(marketRequest.getMarketName())){
            return new ResponseEntity(new ApiResponse(false, "Market name have been taken by another user"), HttpStatus.BAD_REQUEST);
        }

        String acceptedMarketCode;
        int endIndex = 2;
        while(true){
            acceptedMarketCode = marketRequest.getMarketName().toUpperCase().substring(0, endIndex);

            if(marketService.MarketCodeAlreadyExist(acceptedMarketCode)){
                endIndex++;
            } else {
                break;
            }
        }

        Market market = new Market(
                marketRequest.getMarketName(),
                marketRequest.getMarketBio(),
                acceptedMarketCode,
                userId,
                user
        );

        Set<Role> roles = user.getRoles();
        Role marketRole = roleService.findByRoleName(RoleName.ROLE_MARKET)
                .orElseThrow(() -> new AppException("Market Role not set."));
        roles.add(marketRole);
        user.setRoles(roles);
        user.setReadKey(generateRandomString());

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
    public ResponseEntity<?> editMarketProfile(@RequestBody EditMarketRequest editMarketRequest){
        String userId = getUserPrincipal().getUserId();
        Market market = userService.findByUserId(userId).getMarket();

        market.setMarketName(editMarketRequest.getMarketName());
        market.setMarketBio(editMarketRequest.getMarketBio());

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
        Role blockRole = roleService.findByRoleName(RoleName.ROLE_MARKET_BLOCKED)
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
            return new ResponseEntity(new ApiResponse(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
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

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
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
