package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.MarketConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = MarketConstant.TABLE_NAME)
public class Market {

    @Id
    @Column(name = MarketConstant.MARKET_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String marketId;

    @Column(name = MarketConstant.MARKET_NAME)
    private String marketName;

    @Column(name = MarketConstant.MARKET_BIO)
    private String marketBio;

    @Column(name = MarketConstant.MARKET_PHOTO)
    private String marketPhoto;

    @Column(name = MarketConstant.MARKET_SKU)
    private String marketSKU;

    @Column(name = MarketConstant.CREATED_AT)
    private Date createdAt;

    @Column(name = MarketConstant.UPDATED_AT)
    private Date updatedAt;

    @JsonIgnore
    @OneToOne(mappedBy = "market", fetch = FetchType.EAGER)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY)
    private Set<Product> products;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketBio() {
        return marketBio;
    }

    public void setMarketBio(String marketBio) {
        this.marketBio = marketBio;
    }

    public String getMarketPhoto() {
        return marketPhoto;
    }

    public void setMarketPhoto(String marketPhoto) {
        this.marketPhoto = marketPhoto;
    }

    public String getMarketSKU() {
        return marketSKU;
    }

    public void setMarketSKU(String marketSKU) {
        this.marketSKU = marketSKU;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
