package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Basket;
import com.future.booklook.model.entity.BasketDetail;
import com.future.booklook.model.entity.Product;
import com.future.booklook.repository.BasketDetailRepository;
import com.future.booklook.service.BasketDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BasketDetailServiceImpl implements BasketDetailService {
    @Autowired
    BasketDetailRepository basketDetailRepository;

    public BasketDetail save(BasketDetail basketDetail){
        return basketDetailRepository.save(basketDetail);
    }

    public Boolean existsByBasketAndProduct(Basket basket, Product product){
        return basketDetailRepository.existsByBasketAndProduct(basket, product);
    }

    public Set<BasketDetail> findAllByBasket(Basket basket){
        return basketDetailRepository.findAllByBasket(basket);
    }
}
