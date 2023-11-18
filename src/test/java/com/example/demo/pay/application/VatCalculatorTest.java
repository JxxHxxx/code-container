package com.example.demo.pay.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class VatCalculatorTest {

    @Test
    void 부가세_11프로를_검증한다() {
        Integer vatAmount = VatCalculator.execute(15000);

        Assertions.assertThat(vatAmount).isEqualTo(1364);
    }
}