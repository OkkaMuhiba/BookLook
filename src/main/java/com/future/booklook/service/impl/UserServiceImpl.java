package com.future.booklook.service.impl;

import com.future.booklook.model.entity.User;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

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
        return userRepository.findAllUser();
    }
}
