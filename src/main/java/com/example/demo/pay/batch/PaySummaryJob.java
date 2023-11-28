package com.example.demo.pay.batch;

import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.SystemType;
import com.example.demo.sales.infra.SalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaySummaryJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final SalesSummaryRepository salesSummaryRepository;

    private static final int INTEGER_MAX = Integer.MAX_VALUE;

    @Bean(name = "pay.summary.job")
    public Job paySummaryJob() throws Exception {
        return jobBuilderFactory.get("paySummaryJob")
                .start(paySummaryStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step paySummaryStep() throws Exception {
        return stepBuilderFactory.get("paySummaryStep")
                .<PaySummary, PaySummary>chunk(10)
                .reader(paySummaryCursorItemReader())
                .writer(paySummaryItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<PaySummary> paySummaryCursorItemReader() throws Exception {
        log.info("start paySummaryItemReader");
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        Object requestDate = jobParameters.get("requestDate");

        return new JdbcCursorItemReaderBuilder<PaySummary>()
                .name("paySummaryItemReader")
                .dataSource(dataSource)
                .sql("SELECT store_id, " +
                        "SUM(total_amount) AS daily_total_sales, " +
                        "SUM(vat_amount) AS daily_vat_deducted_sales, " +
                        "COUNT(total_amount) AS daily_total_transaction " +
                        "FROM Pay " +
                        "WHERE CONVERT(DATE, create_time, 120) = ? " +
                        "GROUP BY store_id")
                // rowMapper 는 각 행(레코드)을 객체로 변환하는데 사용된다.
                .rowMapper(new BeanPropertyRowMapper<>(PaySummary.class))
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{requestDate}))
                .build();
    }


    @Bean
    @StepScope // stepContext 를 이용하려면 필요함
    public ItemWriter<PaySummary> paySummaryItemWriter() {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String requestDate = (String) jobParameters.get("requestDate");
        String writeType = (String) jobParameters.get("writeType");

        return items -> {
            log.info("=====================start=====================");
            if ("commit".equals(writeType)) {
                createSummary(requestDate, items);
            }
            else {
                doLogSummary(items);
            }
            log.info("===================== end =====================");
        };
    }

    private void doLogSummary(List<? extends PaySummary> items) {
        for (PaySummary item : items) {
            log.info("item {}", item);
        }
    }

    private void createSummary(String requestDate, List<? extends PaySummary> items) {
        List<SalesSummary> salesSummaries = new ArrayList<>();
        for (PaySummary item : items) {
            salesSummaries.add(
                    new SalesSummary(
                            item.getStoreId(),
                            item.getDailyTotalSales(),
                            item.getDailyVatDeductedSales(),
                            item.getDailyTotalTransaction(),
                            LocalDate.parse(requestDate, DateTimeFormatter.ISO_DATE),
                            SystemType.BATCH));
        }
        salesSummaryRepository.saveAll(salesSummaries);
    }

//    @Bean
//    public PagingQueryProvider pagingQueryProvider() throws Exception {
//        SqlPagingQueryProviderFactoryBean queryBean = new SqlPagingQueryProviderFactoryBean();
//        queryBean.setSelectClause("SELECT " +
//                "store_id, " +
//                "SUM(total_amount) AS 'daily_total_sales'," +
//                "SUM(vat_amount) AS 'daily_vat_deducted_sales', " +
//                "COUNT(total_amount) AS 'daily_total_transaction'");
//        queryBean.setFromClause("  FROM Pay ");
//        queryBean.setWhereClause(" WHERE CONVERT(DATE, create_time, 120) = :requestDate");
//        queryBean.setGroupClause(" GROUP BY store_id ");
//        queryBean.setSortKey("store_id");
//        queryBean.setDataSource(dataSource);
//        return queryBean.getObject();
//    }
//
//    @Bean
//    @StepScope
//    public JdbcPagingItemReader paySummaryPagingItemReader() throws Exception {
//        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
//        Object requestDate = jobParameters.get("requestDate");
//
//        HashMap<String, Object> parameterValues = new HashMap<>();
//        parameterValues.put("requestDate", requestDate);
//
//
//        return new JdbcPagingItemReaderBuilder()
//                .name("paySummaryItemReader")
//                .dataSource(dataSource)
//                .queryProvider(pagingQueryProvider())
//                .parameterValues(parameterValues)
//                .pageSize(10)
//                .rowMapper(payRowMapper)
//                .build();
//    }
}
