package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.MarketRepository;
import com.future.booklook.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MarketServiceImpl implements MarketService {
    @Autowired
    private MarketRepository marketRepository;

    public Market save(Market market){
        return marketRepository.save(market);
    }

    public Market findByUser(User user) {
        return marketRepository.findByUser(user);
    }

    public Market findMarketByProduct(Product product){
        Set<Product> products = new HashSet<>();
        products.add(product);

        return marketRepository.findByProducts(products);
    }
}
