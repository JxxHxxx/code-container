package com.example.demo.pay.dto;

import lombok.Getter;

@Getter
public class RefundServiceResponse {
    private final Long payId;
    private final String payerId;
    private final String orderNo;

    public RefundServiceResponse(Long payId, String payerId, String orderNo) {
        this.payId = payId;
        this.payerId = payerId;
        this.orderNo = orderNo;
    }
}
