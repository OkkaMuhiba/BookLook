package com.future.booklook.repository;

import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
    Boolean existsByTransactionAndProduct(Transaction transaction, Product product);

    Set<TransactionDetail> findAllByTransaction(Transaction transaction);

    @Query(value = "select t.* from transaction_details t join products p on t.product_fk = p.product_id " +
            "join markets m on p.market_fk = m.market_id where m.market_id = :marketId", nativeQuery = true)
    Set<TransactionDetail> findAllTransactionDetailByMarket(@Param("marketId") String marketId);

    TransactionDetail findByTransactionDetailId(String transactionDetailId);
}
