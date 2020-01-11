package com.future.booklook.service.impl;

import com.future.booklook.exception.AppException;
import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.repository.RoleRepository;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User findByUserId(String userId){
        return userRepository.findByUserId(userId);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public Boolean userExistByUserId(String userId){
        return userRepository.existsByUserId(userId);
    }

    public Long getTotalUserInNumber(){
        return userRepository.count();
    }

    public Boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existByEmail(String email){
        return userRepository.existsByUserId(email);
    }

    public Boolean userExistByUserIdAndReadKey(String userId, String readKey){
        return userRepository.existsByUserIdAndReadKey(userId, readKey);
    }

    public User findUserByUserIdAndReadKey(String userId, String readKey){
        return userRepository.findByUserIdAndReadKey(userId, readKey);
    }

    public Set<User> findAllUser(){
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        return userRepository.findByRoles(Collections.singleton(userRole));
    }

    public User getUserFromSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null){
            return null;
        }

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User user = userRepository.findByUserId(userPrincipal.getUserId());
        return user;
    }
}
