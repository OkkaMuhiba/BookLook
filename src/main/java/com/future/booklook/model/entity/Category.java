package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.CategoryConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

 @Entity
 @Table(name = CategoryConstant.TABLE_NAME)
public class Category {
    @Id
    @Column(name = CategoryConstant.CATEGORY_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String categortyId;

    @Column(name = CategoryConstant.CATEGORY_NAME)
    private String categoryName;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    public Category(String categoryName) {
         this.categoryName = categoryName;
    }

    public Category(){

    }

    public String getCategortyId() {
        return categortyId;
    }

    public void setCategortyId(String categortyId) {
        this.categortyId = categortyId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
