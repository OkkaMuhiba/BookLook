package com.future.booklook.payload.response;

public class DashboardAdminResponse {
    private Long user;

    private Long market;

    private Long confirmedBook;

    private Long unconfirmedBook;

    private Long transaction;

    public DashboardAdminResponse(Long user, Long market, Long confirmedBook, Long unconfirmedBook, Long transaction) {
        this.user = user;
        this.market = market;
        this.confirmedBook = confirmedBook;
        this.unconfirmedBook = unconfirmedBook;
        this.transaction = transaction;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getMarket() {
        return market;
    }

    public void setMarket(Long market) {
        this.market = market;
    }

    public Long getConfirmedBook() {
        return confirmedBook;
    }

    public void setConfirmedBook(Long confirmedBook) {
        this.confirmedBook = confirmedBook;
    }

    public Long getUnconfirmedBook() {
        return unconfirmedBook;
    }

    public void setUnconfirmedBook(Long unconfirmedBook) {
        this.unconfirmedBook = unconfirmedBook;
    }

    public Long getTransaction() {
        return transaction;
    }

    public void setTransaction(Long transaction) {
        this.transaction = transaction;
    }
}
