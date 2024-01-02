package com.example.demo.sales;

import com.example.demo.sales.dto.PayDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.demo.sales.SalesSummaryConst.*;

/**
 * 여기 고쳐야됨 - 총 주문 금액, 총 매출 나눠야됨
 */

@Getter(AccessLevel.PROTECTED)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesSummary {

    @Comment("매출 요약 레코드 식별자")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Comment("점포 식별자")
    @Column(name = "store_id")
    private String storeId;
    @Comment("일 총 매출액")
    @Column(name = "daily_total_sales")
    private Integer dailyTotalSales;
    @Comment("부가세 차감 일 총 매출액")
    @Column(name = "daily_vat_deducted_sales")
    private Integer dailyVatDeductedSales;
    @Comment("일 총 거래 수")
    @Column(name = "daily_total_transaction")
    private Integer dailyTotalTransaction;
    @Comment("매출 요약 정보의 기준 일자")
    @Column(name = "sales_date")
    private LocalDate salesDate;
    @Comment("레코드 생성 일시")
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Comment("레코드 생성 시스템")
    @Column(name = "create_system")
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
    }

    public static SalesSummary isStoresFirstOfTheDaySalesSummary(PayDto payDto) {
        SalesSummary salesSummary = new SalesSummary(
                payDto.getStoreId(),
                payDto.getPayAmount(),
                payDto.getPayAmount() - payDto.getVatAmount(),
                TOTAL_TRANSACTION_INITIAL_VALUE,
                payDto.getCreatedDate(), // 판매일자
                SystemType.BATCH);

        return salesSummary;
    }

    public void reflectPayInformation(PayDto payDto) {
        this.dailyTotalSales += payDto.getPayAmount();
        this.dailyVatDeductedSales += payDto.getPayAmount() - payDto.getVatAmount();
        this.dailyTotalTransaction += 1;
    }
}
