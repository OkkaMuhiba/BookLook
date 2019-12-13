package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.TransactionDetail;
import com.future.booklook.repository.TransactionDetailRepository;
import com.future.booklook.service.TransactionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionDetailServiceImpl implements TransactionDetailService {
    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    public TransactionDetail save(TransactionDetail transactionDetail){
        return transactionDetailRepository.save(transactionDetail);
    }

    public Boolean checkIfProductAlreadyExistInTransaction(Transaction transaction, Product product){
        return transactionDetailRepository.existsByTransactionAndProduct(transaction, product);
    }
}
