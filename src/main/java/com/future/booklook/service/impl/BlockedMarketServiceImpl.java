package com.future.booklook.service.impl;

import com.future.booklook.model.entity.BlockedMarket;
import com.future.booklook.model.entity.Market;
import com.future.booklook.repository.BlockedMarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BlockedMarketServiceImpl {
    @Autowired
    private BlockedMarketRepository blockedMarketRepository;

    public BlockedMarket saveBlockedMarket(BlockedMarket blockedMarket){
        return blockedMarketRepository.save(blockedMarket);
    }

    public void removeBlockedMarket(BlockedMarket blockedMarket){
        blockedMarketRepository.deleteByBlockedId(blockedMarket.getBlockedId());
    }

    public BlockedMarket findBlockedMarketByMarket(Market market){
        return blockedMarketRepository.findByMarket(market);
    }

    public Set<BlockedMarket> findAllBlockedMarket(){
        return blockedMarketRepository.findAllBlockedMarket();
    }

    public Boolean marketAlreadyBlockedBefore(Market market){
        return blockedMarketRepository.existsByMarket(market);
    }
}
