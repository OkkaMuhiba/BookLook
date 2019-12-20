package com.future.booklook.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.booklook.model.constants.ProductConstant;
import com.future.booklook.model.constants.TransactionConstant;
import com.future.booklook.model.constants.TransactionDetailConstant;
import com.future.booklook.model.entity.properties.ProductConfirm;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = TransactionDetailConstant.TABLE_NAME)
public class TransactionDetail {
    @Id
    @Column(name = TransactionDetailConstant.TRANSACTION_DETAIL_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String transactionDetailId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = TransactionDetailConstant.TRANSACTION_FK, referencedColumnName = TransactionConstant.TRANSACTION_ID)
    private Transaction transaction;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TransactionDetailConstant.PRODUCT_FK, referencedColumnName = ProductConstant.PRODUCT_ID)
    private Product product;

    @Column(name = TransactionDetailConstant.MARKET_ID)
    private String marketId;

    @Enumerated(EnumType.STRING)
    @Column(name = TransactionDetailConstant.PRODUCT_CONFIRM)
    private ProductConfirm productConfirm;

    @Column(name = TransactionDetailConstant.CREATED_AT)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = TransactionDetailConstant.UPDATED_AT)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public TransactionDetail(Transaction transaction, Product product, String marketId) {
        this.transaction = transaction;
        this.product = product;
        this.marketId = marketId;
        this.productConfirm = ProductConfirm.UNCONFIRMED;
    }

    public TransactionDetail() {
    }

    public String getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(String transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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

    public ProductConfirm getProductConfirm() {
        return productConfirm;
    }

    public void setProductConfirm(ProductConfirm productConfirm) {
        this.productConfirm = productConfirm;
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
}
