package com.future.booklook.controller;

import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.EditProfile;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.FileStorageServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Api
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    FileStorageServiceImpl fileStorageService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<?> getUserData(){
        String userId = getUserPrincipal().getUserId();
        return new ResponseEntity(userService.findByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/edit/profile")
    public ResponseEntity<?> editUserData(@RequestBody EditProfile editProfile){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        user.setName(editProfile.getName());
        user.setUsername(editProfile.getUsername());
        user.setEmail(editProfile.getEmail());
        user.setNumberPhone(editProfile.getNumberPhone());
        userService.save(user);

        return new ResponseEntity(new ApiResponse(true, "Profile edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/profile/photo")
    public ResponseEntity<?> editPhotoProfile(@RequestParam MultipartFile picture){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        MultipartFile pictureFile = picture;
        String fileName = fileStorageService.storeFile(pictureFile, "users");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/users/")
                .path(fileName)
                .toUriString();

        user.setUserPhoto(photoUri);
        userService.save(user);

        return new ResponseEntity(new ApiResponse(true, "Photo Profile edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/password")
    public ResponseEntity<?> editPassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        if(user.getPassword() != passwordEncoder.encode(oldPassword)){
            return new ResponseEntity(new ApiResponse(false, "Old password is wrong"), HttpStatus.OK);
        }

        if(user.getPassword() == passwordEncoder.encode(newPassword)){
            return new ResponseEntity(new ApiResponse(false, "Your new password is same as your old password"), HttpStatus.OK);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);

        return new ResponseEntity(new ApiResponse(true, "Your password has been changed"), HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
