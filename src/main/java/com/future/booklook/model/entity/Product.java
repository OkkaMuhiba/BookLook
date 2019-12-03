package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.CategoryProductConstant;
import com.future.booklook.model.constants.MarketConstant;
import com.future.booklook.model.constants.ProductConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = ProductConstant.TABLE_NAME)
public class Product {
    @Id
    @Column(name = ProductConstant.PRODUCT_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String productId;

    @Column(name = ProductConstant.TITLE)
    private String title;

    @Column(name = ProductConstant.AUTHOR)
    private String author;

    @Column(name = ProductConstant.PUBLISHER)
    private String publisher;

    @Column(name = ProductConstant.SKU)
    private String SKU;

    @Column(name = ProductConstant.DESCRIPTION)
    private String description;

    @Column(name = ProductConstant.PRICE)
    private Long price;

    @Column(name = ProductConstant.PRODUCT_PHOTO)
    private String productPhoto;

    @Column(name = ProductConstant.PRODUCT_FILE)
    private String productFile;

    @Column(name = ProductConstant.CREATED_AT)
    private Date createdAt;

    @Column(name = ProductConstant.UPDATED_AT)
    private Date updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ProductConstant.MARKET_FK, referencedColumnName = MarketConstant.MARKET_ID)
    private Market market;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = CategoryProductConstant.TABLE_NAME,
            joinColumns = @JoinColumn(name = CategoryProductConstant.PRODUCT_ID),
            inverseJoinColumns = @JoinColumn(name = CategoryProductConstant.CATEGORY_ID)
    )
    private Set<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<Wishlist> wishlists;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<Library> libraries;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<TransactionDetail> transactionDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<BasketDetail> basketDetails;

    public Product(String title, String author, String publisher, String SKU, String description, Long price, Set<Category> categories, Market market, String productPhoto, String productFile) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.SKU = SKU;
        this.price = price;
        this.description = description;
        this.categories = categories;
        this.market = market;
        this.productPhoto = productPhoto;
        this.productFile = productFile;
    }

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getProductFile() {
        return productFile;
    }

    public void setProductFile(String productFile) {
        this.productFile = productFile;
    }

    public Set<BasketDetail> getBasketDetails() {
        return basketDetails;
    }

    public void setBasketDetails(Set<BasketDetail> basketDetails) {
        this.basketDetails = basketDetails;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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

    public Set<TransactionDetail> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(Set<TransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
