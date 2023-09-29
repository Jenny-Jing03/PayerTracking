package com.zhijing.fetchbe;

import com.zhijing.fetchbe.db.BalanceRepository;
import com.zhijing.fetchbe.db.PayerRepository;
import com.zhijing.fetchbe.db.TransactionRepository;
import com.zhijing.fetchbe.db.entity.BalanceEntity;
import com.zhijing.fetchbe.db.entity.PayerEntity;
import com.zhijing.fetchbe.db.entity.TransactionEntity;
import com.zhijing.fetchbe.model.PayerInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {
    final PayerRepository payerRepository;
    final TransactionRepository transactionRepository;
    final BalanceRepository balanceRepository;

    public PaymentService(PayerRepository payerRepository, TransactionRepository transactionRepository, BalanceRepository balanceRepository) {
        this.payerRepository = payerRepository;
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
    }

    public void addTransaction(String payer, Long points, String timestamp) throws ParseException {
        List<PayerEntity> payers = payerRepository.findPayerByName(payer);

        // if payer is a new payer
        if(payers.size() == 0){
            PayerEntity payerEntity = new PayerEntity(null, payer);
            payerRepository.save(payerEntity);
            payers = payerRepository.findPayerByName(payer);
            balanceRepository.save(new BalanceEntity(null, payers.get(0).id(), points));
        }else{
            if(points < 0){
                spendPointsWithPayerId(payers.get(0).id(), points);
                return;
            }
            balanceRepository.updateBalanceById(payers.get(0).id(), balanceRepository.findBalanceByPayerId(payers.get(0).id()).balance() + points);
        }
        Date timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(timestamp);
        transactionRepository.save(new TransactionEntity(null, payers.get(0).id(), payer, points, new Timestamp( timeStamp.getTime()), 1 )); // I assume all the user name are unique

    }

    public void spendPointsWithPayerId(Long payerId, Long points){
        points = points * -1;
        Long b = balanceRepository.findBalanceByPayerId(payerId).balance();
        if(balanceRepository.findBalanceByPayerId(payerId).balance() < points){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't have enough points");
        }

        List<TransactionEntity> transactions = transactionRepository.findByPayerId(payerId);
        int index = 0;
        while(points > 0 && index < transactions.size()){
            TransactionEntity transactionInfo = transactions.get(index);

            String payer = transactionInfo.payer();
            Long point = transactionInfo.points();
            Timestamp timestamp = transactionInfo.timestamp();

            if(points >= point){
                points -= point;
                // update db
                transactionRepository.deleteTransaction(payerId, payer, timestamp, 0);
                balanceRepository.updateBalanceById(payerId, balanceRepository.findBalanceByPayerId(payerId).balance() - point);
            }else{
                point -= points;

                // update db
                transactionRepository.updatePointsByIdAndTimestamp(payerId, point, timestamp);
                balanceRepository.updateBalanceById(payerId, balanceRepository.findBalanceByPayerId(payerId).balance() - points);
                points = 0L;
            }
            index++;
        }
    }

    public List<PayerInfo> spendPoints(Long points){
        if(balanceRepository.getBalanceSum() < points){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't have enough points");
        }

        List<PayerInfo> response = new ArrayList<>();
        List<TransactionEntity> allTransaction = transactionRepository.findAllTransaction();
        int index = 0;
        while(points > 0 && index < allTransaction.size()){
            TransactionEntity transactionInfo = allTransaction.get(index);
            String payer = transactionInfo.payer();
            Long point = transactionInfo.points();

            Timestamp timestamp = transactionInfo.timestamp();
            Long payerId = transactionInfo.payerId();
            Long currentBalance = balanceRepository.findBalanceByPayerId(payerId).balance();

            if(points >= point){
                response.add(new PayerInfo(payer, -point));
                points -= point;

                // update db
                transactionRepository.deleteTransaction(payerId, payer, timestamp, 0);
                balanceRepository.updateBalanceById(payerId, currentBalance - point);
            }else{
                response.add(new PayerInfo(payer, -points));
                point -= points;
                points = 0L;

                // update db
                transactionRepository.updatePointsByIdAndTimestamp(payerId, point, timestamp);
                balanceRepository.updateBalanceById(payerId, point);
            }
            index++;
        }

        return response;
    }

    @Cacheable("balance")
    public List<PayerInfo> getBalance(){
        List<BalanceEntity> balances = balanceRepository.findAll();
        List<PayerInfo> payerInfos = new ArrayList<>();

        for(BalanceEntity b : balances){
            payerInfos.add(new PayerInfo(payerRepository.findPayerById(b.payerId()).name(), b.balance()));
        }

        return payerInfos;
    }
}