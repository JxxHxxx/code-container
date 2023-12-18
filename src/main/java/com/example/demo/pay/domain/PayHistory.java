package com.example.demo.pay.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class PayHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_history_id")
    private Long id;

    private LocalDateTime historyTime;
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;
    @ManyToOne
    private Pay pay;

    public PayHistory(PayStatus payStatus, Pay pay) {
        this.historyTime = LocalDateTime.now();
        this.payStatus = payStatus;
        this.pay = pay;
    }
}
