package com.future.booklook.controller;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.JwtAuthenticationResponse;
import com.future.booklook.payload.LoginRequest;
import com.future.booklook.payload.SignUpRequest;
import com.future.booklook.repository.RoleRepository;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.security.JwtTokenProvider;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.FileStorageServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    FileStorageServiceImpl fileStorageService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationAttempt(loginRequest);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByUserId(userPrincipal.getUserId());
        Set<Role> roles = user.getRoles();
        Boolean authenticatedStatus = true;
        for(Role role : roles){
            if(role.getName() == RoleName.ROLE_ADMIN){
                authenticatedStatus = false;
                break;
            }
        }

        if(!(authenticatedStatus)){
            return new ResponseEntity(new JwtAuthenticationResponse(false, "You're not allowed to login"), HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(true, jwt));
    }

    @PostMapping("/admin/signin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationAttempt(loginRequest);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByUserId(userPrincipal.getUserId());
        Set<Role> roles = user.getRoles();
        Boolean authenticatedStatus = false;
        for(Role role : roles){
            if(role.getName() == RoleName.ROLE_ADMIN){
                authenticatedStatus = true;
                break;
            }
        }

        if(!(authenticatedStatus)){
            return new ResponseEntity(new JwtAuthenticationResponse(false, "You're not allowed to login"), HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(true, jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getNumberPhone()
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        return new ResponseEntity(new ApiResponse(true, "User registered successfully"), HttpStatus.OK);
    }

    public Authentication authenticationAttempt(LoginRequest loginRequest){
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
    }
}
