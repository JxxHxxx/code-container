package com.example.demo.batch.job.processor;

import com.example.demo.pay.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class PaySummaryItemProcessorTest {

    PaySummaryItemProcessor paySummaryItemProcessor;

    @BeforeEach
    void beforeEach() {
        log.info("call beforeEach ");
        paySummaryItemProcessor = new PaySummaryItemProcessor();
    }

    @DisplayName("  결제 상태(payStatus)가 결제 완료(PAYMENT_COMPLETED)가 아닐 경우" +
            "       UnableToProcessException 예외를 던지며" +
            "       해당 Pay 객체 정보가 skippedItems 에 담긴다. (key : pay.id value :pay.payStatus) " +

            "       GIVEN : EnumSource 에 입력된 PayStatus 상태를 가진 Pay 인스턴스 생성 " +
            "       WHEN : process() 를 호출한다. " +
            "       THEN : UnableToProcessException 예외 throw 검증, skippedItems 검증")
    @ParameterizedTest
    @EnumSource(names = {"REQUEST_PAYMENT", "PAYMENT_PROGRESS", "PAYMENT_FAIL", "REFUND_COMPLETED"})
    void process_case_pay_status_is_not_payment_completed(PayStatus payStatus) throws Exception {
        // given
        Long payId = 100l;
        Pay pay = Pay.builder()
                .id(payId)
                .orderInformation(new OrderInformation("441", 52000, "135"))
                .payInformation(new PayInformation("A31", 52000, PayType.CARD, payStatus))
                .createdTime(LocalTime.now())
                .createdDate(LocalDate.now())
                .build();

        //when - then
        assertThatThrownBy(() -> paySummaryItemProcessor.process(pay))
                .isInstanceOf(UnableToProcessException.class)
                .hasMessage("IS NOT PAYMENT_COMPLETED STATUS : PAY ITEM STATUS IS " + payStatus.name());

        Map<Long, String> skippedItems = paySummaryItemProcessor.getSkippedItems();
        //then
        assertThat(skippedItems.get(payId)).isEqualTo(payStatus.name());
    }

    @DisplayName("  결제 상태(payStatus)가 결제 완료(PAYMENT_COMPLETED)일 경우" +
            "       어떠한 예외도 발생하지 않으며 " +
            "       skippedItems 은 비어있는 상태이다." +

            "       GIVEN : PAYMENT_COMPLETED 상태의 Pay 객체 생성 " +
            "       WHEN : process() 를 호출한다. " +
            "       THEN : 예외 미발생 및 skippedItems 비어있는지 체크")
    @Test
    void process_case_pay_status_is_payment_completed() {
        //given
        PayStatus payStatus = PayStatus.PAYMENT_COMPLETED;

        Pay pay = Pay.builder()
                .id(200l)
                .orderInformation(new OrderInformation("441", 52000, "135"))
                .payInformation(new PayInformation("A31", 52000, PayType.CARD, payStatus))
                .createdTime(LocalTime.now())
                .createdDate(LocalDate.now())
                .build();

        //when - then
        assertThatCode(() -> paySummaryItemProcessor.process(pay))
                        .doesNotThrowAnyException();

        //then
        Map<Long, String> skippedItems = paySummaryItemProcessor.getSkippedItems();
        assertThat(skippedItems).isEmpty();

    }

}