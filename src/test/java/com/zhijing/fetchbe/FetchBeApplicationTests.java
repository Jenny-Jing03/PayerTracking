package com.zhijing.fetchbe;

import com.zhijing.fetchbe.db.BalanceRepository;
import com.zhijing.fetchbe.db.PayerRepository;
import com.zhijing.fetchbe.db.TransactionRepository;
import com.zhijing.fetchbe.db.entity.TransactionEntity;
import com.zhijing.fetchbe.model.PayerInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FetchBeApplicationTests {
    @Mock
    private PayerRepository payerRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private PaymentService paymentService;
    @Mock
    private BalanceRepository balanceRepository;

    @BeforeEach
    public void setUp(){
        paymentService = new PaymentService(payerRepository, transactionRepository, balanceRepository);
    }

    @BeforeEach
    public void addTester() {
        TransactionEntity transactionEntity1 = new TransactionEntity(null,1L, "DANNON", 300L, new Timestamp(2022, 10, 31, 10, 00,00,00));
        TransactionEntity transactionEntity2 = new TransactionEntity(null,2L,"UNILEVER", 200L, new Timestamp(2022, 10, 31, 11, 00,00,00));
        TransactionEntity transactionEntity3 = new TransactionEntity(null, 1L,"DANNON", -200L, new Timestamp(2022, 10, 31, 15, 00,00,00));
        TransactionEntity transactionEntity4 = new TransactionEntity(null, 3L,"MILLER COORS", 10000L, new Timestamp(2022, 11, 01, 14, 00,00,00));
        TransactionEntity transactionEntity5 = new TransactionEntity(null,1L,"DANNON", 1000L, new Timestamp(2022, 11, 02, 14, 00,00,00));
        // add
    }

    @Test
    public void spendTester(){
        // spend
        List<PayerInfo> expectation = new ArrayList<>();
        expectation.add(new PayerInfo("DANNON", -100L));
        expectation.add(new PayerInfo("UNILEVER", -200L));
        expectation.add(new PayerInfo("MILLER COORS", -4700L));
        assert(paymentService.spendPoints(5000L).equals(expectation));
    }

}
