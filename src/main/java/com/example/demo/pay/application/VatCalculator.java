package com.example.demo.pay.application;

import com.example.demo.pay.application.exception.NotValidBoundException;
import com.example.demo.pay.application.exception.PayExceptionMessage;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.demo.pay.application.exception.PayExceptionMessage.*;

@Slf4j
public class VatCalculator {

    private final static int VAT_RATE = 11;
    private final static int ROUNDING_SCALE = 0;

    /**
     * @param totalAmount : 총 결제 금액
     * @return 부가 세액
     */

    public static Integer execute(final Integer totalAmount) {
        return calculateVatAmount(totalAmount, VAT_RATE);
    }

    /**
     * 부가세가 11%가 아닐 경우 사용한다.
     */

    public static Integer execute(Integer totalAmount, Integer vatRate) {
        if (vatRate > 100 || vatRate < 0) {
            log.warn("vatRate 범위는 0 ~ 100 입니다. 전달받은 vatRate {}", vatRate);
            throw new NotValidBoundException(OUT_OF_BOUND);
        }
        return calculateVatAmount(totalAmount, vatRate);
    }

    private static int calculateVatAmount(Integer totalAmount, Integer vatRate) {
        return BigDecimal.valueOf(totalAmount / (double) vatRate)
                .setScale(ROUNDING_SCALE, RoundingMode.HALF_UP)
                .intValue();
    }

}
