package com.example.demo.pay.batch;


import com.example.demo.sales.DailySaleForm;
import com.example.demo.pay.Pay;
import com.example.demo.pay.infra.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CountSalesJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PayRepository payRepository;

    @Bean(name = "count.sales.job")
    public Job job1() {
        return jobBuilderFactory.get("count-sales-job")
                .start(step1())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = "count-sales.step1")
    public Step step1() {
        return stepBuilderFactory.get("count-sales-step1")
                .tasklet(someTasklet())
                .build();
    }

    @Bean
    public Tasklet someTasklet() {
        return (contribution, chunkContext) -> {
            List<String> receivers = payRepository.findAllReceiver();

            receivers.forEach(receiver -> {
                List<Pay> paysOfOneReceiver = payRepository.findByReceiverId(receiver);
                int sales = sumPayAmount(paysOfOneReceiver);
                log.info("receiver : {} sales : {}",receiver, sales);
            });

            List<DailySaleForm> dailySales = new ArrayList<>();

            return RepeatStatus.FINISHED;
        };
    }

    private int sumPayAmount(List<Pay> paysOfOneReceiver) {
        int sales = paysOfOneReceiver.stream()
                .mapToInt(Pay::getAmount)
                .sum();
        return sales;
    }
}
