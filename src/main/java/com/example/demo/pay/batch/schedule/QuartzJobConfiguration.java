package com.example.demo.pay.batch.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scheduledJob() {
        return this.jobBuilderFactory.get("scheduled.job")
                .incrementer(new RunIdIncrementer()) // 스케줄러로 만로만 실행되는 배치니까.
                .start(scheduledStep())
                .build();
    }

    @Bean
    public Step scheduledStep() {
        return this.stepBuilderFactory.get("scheduled.step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("TEST schedule step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
