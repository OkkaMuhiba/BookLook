package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.BlockedMarketConstant;
import com.future.booklook.model.constants.MarketConstant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = BlockedMarketConstant.TABLE_NAME)
public class BlockedMarket {
    @Id
    @Column(name =BlockedMarketConstant.BLOCKED_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String blockedId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BlockedMarketConstant.MARKET_FK, referencedColumnName = MarketConstant.MARKET_ID)
    private Market market;

    @Column(name = BlockedMarketConstant.START_AT)
    @CreationTimestamp
    private Timestamp startAt;

    @Column(name = BlockedMarketConstant.END_AT)
    private Timestamp endAt;

    public BlockedMarket(Market market, Timestamp endAt) {
        this.market = market;
        this.endAt = endAt;
    }

    public BlockedMarket() {
    }

    public String getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(String blockedId) {
        this.blockedId = blockedId;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }
}
