package com.example.demo.pay.application;

import com.example.demo.pay.Pay;
import com.example.demo.pay.dto.PayForm;
import com.example.demo.pay.dto.PayServiceResponse;
import com.example.demo.pay.infra.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        Pay pay = new Pay(payForm.getAmount());
        Pay savedPay = payRepository.save(pay);

        return new PayServiceResponse(savedPay.getTotalAmount(), pay.getSenderId(), pay.getStoreId());
    }

    @Transactional
    public void saveAll(int iteration) {
        List<Pay> pays = new ArrayList<>();

        for (int i = 0; i < iteration; i++) {
            Pay pay = new Pay(1000);
            pays.add(pay);
        }

        payRepository.saveAll(pays);
    }

    @Transactional
    public Future asyncSaveAll(int iteration) {
        return executorService.submit(() -> {
            log.info("currentThread {}", Thread.currentThread().getName());
            try {
                List<Pay> pays = new ArrayList<>();

                for (int i = 0; i < iteration; i++) {
                    Pay pay = new Pay(1000);
                    pays.add(pay);
                }

                payRepository.saveAll(pays);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
