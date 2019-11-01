package com.future.booklook.security;

import com.future.booklook.exception.ResourceNotFoundException;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : "+usernameOrEmail));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));

        return UserPrincipal.create(user);
    }
}
