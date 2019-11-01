package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.UserConstant;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = UserConstant.TABLE_NAME)
public class User{
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

    @Column(name = UserConstant.PHOTO_USER)
    private String photoUser;

    @Column(name = UserConstant.CREATED_AT)
    private Date createdAt;

    @Column(name = UserConstant.UPDATED_AT)
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Market market;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Wishlist> wishlists;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Library> libraries;

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
