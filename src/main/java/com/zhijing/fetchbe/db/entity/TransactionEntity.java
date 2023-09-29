package com.zhijing.fetchbe.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;

@Table("transactions")
public record TransactionEntity(
        @Id Long id,
        @JsonProperty("payer_id") Long payerId,
        String payer,
        Long points,
        Timestamp timestamp,
        Integer status
        ) {
}
