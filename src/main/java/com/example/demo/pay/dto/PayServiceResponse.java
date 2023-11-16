package com.example.demo.pay.dto;

import lombok.Getter;

@Getter
public class PayServiceResponse {

    private final Integer amount;
    private final String senderId;
    private final String receiverId;

    public PayServiceResponse(Integer amount, String senderId, String receiverId) {
        this.amount = amount;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
