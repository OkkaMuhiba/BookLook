package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Basket;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.BasketRepository;
import com.future.booklook.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    BasketRepository basketRepository;

    public Basket save(Basket basket){
        return basketRepository.save(basket);
    }

    public Boolean existsByUser(User user){
        return basketRepository.existsByUser(user);
    }

    public Basket findByUser(User user){
        return basketRepository.findByUser(user);
    }
}
