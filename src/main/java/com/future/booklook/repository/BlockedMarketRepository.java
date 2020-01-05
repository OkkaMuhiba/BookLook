package com.future.booklook.repository;

import com.future.booklook.model.entity.BlockedMarket;
import com.future.booklook.model.entity.Market;
import jdk.nashorn.internal.ir.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface BlockedMarketRepository extends JpaRepository<BlockedMarket, String> {
    BlockedMarket findByMarket(Market market);

    Boolean existsByMarket(Market market);

    @Transactional
    void deleteByBlockedId(String BlockedId);

    @Query("select b from BlockedMarket b")
    Set<BlockedMarket> findAllBlockedMarket();
}
