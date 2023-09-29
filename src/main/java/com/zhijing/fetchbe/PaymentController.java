package com.zhijing.fetchbe;

import com.zhijing.fetchbe.db.entity.TransactionEntity;
import com.zhijing.fetchbe.model.BalanceResponse;
import com.zhijing.fetchbe.model.PayerInfo;
import com.zhijing.fetchbe.model.SpendResponse;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping ("/add")
    public void add(@RequestParam("payer") String payer, @RequestParam("points") Long points, @RequestParam("timestamp") String timestamp) throws ParseException {
        paymentService.addTransaction(payer, points, timestamp);
    }

    @RequestMapping("/spend")
    public List<PayerInfo> spend(@RequestParam("points") Long points){
        return paymentService.spendPoints(points);
    }

    @GetMapping("/balance")
    public List<PayerInfo> balance(){
        return paymentService.getBalance();
    }
}
