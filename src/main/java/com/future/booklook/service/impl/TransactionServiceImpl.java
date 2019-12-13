package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Transaction;
import com.future.booklook.repository.TransactionRepository;
import com.future.booklook.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
