package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.TransactionConstant;
import com.future.booklook.model.constants.UserConstant;
import com.future.booklook.model.entity.properties.TransferConfirm;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = TransactionConstant.TABLE_NAME)
public class Transaction {
    @Id
    @Column(name = TransactionConstant.TRANSACTION_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String transactionId;

    @Column(name = TransactionConstant.CHECKOUT)
    private Long checkout;

    @Column(name = TransactionConstant.TRANSACTION_CODE)
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = TransactionConstant.TRANSFER_CONFIRM)
    private TransferConfirm transferConfirm;

    @Column(name = TransactionConstant.CREATED_AT)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = TransactionConstant.UPDATED_AT)
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = TransactionConstant.USER_ID)
    private String userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TransactionConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "transaction")
    private Set<TransactionDetail> transactionDetails;

    public Transaction(Long checkout, User user, String transactionCode) {
        this.checkout = checkout;
        this.user = user;
        this.transactionCode = transactionCode;
        this.transferConfirm = TransferConfirm.UNPAID;
        this.userId = user.getUserId();
    }

    public Transaction() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getCheckout() {
        return checkout;
    }

    public void setCheckout(Long checkout) {
        this.checkout = checkout;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public TransferConfirm getTransferConfirm() {
        return transferConfirm;
    }

    public void setTransferConfirm(TransferConfirm transferConfirm) {
        this.transferConfirm = transferConfirm;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<TransactionDetail> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(Set<TransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
