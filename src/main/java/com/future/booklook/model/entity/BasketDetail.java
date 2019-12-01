package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.BasketConstant;
import com.future.booklook.model.constants.BasketDetailConstant;
import com.future.booklook.model.constants.ProductConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = BasketDetailConstant.TABLE_NAME)
public class BasketDetail {
    @Id
    @Column(name =BasketDetailConstant.BASKET_DETAIL_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String basketDetailId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BasketDetailConstant.BASKET_FK, referencedColumnName = BasketConstant.BASKET_ID)
    private Basket basket;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BasketDetailConstant.PRODUCT_FK, referencedColumnName = ProductConstant.PRODUCT_ID)
    private Product product;

    public BasketDetail() {
    }

    public String getBasketDetailId() {
        return basketDetailId;
    }

    public void setBasketDetailId(String basketDetailId) {
        this.basketDetailId = basketDetailId;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
