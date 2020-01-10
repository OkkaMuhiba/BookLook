package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.MarketConstant;
import com.future.booklook.model.constants.UserConstant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
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

    @Column(name = MarketConstant.USER_ID)
    private String userID;

    @Column(name = MarketConstant.MARKET_BIO)
    private String marketBio;

    @Column(name = MarketConstant.MARKET_PHOTO)
    private String marketPhoto;

    @Column(name = MarketConstant.MARKET_CODE)
    private String marketCode;

    @Column(name = MarketConstant.TOTAL_PRODUCT)
    private Long totalProduct;

    @Column(name = MarketConstant.CREATED_AT)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = MarketConstant.UPDATED_AT)
    @UpdateTimestamp
    private Timestamp updatedAt;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = MarketConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY)
    private Set<Product> products;

    public Market(String marketName, String marketBio, String marketCode, String userID, User user) {
        this.marketName = marketName;
        this.marketBio = marketBio;
        this.marketCode = marketCode;
        this.userID = userID;
        this.user = user;
        this.totalProduct = new Long(0);
    }

    public Market() {

    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public Long getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(Long totalProduct) {
        this.totalProduct = totalProduct;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
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
