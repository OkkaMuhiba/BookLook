package com.future.booklook.controller;

import com.future.booklook.model.entity.Library;
import com.future.booklook.model.entity.User;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.LibraryServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Api
@RestController
@RequestMapping("/api/libraries")
public class LibraryController {
    @Autowired
    private LibraryServiceImpl libraryService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    public ResponseEntity<?> getAllLibraryByUser(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Library> libraries = libraryService.findAllByUser(user);

        return new ResponseEntity(libraries, HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
