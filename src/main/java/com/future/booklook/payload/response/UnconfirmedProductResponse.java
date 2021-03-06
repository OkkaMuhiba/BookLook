package com.future.booklook.payload.response;

import com.future.booklook.model.entity.Product;

public class UnconfirmedProductResponse {
    private Product product;

    private String marketId;

    private String marketName;

    public UnconfirmedProductResponse(Product product, String marketId, String marketName) {
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
