package com.future.booklook.service.impl;

import com.future.booklook.model.entity.User;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User save(User user){
        return userRepository.save(user);
    }
}
