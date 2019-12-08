package com.future.booklook.payload;

import com.future.booklook.model.entity.Product;

public class ProductPageResponse {
    Product product;
    String marketName;

    public ProductPageResponse(Product product, String marketName) {
        this.product = product;
        this.marketName = marketName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
}
