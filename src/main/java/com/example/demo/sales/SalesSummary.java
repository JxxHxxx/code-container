package com.example.demo.sales;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class SalesSummary {

    @Id @GeneratedValue
    private Long id;

    private String storeId;
    private Integer amount;
    private LocalDate createDate;
    private String createBy;

    public SalesSummary(String storeId, Integer amount, String createBy) {
        this.storeId = storeId;
        this.amount = amount;
        this.createBy = createBy;
        this.createDate = LocalDate.now();
    }
}
