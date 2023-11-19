package com.example.demo.pay.application;

import com.example.demo.pay.Pay;
import com.example.demo.pay.dto.PayForm;
import com.example.demo.pay.dto.PayServiceResponse;
import com.example.demo.pay.infra.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;

    @Transactional
    public PayServiceResponse save(PayForm payForm) {
        Pay pay = new Pay(payForm.getAmount());
        Pay savedPay = payRepository.save(pay);

        return new PayServiceResponse(savedPay.getTotalAmount(), pay.getSenderId(), pay.getStoreId());
    }

    @Transactional
    public void saveAll(List<PayForm> payForms) {
        List<Pay> pays = payForms.stream()
                .map(payForm -> new Pay(payForm.getAmount()))
                .collect(Collectors.toList());
        payRepository.saveAll(pays);
    }
}
