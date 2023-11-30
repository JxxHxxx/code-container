package com.example.demo.sales;

import com.example.demo.sales.dto.PayDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.demo.sales.SalesSummaryConst.*;

@Getter(AccessLevel.PROTECTED)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesSummary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeId;
    private Integer dailyTotalSales;
    private Integer dailyVatDeductedSales;
    private Integer dailyTotalTransaction;
    private LocalDate salesDate;
    private LocalDateTime createTime;
    private boolean neverChangedFlag;

    @Enumerated(EnumType.STRING)
    private SystemType createSystem;

    public SalesSummary(String storeId, Integer dailyTotalSales, Integer dailyVatDeductedSales, Integer dailyTotalTransaction,
                        LocalDate salesDate, SystemType createSystem) {
        this.storeId = storeId;
        this.dailyTotalSales = dailyTotalSales;
        this.dailyVatDeductedSales = dailyVatDeductedSales;
        this.dailyTotalTransaction = dailyTotalTransaction;
        this.salesDate = salesDate;
        this.createTime = LocalDateTime.now();
        this.createSystem = createSystem;
        this.neverChangedFlag = true;
    }

    public static SalesSummary constructorStoreIdIsNotExistCase(PayDto payDto, SystemType createSystem) {
        SalesSummary salesSummary = new SalesSummary(
                payDto.getStoreId(),
                payDto.getTotalAmount(),
                payDto.getTotalAmount() - payDto.getVatAmount(),
                TOTAL_TRANSACTION_INITIAL_VALUE,
                payDto.getCreatedDate(),
                createSystem);
        
        salesSummary.setNeverChangedFlagToFalse();
        return salesSummary;
    }
    
    private SalesSummary setNeverChangedFlagToFalse() {
        this.neverChangedFlag = false;
        return this;
    }

    public void reflectPayInformation(PayDto payDto) {
        if (this.neverChangedFlag) {
            throw new IllegalStateException("거래 정보를 갱신할 수 없습니다. 해당 엔티티에 대한 기존 정보가 존재하지 않습니다.");
        }

        this.dailyTotalSales += payDto.getTotalAmount();
        this.dailyVatDeductedSales += payDto.getTotalAmount() - payDto.getVatAmount();
        this.dailyTotalTransaction += 1;
    }
}
