package com.example.demo.pay.application;

import com.example.demo.pay.Pay;
import com.example.demo.pay.dto.PayRequest;
import com.example.demo.pay.dto.PayServiceResponse;
import com.example.demo.pay.infra.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;

    @Transactional
    public PayServiceResponse save(PayRequest payRequest) {
        Pay pay = new Pay(payRequest.getAmount());
        Pay savedPay = payRepository.save(pay);

        return new PayServiceResponse(savedPay.getTotalAmount(), pay.getSenderId(), pay.getReceiverId());
    }

}
