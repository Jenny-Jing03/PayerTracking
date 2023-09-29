package com.zhijing.fetchbe.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("payers")
public record PayerEntity(
        @Id Long id,
        String name) {

}
