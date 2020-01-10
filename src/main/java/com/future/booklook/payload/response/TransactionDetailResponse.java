package com.future.booklook.payload.response;

import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.TransactionDetail;

import java.util.Set;

public class TransactionDetailResponse {
    private Transaction transaction;
    private Set<TransactionDetail> transactionDetail;

    public TransactionDetailResponse(Transaction transaction, Set<TransactionDetail> transactionDetail) {
        this.transaction = transaction;
        this.transactionDetail = transactionDetail;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Set<TransactionDetail> getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(Set<TransactionDetail> transactionDetail) {
        this.transactionDetail = transactionDetail;
    }
}
