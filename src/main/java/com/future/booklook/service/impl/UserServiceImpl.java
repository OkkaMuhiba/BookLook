package com.future.booklook.service.impl;

import com.future.booklook.model.entity.User;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    public User findByUserId(String userId){
        return userRepository.findByUserId(userId);
    }
}
