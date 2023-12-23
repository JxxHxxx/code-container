package com.example.demo.pay.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.demo.pay.domain.PayStatus.PAYMENT_COMPLETED;

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

    @Builder
    public Pay(Long id, OrderInformation orderInformation, PayInformation payInformation, LocalDate createdDate, LocalTime createdTime) {
        this(orderInformation, payInformation, createdDate, createdTime);
        this.id = id;
    }

    public Pay(OrderInformation orderInformation, PayInformation payInformation, LocalDate createdDate, LocalTime createdTime) {
        this.orderInformation = orderInformation;
        this.payInformation = payInformation;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    // order-status 
    // 0 : 주문 완료 
    // 1 : 접수 
    // 2 : 제조 
    // 3 : 배송 
    // 4 : 배송 완료 
    public boolean refund(int orderStatus) {
        validateRefundablePayment(orderStatus);

        payInformation.changePayStatus(PayStatus.REFUND_COMPLETED);
        return true;
    }

    private  void validateRefundablePayment(int orderStatus) {
        if (isBeforeDelivery(orderStatus)) {
            throw new ImpossibleRefundException("환불이 불가능합니다. 사유 :" + orderStatus);
        }
        if (isNotPaymentCompleted()) {
            throw new ImpossibleRefundException("환불이 불가능합니다. 사유 :" + payInformation.getPayStatus().getDescription());
        }
    }

    public boolean isCompletedPayment() {
        return PAYMENT_COMPLETED.equals(getPayStatus());
    }

    private boolean isNotPaymentCompleted() {
        return !PAYMENT_COMPLETED.equals(payInformation.getPayStatus());
    }

    private boolean isBeforeDelivery(int orderStatus) {
        return orderStatus == 0 || orderStatus == 1 || orderStatus == 2 ;
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
