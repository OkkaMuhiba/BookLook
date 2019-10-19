package com.future.booklook.service.impl;

import com.future.booklook.repository.WishlistRepository;
import com.future.booklook.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;
}
