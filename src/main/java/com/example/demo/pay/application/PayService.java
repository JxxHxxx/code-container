package com.example.demo.pay.application;

import com.example.demo.message.domain.QMessage;
import com.example.demo.message.domain.Requester;
import com.example.demo.message.domain.TaskType;
import com.example.demo.message.infra.QMessageRepository;
import com.example.demo.pay.domain.*;
import com.example.demo.pay.dto.PayForm;
import com.example.demo.pay.dto.PayServiceResponse;
import com.example.demo.pay.dto.RefundServiceResponse;
import com.example.demo.pay.infra.PayHistoryRepository;
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

import static com.example.demo.message.domain.TaskType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final QMessageRepository qMessageRepository;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            10,10,60, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    @Transactional
    public PayServiceResponse save(PayForm payForm) {
        Pay pay = new Pay();
        Pay savedPay = payRepository.save(pay);
        payHistoryRepository.save(new PayHistory(pay.getPayStatus(), pay));

        return new PayServiceResponse(savedPay.getPayAmount(), savedPay.getPayerId(), savedPay.getStoreId());
    }

    @Transactional
    public void saveAll(int iteration) {
        List<Pay> pays = new ArrayList<>();
        List<PayHistory> payHistories = new ArrayList<>();

        createPayEntity(iteration, pays, payHistories);

        payRepository.saveAll(pays);
        payHistoryRepository.saveAll(payHistories);
    }

    @Transactional
    public RefundServiceResponse tryRefund(Long payId, int orderStatus) {
        Pay pay = payRepository.findById(payId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 결제 건입니다."));
        boolean refundIsSuccessful = pay.refund(orderStatus);

        if (refundIsSuccessful) {
            PayHistory payHistory = new PayHistory(pay.getPayStatus(), pay);
            payHistoryRepository.save(payHistory);

            Requester requester = new Requester(pay.getPayerId());
            QMessage qMessage = QMessage.sentToOrderService(OC01, requester);
            qMessageRepository.save(qMessage);
        }

        return new RefundServiceResponse(pay.getId(), pay.getPayerId(), pay.getOrderNo());
    }

    @Transactional
    public Future asyncSaveAll(int iteration) {
        return executorService.submit(() -> {
            log.info("currentThread {}", Thread.currentThread().getName());
            try {
                List<Pay> pays = new ArrayList<>();
                List<PayHistory> payHistories = new ArrayList<>();
                createPayEntity(iteration, pays, payHistories);

                payRepository.saveAll(pays);
                payHistoryRepository.saveAll(payHistories);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createPayEntity(int iteration, List<Pay> pays, List<PayHistory> payHistories) {
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
                    PayStatus.PAYMENT_COMPLETED);

            Pay pay = new Pay(orderInformation, payInformation,
                    DateTimeGenerator.createLocalDate(), DateTimeGenerator.createLocalTime());
            pays.add(pay);

            PayHistory payHistory = new PayHistory(pay.getPayStatus(), pay);
            payHistories.add(payHistory);
        }
    }
}
