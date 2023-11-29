package com.example.demo.pay;

import com.example.demo.pay.application.VatCalculator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Entity
@Setter
@NoArgsConstructor
@ToString
@Table(indexes = @Index(name = "idx_pay_storeId", columnList = "storeId, createdDate"))
public class Pay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalAmount;
    private Integer vatAmount;
    private String senderId;
    private String storeId;
    @Enumerated(EnumType.STRING)
    private PayType payType;
    private LocalDate createdDate;
    private LocalTime createdTime;

    public Pay(int unit) {
        this.totalAmount = PayAmountProvider.execute(unit);
        this.vatAmount = VatCalculator.execute(totalAmount);
        this.senderId = UUID.randomUUID().toString();
        this.storeId = IdentifyProvider.generateStringId(500);
        this.createdDate = CreateTimeProvider.createLocalDate();
        this.createdTime = CreateTimeProvider.createLocalTime();
        this.payType = PayTypeProvider.provide();
    }

    public Pay(Long id, Integer totalAmount, Integer vatAmount, String senderId, String storeId, PayType payType, LocalDate createdDate, LocalTime createdTime) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.vatAmount = vatAmount;
        this.senderId = senderId;
        this.storeId = storeId;
        this.payType = payType;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public Integer vatDeductedAmount() {
        return this.totalAmount - this.vatAmount;
    }
}
