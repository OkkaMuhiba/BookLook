package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.TransactionDetail;
import com.future.booklook.repository.ProductRepository;
import com.future.booklook.repository.TransactionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TransactionDetailServiceImpl {
    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    public TransactionDetail save(TransactionDetail transactionDetail){
        return transactionDetailRepository.save(transactionDetail);
    }

    public Boolean checkIfProductAlreadyExistInTransaction(Transaction transaction, Product product){
        return transactionDetailRepository.existsByTransactionAndProduct(transaction, product);
    }

    public Set<TransactionDetail> findAllTransactionDetailFromMarket(Market market){
        return transactionDetailRepository.findAllTransactionDetailByMarket(market.getMarketId());
    }

    public Set<TransactionDetail> findAllByTransaction(Transaction transaction){
        return transactionDetailRepository.findAllByTransaction(transaction);
    }

    public TransactionDetail findByTransactionDetailId(String transactionDetailId){
        return transactionDetailRepository.findByTransactionDetailId(transactionDetailId);
    }

    public Boolean existsByTransactionDetailId(String transactionDetailId){
        return transactionDetailRepository.existsByTransactionDetailId(transactionDetailId);
    }

    public Product findProductByTransactionDetail(TransactionDetail transactionDetail){
        Set<TransactionDetail> transactionDetails = new HashSet<>();
        transactionDetails.add(transactionDetail);
        return productRepository.findByTransactionDetails(transactionDetails);
    }
}
