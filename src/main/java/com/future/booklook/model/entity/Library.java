package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.LibraryConstant;
import com.future.booklook.model.constants.ProductConstant;
import com.future.booklook.model.constants.UserConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = LibraryConstant.TABLE_NAME)
public class Library {
    @Id
    @Column(name = LibraryConstant.LIBRARY_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String libraryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LibraryConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = LibraryConstant.PRODUCT_FK, referencedColumnName = ProductConstant.PRODUCT_ID)
    private Product product;

    public Library(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public Library() {
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
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
