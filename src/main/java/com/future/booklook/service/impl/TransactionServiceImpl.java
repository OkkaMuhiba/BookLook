package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.TransactionRepository;
import com.future.booklook.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Set<Transaction> findAllTransactionByUser(User user){
        return transactionRepository.findAllByUser(user);
    }

    public Transaction findByTransactionId(String transactionId){
        return transactionRepository.findByTransactionId(transactionId);
    }
}
