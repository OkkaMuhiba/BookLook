package com.future.booklook.controller;

import com.future.booklook.model.entity.User;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.request.EditProfileRequest;
import com.future.booklook.payload.request.EditUserPasswordRequest;
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
        return new ResponseEntity<>(userService.findByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDataFromUserId(@PathVariable String userId){
        if(userService.userExistByUserId(userId)){
            return new ResponseEntity<>(userService.findByUserId(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "User does not exist"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit/profile")
    public ResponseEntity<?> editUserData(@RequestBody EditProfileRequest editProfileRequest){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        user.setName(editProfileRequest.getName());
        user.setUsername(editProfileRequest.getUsername());
        user.setEmail(editProfileRequest.getEmail());
        user.setNumberPhone(editProfileRequest.getNumberPhone());
        userService.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "Profile edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/profile/photo")
    public ResponseEntity<?> editPhotoProfile(@RequestParam MultipartFile picture){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        String fileName = fileStorageService.storeFile(picture, "users");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/users/")
                .path(fileName)
                .toUriString();

        user.setUserPhoto(photoUri);
        userService.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "Photo Profile edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/password")
    public ResponseEntity<?> editPassword(@RequestBody EditUserPasswordRequest editUserPasswordRequest){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);

        if(!(passwordEncoder.matches(editUserPasswordRequest.getOldPassword(), user.getPassword()))){
            return new ResponseEntity<>(new ApiResponse(false, "Old password is wrong"), HttpStatus.BAD_REQUEST);
        }

        if(passwordEncoder.matches(editUserPasswordRequest.getNewPassword(), user.getPassword())){
            return new ResponseEntity<>(new ApiResponse(false, "Your new password is same as your old password"), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(editUserPasswordRequest.getNewPassword()));
        userService.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "Your password has been changed"), HttpStatus.OK);
    }

    private UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
}
