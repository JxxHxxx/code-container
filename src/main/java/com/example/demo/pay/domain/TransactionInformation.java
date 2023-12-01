package com.example.demo.pay.domain;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public class TransactionInformation {

    // 결제 번호
    private String transactionId;
    // 결제자
    private String payer;
    // 거래 일자
    private LocalDate createdDate;
    // 거래 시간
    private LocalTime createdTime;
}
