package com.zhijing.fetchbe.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("balance")
public record BalanceEntity(
        @Id Long id,
        @JsonProperty("payer_id") Long payerId,
        Long balance) {
}
