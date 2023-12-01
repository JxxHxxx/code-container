package com.example.demo.pay.domain;

import javax.persistence.Embeddable;

// 매출 : 결제 금액
// VAT 차감 매출 : 결제 금액 - VAT
@Embeddable
public class Amount {

    // 주문 금액
    private Integer order;
    // 할인 금액 (쿠폰)
    private Integer discount;
    // 결제 금액 : 주문 금액 - 할인 금액
    private Integer payment;
    // VAT : 결제 금액 10%
    private Integer vat;

}
