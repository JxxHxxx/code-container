package com.example.demo.pay.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class OrderInformation {

    @Column(name = "order_no")
    private String orderNo;
    // 주문금색
    @Column(name = "order_amount")
    private Integer orderAmount;
    @Column(name = "store_id")
    private String storeId;

    public OrderInformation(String orderNo, Integer orderAmount, String storeId) {
        this.orderNo = orderNo;
        this.orderAmount = orderAmount;
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "OrderInformation{" +
                "orderNo='" + orderNo + '\'' +
                ", orderAmount=" + orderAmount +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
