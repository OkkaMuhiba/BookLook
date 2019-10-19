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

    @Column(name = LibraryConstant.USER_ID)
    private String userId;

    @Column(name = LibraryConstant.PRODUCT_ID)
    private String productId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LibraryConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LibraryConstant.PRODUCT_FK, referencedColumnName = ProductConstant.PRODUCT_ID)
    private Product product;
}