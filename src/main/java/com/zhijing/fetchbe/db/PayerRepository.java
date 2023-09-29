package com.zhijing.fetchbe.db;

import com.zhijing.fetchbe.db.entity.PayerEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface PayerRepository extends ListCrudRepository<PayerEntity, Long> {

    List<PayerEntity> findPayerByName(String payer);

    PayerEntity findPayerById(Long id);
}
