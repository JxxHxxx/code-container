package com.example.demo.pay.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter(AccessLevel.PROTECTED)
@Embeddable
@NoArgsConstructor
public class PayInformation {

    @Column(name = "payer_id")
    private String payerId;
    @Column(name = "pay_amount")
    private Integer payAmount;
    @Column(name = "vat_amount")
    private Integer vatAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    private PayType payType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private PayStatus payStatus;

    public PayInformation(String payerId, Integer payAmount, Integer vatAmount, PayType payType, PayStatus payStatus) {
        this.payerId = payerId;
        this.payAmount = payAmount;
        this.vatAmount = vatAmount;
        this.payType = payType;
        this.payStatus = payStatus;
    }

    protected void changePayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return "PayInformation{" +
                "payerId='" + payerId + '\'' +
                ", payAmount=" + payAmount +
                ", vatAmount=" + vatAmount +
                ", payType=" + payType +
                ", payStatus=" + payStatus +
                '}';
    }
}
