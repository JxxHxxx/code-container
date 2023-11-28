package com.example.demo.pay.batch;


import com.example.demo.sales.SystemType;
import com.example.demo.sales.application.SalesSummaryService;
import com.example.demo.sales.dto.SalesSummaryForm;
import com.example.demo.pay.Pay;
import com.example.demo.pay.infra.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SalesSummaryJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PayRepository payRepository;
    private final SalesSummaryService salesSummaryService;

    @Bean(name = "sales.summary.job")
    public Job job1() {
        return jobBuilderFactory.get("sales-summary-job")
                .start(step1())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = "sales.summary.step1")
    public Step step1() {
        return stepBuilderFactory.get("sales-summary-step1")
                .tasklet(makeDailySalesSummary())
                .build();
    }

    @Bean
    public Tasklet makeDailySalesSummary() {
        return (contribution, chunkContext) -> {
            List<SalesSummaryForm> salesSummary = new ArrayList<>(); // salesSummary 를 담을 공간

            List<String> storeIds = payRepository.findAllStore();

            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            LocalDate requestDate = LocalDate.parse(String.valueOf(jobParameters.get("requestDate")));

            storeIds.forEach(storeId -> {
                List<Pay> oneStorePays = payRepository.findOneDayPays(storeId, requestDate);
                int dailyTotalSales = calculateDailyTotalSales(oneStorePays);
                int dailyVatDeductedSales = calculateDailyVatDeductedSales(oneStorePays);

                salesSummary.add(new SalesSummaryForm(
                        storeId,
                        dailyTotalSales,
                        dailyVatDeductedSales,
                        oneStorePays.size(),
                        requestDate,
                        SystemType.BATCH));
            });
            salesSummaryService.saveAll(salesSummary);
            log.info("sales.summary.job request date : {}", requestDate);
            log.info("sales.summary.job BATCH RESULT : SUCCESS ");

            return RepeatStatus.FINISHED;
        };
    }

    private int calculateDailyTotalSales(List<Pay> paysOfOneReceiver) {
        return paysOfOneReceiver.stream()
                .mapToInt(Pay::getTotalAmount)
                .sum();
    }

    private int calculateDailyVatDeductedSales(List<Pay> paysOfOneReceiver) {
        return paysOfOneReceiver.stream()
                .mapToInt(pay -> pay.getTotalAmount() - pay.getVatAmount())
                .sum();
    }
}
