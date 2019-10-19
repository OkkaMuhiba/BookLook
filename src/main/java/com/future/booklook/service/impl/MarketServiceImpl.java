package com.future.booklook.service.impl;

import com.future.booklook.repository.MarketRepository;
import com.future.booklook.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketServiceImpl implements MarketService {
    @Autowired
    private MarketRepository marketRepository;
}
