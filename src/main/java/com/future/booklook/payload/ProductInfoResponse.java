package com.future.booklook.payload;

import com.future.booklook.model.entity.Product;

public class ProductInfoResponse {
    Product product;
    String marketId;
    String marketName;

    public ProductInfoResponse(Product product, String marketId, String marketName) {
        this.product = product;
        this.marketId = marketId;
        this.marketName = marketName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
}
