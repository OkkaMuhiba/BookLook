package com.future.booklook.controller;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class HomeController {

    @GetMapping("/api/home")
    public ResponseEntity<String> home(){
        return new ResponseEntity<>("Hello, Happy world~", HttpStatus.OK);
    }
}
