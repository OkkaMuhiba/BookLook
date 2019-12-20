package com.future.booklook.repository;

import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Set<Transaction> findAllByUser(User user);

    Transaction findByTransactionId(String transactionId);

    Boolean existsByTransactionId(String transactionId);

    @Query(value = "select t.* from transactions t " +
            "join transaction_details d on d.transaction_fk = t.transaction_id " +
            "join products p on d.product_fk = p.product_id " +
            "join markets m on p.market_fk = m.market_id " +
            "where m.market_id = :marketId", nativeQuery = true)
    Set<Transaction> findAllTransactionWithProductInMarket(@Param("marketId") String marketId);
}
