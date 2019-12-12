package com.future.booklook.payload;

import java.util.Set;

public class TransactionRequest {
    Set<String> products;

    public Set<String> getProducts() {
        return products;
    }

    public void setProducts(Set<String> products) {
        this.products = products;
    }
}
