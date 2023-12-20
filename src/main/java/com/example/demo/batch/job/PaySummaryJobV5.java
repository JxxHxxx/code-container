package com.example.demo.batch.job;

import com.example.demo.batch.job.processor.PaySummaryItemProcessor;
import com.example.demo.batch.job.processor.UnableToProcessException;
import com.example.demo.batch.mapper.PayRowMapper;
import com.example.demo.pay.domain.Pay;
import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.dto.PayDto;
import com.example.demo.sales.infra.SalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 결제 데이터를 합산하여
 * 매출 요약 데이터를 만든다.
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaySummaryJobV5 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final SalesSummaryRepository salesSummaryRepository;
    private final PaySummaryItemProcessor paySummaryItemProcessor;
    private final PayRowMapper payRowMapper;

    @Bean(name = "pay.summary.job.v5")
    public Job paySummaryJobV5() throws Exception {
        return jobBuilderFactory.get("pay.summary.job.v5")// 재시작시 잡 이름 찾음
                .start(paySummaryStepV5())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step paySummaryStepV5() throws Exception {
        return stepBuilderFactory.get("paySummaryStepV5")
                . <Pay, Pay>chunk(100)
                .reader(paySummaryCursorItemReaderV5())
                .processor(paySummaryItemProcessor)
                .writer(paySummaryItemWriterV5())
                .faultTolerant()
                .skip(UnableToProcessException.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Pay> paySummaryCursorItemReaderV5() {
        log.info("start paySummaryItemReaderV5");
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        Object requestDate = jobParameters.get("requestDate");

        return new JdbcCursorItemReaderBuilder<Pay>()
                .name("paySummaryItemReaderV5")
                .dataSource(dataSource)
                .sql(mssql())
                .rowMapper(payRowMapper)
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
    public ItemWriter<Pay> paySummaryItemWriterV5() {
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

    private void createSummary(List<? extends Pay> pays) {
        for (Pay pay : pays) {
            log.info("item {}", pay);
            Optional<SalesSummary> optionalSalesSummary = salesSummaryRepository.findByStoreIdAndSalesDate(pay.getStoreId(), pay.getCreatedDate());

            PayDto payDto = new PayDto(pay.getStoreId(), pay.getPayAmount(), pay.getVatAmount(), pay.getCreatedDate());
            // 당일,해당 점포 매출 데이터 반영이 최초가 아닌 경우
            if (optionalSalesSummary.isPresent()) {

                SalesSummary findSaleSummary = optionalSalesSummary.get();
                findSaleSummary.reflectPayInformation(payDto);
            }

            // 당일, 해당 점포 매출 데이터 반영이 최초인 경우
            else {
                SalesSummary salesSummary = SalesSummary.isStoresFirstOfTheDaySalesSummary(payDto);
                salesSummaryRepository.save(salesSummary);
            }
        }
    }
}
