package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.MarketConstant;
import com.future.booklook.model.constants.UserConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = UserConstant.TABLE_NAME)
public class User {
    @Id
    @Column(name = UserConstant.USER_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String userId;

    @Column(name = UserConstant.USERNAME)
    private String username;

    @Column(name = UserConstant.PASSWORD)
    private String password;

    @Column(name = UserConstant.EMAIL)
    private String email;

    @Column(name = UserConstant.NAME)
    private String name;

    @Column(name = UserConstant.NUMBER_PHONE)
    private String numberPhone;

    @Column(name = UserConstant.ROLE)
    private String role;

    @Column(name = UserConstant.PHOTO_USER)
    private String photoUser;

    @Column(name = UserConstant.CREATED_AT)
    private Date createdAt;

    @Column(name = UserConstant.UPDATED_AT)
    private Date updatedAt;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Market market;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Wishlist> wishlists;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Library> libraries;

    public User() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
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

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Set<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }
}
