package com.future.booklook.payload.request;

import java.util.Set;

public class TransactionRequest {
    private Set<String> products;

    public Set<String> getProducts() {
        return products;
    }

    public void setProducts(Set<String> products) {
        this.products = products;
    }
}
