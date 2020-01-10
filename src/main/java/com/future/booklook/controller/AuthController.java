package com.future.booklook.controller;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.BlockedUser;
import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.response.JwtAuthenticationResponse;
import com.future.booklook.payload.request.SignInRequest;
import com.future.booklook.payload.request.SignUpRequest;
import com.future.booklook.security.JwtTokenProvider;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.BlockedUserServiceImpl;
import com.future.booklook.service.impl.RoleServiceImpl;
import com.future.booklook.service.impl.UserServiceImpl;
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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private BlockedUserServiceImpl blockedUserService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationAttempt(signInRequest);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.findByUserId(userPrincipal.getUserId());
        Set<Role> roles = user.getRoles();
        Boolean authenticatedStatus = Boolean.TRUE;
        String blockedUntil = null;
        for(Role role : roles){
            if(role.getName().equals(RoleName.ROLE_ADMIN)){
                authenticatedStatus = false;
                break;
            }

            if(role.getName().equals(RoleName.ROLE_USER_BLOCKED)){
                BlockedUser blockedUser = blockedUserService.findBlockedUserByUser(user);
                Date date = new Date();
                date.setTime(blockedUser.getEndAt().getTime());
                blockedUntil = new SimpleDateFormat("d-M-yyyy").format(date);
                break;
            }
        }

        if(!(authenticatedStatus)){
            if(blockedUntil == null){
                return new ResponseEntity<>(new JwtAuthenticationResponse(false, "You're not allowed to login"), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(new JwtAuthenticationResponse(false, "You're blocked by admin, until "+blockedUntil), HttpStatus.BAD_REQUEST);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(true, jwt));
    }

    @PostMapping("/admin/signin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationAttempt(signInRequest);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.findByUserId(userPrincipal.getUserId());
        Set<Role> roles = user.getRoles();
        Boolean authenticatedStatus = false;
        for(Role role : roles){
            if(role.getName() == RoleName.ROLE_ADMIN){
                authenticatedStatus = Boolean.TRUE;
                break;
            }
        }

        if(!(authenticatedStatus)){
            return new ResponseEntity<>(new JwtAuthenticationResponse(false, "You're not allowed to login"), HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(true, jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userService.existByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
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

        Role userRole = roleService.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> removeAuthenticationUser(HttpServletRequest request) throws ServletException {
        try{
            request.logout();
            HttpSession session= request.getSession(false);
            SecurityContextHolder.clearContext();
            if(session != null) {
                session.invalidate();
            }
            for(Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ServletException e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    private Authentication authenticationAttempt(SignInRequest signInRequest){
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsernameOrEmail(),
                        signInRequest.getPassword()
                )
        );
    }
}
