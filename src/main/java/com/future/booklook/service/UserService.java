package com.future.booklook.service;

import com.future.booklook.model.entity.User;

public interface UserService {
    User findByUserId(String userId);
}
