package com.future.booklook.payload.response;

public class JwtAuthenticationResponse {
    private Boolean status;
    private String result;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(Boolean status, String accessToken) {
        this.status = status;
        this.result = accessToken;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
