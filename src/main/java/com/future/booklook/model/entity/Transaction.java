package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.TransactionConstant;
import com.future.booklook.model.constants.UserConstant;
import com.future.booklook.model.entity.properties.TransferConfirm;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = TransactionConstant.TABLE_NAME)
public class Transaction {
    @Id
    @Column(name = TransactionConstant.TRANSACTION_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String userId;

    @Column(name = TransactionConstant.CHECKOUT)
    private Long checkout;

    @Enumerated(EnumType.STRING)
    @Column(name = TransactionConstant.TRANSFER_CONFIRM)
    private TransferConfirm transferConfirm;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TransactionConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "transaction")
    private Set<TransactionDetail> transactionDetails;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCheckout() {
        return checkout;
    }

    public void setCheckout(Long checkout) {
        this.checkout = checkout;
    }

    public TransferConfirm getTransferConfirm() {
        return transferConfirm;
    }

    public void setTransferConfirm(TransferConfirm transferConfirm) {
        this.transferConfirm = transferConfirm;
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