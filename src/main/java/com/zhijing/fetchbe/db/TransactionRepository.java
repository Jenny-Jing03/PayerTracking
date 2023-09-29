package com.zhijing.fetchbe.db;

import com.zhijing.fetchbe.db.entity.PayerEntity;
import com.zhijing.fetchbe.db.entity.TransactionEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionRepository  extends ListCrudRepository<TransactionEntity, Long> {

    @Modifying
    @Query("UPDATE transactions SET status= :status WHERE payer_id= :payerId AND timestamp= :timestamp")
    void deleteTransaction(Long payerId, String payer, Timestamp timestamp, Integer status);

    @Modifying
    @Query("UPDATE transactions SET points= :point WHERE payer_id= :payerId AND timestamp= :timestamp")
    void updatePointsByIdAndTimestamp(Long payerId, Long point, Timestamp timestamp);

    @Query("SELECT payer_id,  payer, points, timestamp FROM transactions WHERE status AND payer_id= :payerId ORDER BY timestamp ASC ")
    List<TransactionEntity> findByPayerId(Long payerId);

    @Query("SELECT payer_id,  payer, points, timestamp FROM transactions WHERE status ORDER BY timestamp ASC ")
    List<TransactionEntity> findAllTransaction();
}
