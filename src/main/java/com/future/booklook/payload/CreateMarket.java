package com.future.booklook.payload;

public class CreateMarket {
    private String marketName;
    private String marketSKU;
    private String marketBio;

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketSKU() {
        return marketSKU;
    }

    public void setMarketSKU(String marketSKU) {
        this.marketSKU = marketSKU;
    }

    public String getMarketBio() {
        return marketBio;
    }

    public void setMarketBio(String marketBio) {
        this.marketBio = marketBio;
    }
}
