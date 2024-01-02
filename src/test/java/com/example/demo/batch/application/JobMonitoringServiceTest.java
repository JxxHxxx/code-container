package com.example.demo.batch.application;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.batch.core.BatchStatus;


@Slf4j
class JobMonitoringServiceTest {

    @ParameterizedTest
    @EnumSource(value = BatchStatus.class)
    void valueTest(BatchStatus batchStatus) {
        boolean unsuccessful = batchStatus.isUnsuccessful();
        log.info("{} is unsuccessful ? {}", batchStatus, unsuccessful);
    }
}