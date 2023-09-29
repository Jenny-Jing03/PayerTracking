package com.zhijing.fetchbe.db;

import com.zhijing.fetchbe.db.entity.BalanceEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BalanceRepository extends ListCrudRepository<BalanceEntity, Long> {


    BalanceEntity findBalanceByPayerId(Long payerId);

    @Modifying
    @Query("UPDATE balance SET balance= :balance WHERE payer_id= :payerId")
    void updateBalanceById(Long payerId, Long balance);

    @Query("SELECT SUM(balance) FROM balance")
    Long getBalanceSum();

    List<BalanceEntity> findAll();
}
