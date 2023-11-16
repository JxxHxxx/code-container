package com.example.demo.pay;

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
    private Integer amount;
    private String senderId;
    private String receiverId;

    public Pay(Integer amount) {
        this.amount = amount;
        this.senderId = UUID.randomUUID().toString();
        this.receiverId = IdentifyProvider.generate(10);
    }
}
