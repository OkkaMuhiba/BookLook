package com.future.booklook.controller;

import com.future.booklook.model.entity.Library;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.FileStorageServiceImpl;
import com.future.booklook.service.impl.LibraryServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/libraries")
public class LibraryController {
    @Autowired
    private LibraryServiceImpl libraryService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @GetMapping("")
    public ResponseEntity<?> getAllLibraryByUser(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Set<Library> libraries = libraryService.findAllByUser(user);

        return new ResponseEntity(libraries, HttpStatus.OK);
    }

    @GetMapping("/books/{userId}/{key}/{fileName:.+}")
    public ResponseEntity<?> getBookFromLibrary(@PathVariable String userId, @PathVariable String key, @PathVariable String fileName, HttpServletRequest request){
        if(!(userService.userExistByUserId(userId))){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUserId(userId);
        if(libraryService.existsByUserAndUniqueKey(user, key)){
            Library library = libraryService.findByUserAndUniqueKey(user, key);
            library.setUniqueKey(generateRandomString());
            libraryService.save(library);
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
