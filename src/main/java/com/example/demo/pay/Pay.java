package com.example.demo.pay;

import com.example.demo.pay.application.VatCalculator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_pay_storeId", columnList = "storeId"))
public class Pay {

    @Id @GeneratedValue
    private Long id;
    private Integer totalAmount;
    private Integer vatAmount;
    private String senderId;
    private String storeId;
    @Enumerated(EnumType.STRING)
    private PayType payType;
    private LocalDateTime createTime;


    public Pay(Integer totalAmount) {
        this.vatAmount = VatCalculator.execute(totalAmount);
        this.totalAmount = totalAmount;
        this.senderId = UUID.randomUUID().toString();
        this.storeId = IdentifyProvider.generateStringId(500);
        this.createTime = CreateTimeProvider.provide();
        this.payType = PayTypeProvider.provide();
    }
}
