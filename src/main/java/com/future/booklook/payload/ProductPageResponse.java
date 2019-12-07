package com.future.booklook.payload;

import com.future.booklook.model.entity.Product;

public class ProductPageResponse {
    Product product;
    String marketName;

    public ProductPageResponse(Product product, String marketName) {
        this.product = product;
        this.marketName = marketName;
    }
}
