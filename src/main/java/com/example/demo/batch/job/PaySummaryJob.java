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
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.boot.jdbc.DatabaseDriver.*;
import static org.springframework.boot.jdbc.DatabaseDriver.SQLSERVER;

/**
 * 결제 데이터를 합산하여
 * 매출 요약 데이터를 만든다.
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaySummaryJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final SalesSummaryRepository salesSummaryRepository;
    private final PaySummaryItemProcessor paySummaryItemProcessor;
    private final PayRowMapper payRowMapper;
    private final Environment env;

    private static final String DB_DRIVER_CLASS_NAME_PROPERTY_KEY = "spring.datasource.driver-class-name";

    @Bean(name = "pay.summary.job")
    public Job paySummaryJob() throws Exception {
        return jobBuilderFactory.get("pay.summary.job")// 재시작시 잡 이름 찾음
                .start(paySummaryStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step paySummaryStep() throws Exception {
        return stepBuilderFactory.get("paySummaryStep")
                . <Pay, Pay>chunk(100)
                .reader(paySummaryCursorItemReader())
                .processor(paySummaryItemProcessor)
                .writer(paySummaryItemWriter())
                .faultTolerant()
                .skipLimit(Integer.MAX_VALUE)
                .skip(UnableToProcessException.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Pay> paySummaryCursorItemReader() {
        log.info("start paySummaryItemReader");
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        Object requestDate = jobParameters.get("requestDate");

        String sql = createPayQueryByDbVendor();

        return new JdbcCursorItemReaderBuilder<Pay>()
                .name("paySummaryItemReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(payRowMapper)
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{requestDate}))
                .build();
    }

    private String createPayQueryByDbVendor() {
        String sql  = null;

        if (MYSQL.getDriverClassName().equals(env.getProperty(DB_DRIVER_CLASS_NAME_PROPERTY_KEY))) {
            sql = "SELECT * FROM pay p WHERE created_date = ? order by store_id ";
        }
        if (SQLSERVER.getDriverClassName().equals(env.getProperty(DB_DRIVER_CLASS_NAME_PROPERTY_KEY))) {
            sql = "SELECT * FROM pay p with(nolock) WHERE created_date = ? order by store_id ";
        }

        if (Objects.isNull(sql)) {
            log.info("Driver class is not MYSQL or SQLSERVER");
            throw new IllegalArgumentException("Driver class is not MYSQL or SQLSERVER");
        }
        return sql;
    }

    @Bean
    @StepScope // stepContext 를 이용하려면 필요함
    public ItemWriter<Pay> paySummaryItemWriter() {
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
