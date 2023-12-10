package com.example.demo.pay.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
@ToString(exclude = "id")
@Table(indexes = @Index(name = "idx_pay_storeId", columnList = "store_id, created_date"))
public class Pay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long id;
    // 주문 금액
    @Embedded
    private OrderInformation orderInformation;
    @Embedded
    private PayInformation payInformation;
    @Column(name = "created_date")
    private LocalDate createdDate;
    @Column(name = "created_time")
    private LocalTime createdTime;

    public Pay(OrderInformation orderInformation, PayInformation payInformation, LocalDate createdDate, LocalTime createdTime) {
        this.orderInformation = orderInformation;
        this.payInformation = payInformation;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public Integer vatDeductedAmount() {
        return this.payInformation.getPayAmount() - this.payInformation.getVatAmount();
    }

    public String getOrderNo() {
        return this.orderInformation.getOrderNo();
    }

    public Integer getOrderAmount() {
        return this.orderInformation.getOrderAmount();
    }

    public String getStoreId() {
        return this.orderInformation.getStoreId();
    }


    public Integer getPayAmount() {
        return this.payInformation.getPayAmount();
    }

    public Integer getVatAmount() {
        return this.payInformation.getVatAmount();
    }

    public PayStatus getPayStatus() {
        return this.payInformation.getPayStatus();
    }

    public String  getPayerId() {
        return this.payInformation.getPayerId();
    }

    public PayType getPayType() {
        return this.payInformation.getPayType();
    }
}
