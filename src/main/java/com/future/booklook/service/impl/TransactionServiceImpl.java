package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.ProductRepository;
import com.future.booklook.repository.TransactionRepository;
import com.future.booklook.repository.UserRepository;
import com.future.booklook.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Set<Transaction> findAllTransactionByUser(User user){
        return transactionRepository.findAllByUser(user);
    }

    public Transaction findByTransactionId(String transactionId){
        return transactionRepository.findByTransactionId(transactionId);
    }

    public Boolean existsByTransactionId(String transactionId){
        return transactionRepository.existsByTransactionId(transactionId);
    }

    public Set<Transaction> findAllTransactionByMarket(Market market){
        String marketId = market.getMarketId();
        return transactionRepository.findAllTransactionWithProductInMarket(marketId);
    }

    public User findUserFromTransaction(Transaction transaction){
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(transaction);
        return userRepository.findByTransactions(transactions);
    }
}
