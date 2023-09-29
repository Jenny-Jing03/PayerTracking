package com.zhijing.fetchbe.model;

import com.zhijing.fetchbe.db.entity.PayerEntity;

import java.util.List;

public record BalanceResponse(List<PayerInfo> data) {
}
