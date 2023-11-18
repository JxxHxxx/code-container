package com.example.demo.pay;

import com.example.demo.pay.application.VatCalculator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_pay_receiver", columnList = "receiverId"))
public class Pay {

    @Id @GeneratedValue
    private Long id;
    private Integer totalAmount;
    private Integer vatAmount;
    private String senderId;
    private String receiverId;

    public Pay(Integer totalAmount) {
        this.vatAmount = VatCalculator.execute(totalAmount);
        this.totalAmount = totalAmount;
        this.senderId = UUID.randomUUID().toString();
        this.receiverId = IdentifyProvider.generate(10);
    }
}
