package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.BasketConstant;
import com.future.booklook.model.constants.UserConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = BasketConstant.TABLE_NAME)
public class Basket {
    @Id
    @Column(name =BasketConstant.BASKET_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String basketId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BasketConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "basket")
    private Set<BasketDetail> basketDetails;

    public Basket(User user) {
        this.user = user;
    }

    public Basket() {
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<BasketDetail> getBasketDetails() {
        return basketDetails;
    }

    public void setBasketDetails(Set<BasketDetail> basketDetails) {
        this.basketDetails = basketDetails;
    }
}
