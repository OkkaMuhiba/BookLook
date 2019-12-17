package com.future.booklook.repository;

import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Set<Transaction> findAllByUser(User user);

    Transaction findByTransactionId(String transactionId);

    Boolean existsByTransactionId(String transactionId);
}
