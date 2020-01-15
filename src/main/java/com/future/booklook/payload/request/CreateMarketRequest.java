package com.future.booklook.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateMarketRequest {
    @NotBlank
    @Size(min = 6, max = 32)
    private String marketName;

    @NotBlank
    private String marketBio;

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketBio() {
        return marketBio;
    }

    public void setMarketBio(String marketBio) {
        this.marketBio = marketBio;
    }
}
