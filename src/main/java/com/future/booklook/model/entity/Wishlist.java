package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.ProductConstant;
import com.future.booklook.model.constants.UserConstant;
import com.future.booklook.model.constants.WishlistConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

 @Entity
 @Table(name = WishlistConstant.TABLE_NAME)
public class Wishlist {
    @Id
    @Column(name = WishlistConstant.WISHLIST_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String wishlistId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WishlistConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WishlistConstant.PRODUCT_FK, referencedColumnName = ProductConstant.PRODUCT_ID)
    private Product product;

     public Wishlist() {
     }

     public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
