package com.example.demo.pay.batch.job;

import com.example.demo.pay.Pay;
import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.SystemType;
import com.example.demo.sales.infra.SalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaySummaryJobV2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final SalesSummaryRepository salesSummaryRepository;
    private static final int TOTAL_TRANSACTION_INITIAL_VALUE = 1;

    @Bean(name = "pay.summary.job.v2")
    public Job paySummaryJobV2() throws Exception {
        return jobBuilderFactory.get("pay.summary.job.v2")// 재시작시 잡 이름 찾음
                .start(paySummaryStepV2())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step paySummaryStepV2() throws Exception {
        return stepBuilderFactory.get("paySummaryStep")
                .<Pay, Pay>chunk(100)
                .reader(paySummaryCursorItemReaderV2())
                .writer(paySummaryItemWriterV2())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Pay> paySummaryCursorItemReaderV2() {
        log.info("start paySummaryItemReader");
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        Object requestDate = jobParameters.get("requestDate");

        return new JdbcCursorItemReaderBuilder<Pay>()
                .name("paySummaryItemReader")
                .dataSource(dataSource)
                .sql(mssql())
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{requestDate}))
                .build();
    }

    private static String mysql() {
        return "SELECT * FROM pay p WHERE created_date = ? order by store_id ";
    }

    private static String mssql() {
        return "SELECT * FROM pay p with(nolock) WHERE created_date = ? order by store_id ";
    }


    @Bean
    @StepScope // stepContext 를 이용하려면 필요함
    public ItemWriter<Pay> paySummaryItemWriterV2() {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String writeType = (String) jobParameters.get("writeType");
        return items -> {
            String storeId = items.get(0).getStoreId();
            log.info("===================== processing storeId : {} =====================", storeId);
            if ("commit".equals(writeType)) {
                createSummary(items);
            } else {
                doLogSummary(items);
            }
            log.info("===================== continue... =================================");
        };
    }

    private void doLogSummary(List<? extends Pay> items) {
        for (Pay item : items) {
            log.info("item {}", item);
        }
    }

    private void createSummary(List<? extends Pay> items) {
        for (Pay item : items) {
            log.info("item {}", item);
            Optional<SalesSummary> optionalSalesSummary = salesSummaryRepository.findByStoreIdAndSalesDate(item.getStoreId(), item.getCreatedDate());

            if (optionalSalesSummary.isPresent()) {
                Integer totalAmount = item.getTotalAmount();
                Integer vatDeductedAmount = item.getTotalAmount() - item.getVatAmount();

                SalesSummary findSaleSummary = optionalSalesSummary.get();
                findSaleSummary.update(totalAmount, vatDeductedAmount);
            }
            else {
                salesSummaryRepository.save(new SalesSummary(
                        item.getStoreId(),
                        item.getTotalAmount(),
                        item.vatDeductedAmount(),
                        TOTAL_TRANSACTION_INITIAL_VALUE,
                        item.getCreatedDate(),
                        SystemType.BATCH));
            }
        }
    }
}
