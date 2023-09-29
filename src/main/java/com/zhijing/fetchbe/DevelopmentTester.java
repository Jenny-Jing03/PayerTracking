package com.zhijing.fetchbe;

import com.zhijing.fetchbe.model.PayerInfo;
import com.zhijing.fetchbe.model.SpendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;

public class DevelopmentTester implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevelopmentTester.class);
    private final PaymentService paymentService;

    public DevelopmentTester(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // add

        // spend
        //String expectation = "{\"payer\": \"DANNON\", \"points\": -100}, {\"payer\": \"UNILEVER\", \"points\": -200}, {\"payer\": \"MILLER COORS\", \"points\": -4700},";
        List<PayerInfo> spendResponse = paymentService.spendPoints(5000L);
        logger.info("spend response", spendResponse);
    }
}
