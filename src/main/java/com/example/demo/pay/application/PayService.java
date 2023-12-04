package com.example.demo.pay.application;

import com.example.demo.pay.domain.*;
import com.example.demo.pay.dto.PayForm;
import com.example.demo.pay.dto.PayServiceResponse;
import com.example.demo.pay.infra.PayRepository;
import com.example.demo.pay.support.AmountGenerator;
import com.example.demo.pay.support.DateTimeGenerator;
import com.example.demo.pay.support.PayTypeProvider;
import com.example.demo.pay.support.StringNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            10,10,60, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    @Transactional
    public PayServiceResponse save(PayForm payForm) {
        Pay pay = new Pay();
        Pay savedPay = payRepository.save(pay);

        return new PayServiceResponse(savedPay.getPayAmount(), savedPay.getPayerId(), savedPay.getStoreId());
    }

    @Transactional
    public void saveAll(int iteration) {
        List<Pay> pays = new ArrayList<>();

        createPayEntity(iteration, pays);

        payRepository.saveAll(pays);
    }

    @Transactional
    public Future asyncSaveAll(int iteration) {
        return executorService.submit(() -> {
            log.info("currentThread {}", Thread.currentThread().getName());
            try {
                List<Pay> pays = new ArrayList<>();

                createPayEntity(iteration, pays);

                payRepository.saveAll(pays);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private  void createPayEntity(int iteration, List<Pay> pays) {
        for (int i = 0; i < iteration; i++) {
            int orderAmount = AmountGenerator.execute();
            String orderNo = UUID.randomUUID().toString().substring(0, 12);

            OrderInformation orderInformation = new OrderInformation(
                    orderNo,
                    orderAmount,
                    StringNumberGenerator.generateStringId(1000));

            int payAmount = AmountGenerator.payAmount(orderAmount);

            PayInformation payInformation = new PayInformation(
                    StringNumberGenerator.generateStringId(3000),
                    payAmount,
                    VatCalculator.execute(payAmount),
                    PayTypeProvider.provide(),
                    PayStatus.COMPLETED);

            Pay pay = new Pay(orderInformation, payInformation,
                    DateTimeGenerator.createLocalDate(), DateTimeGenerator.createLocalTime());
            pays.add(pay);
        }
    }
}
