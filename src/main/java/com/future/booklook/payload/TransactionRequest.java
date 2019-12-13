package com.future.booklook.payload;

import java.util.Set;

public class TransactionRequest {
    private Set<String> products;
    private Long checkout;

    public Set<String> getProducts() {
        return products;
    }

    public void setProducts(Set<String> products) {
        this.products = products;
    }

    public Long getCheckout() {
        return checkout;
    }

    public void setCheckout(Long checkout) {
        this.checkout = checkout;
    }
}
