package com.example.demo.pay.batch;

import com.example.demo.pay.Pay;
import com.example.demo.pay.infra.PayRepository;
import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.infra.SalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaySummaryJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final PayRowMapper payRowMapper;
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
                .<Pay, Pay>chunk(10)
                .reader(paySummaryItemReader())
                .writer(paySummaryItemWriter())
                .build();
    }

    @Bean
    public PagingQueryProvider pagingQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryBean = new SqlPagingQueryProviderFactoryBean();
        queryBean.setSelectClause("select * ");
        queryBean.setFromClause("from Pay ");
        queryBean.setWhereClause("where CONVERT(DATE, create_time, 120) = :requestDate");
        queryBean.setSortKey("store_id");
        queryBean.setDataSource(dataSource);
        return queryBean.getObject();
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

    @Bean
    @StepScope
    public JdbcPagingItemReader paySummaryItemReader() throws Exception {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        Object requestDate = jobParameters.get("requestDate");

        HashMap<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("requestDate", requestDate);


        return new JdbcPagingItemReaderBuilder()
                .name("paySummaryItemReader")
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider())
                .parameterValues(parameterValues)
                .pageSize(10)
                .rowMapper(payRowMapper)
                .build();
    }

    @Bean
    public ItemWriter<Pay> paySummaryItemWriter() {
        return items -> {
            log.info("=====================start=====================");
            for (Pay item : items) {
                log.info("{}", item);
            }
            log.info("===================== end =====================");
        };
    }
}
