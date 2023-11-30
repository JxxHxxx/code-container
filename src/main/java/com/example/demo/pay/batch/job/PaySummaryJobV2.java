package com.example.demo.pay.batch.job;

import com.example.demo.pay.Pay;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.sales.SystemType.BATCH;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaySummaryJobV2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final SalesSummaryRepository salesSummaryRepository;

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

    private void createSummary(List<? extends Pay> pays) {
        for (Pay pay : pays) {
            log.info("item {}", pay);
            Optional<SalesSummary> optionalSalesSummary = salesSummaryRepository.findByStoreIdAndSalesDate(pay.getStoreId(), pay.getCreatedDate());

            PayDto payDto = new PayDto(pay.getStoreId(), pay.getTotalAmount(), pay.getVatAmount(), pay.getCreatedDate());
            if (optionalSalesSummary.isPresent()) {

                SalesSummary findSaleSummary = optionalSalesSummary.get();
                findSaleSummary.reflectPayInformation(payDto);

            }
            else {
                SalesSummary salesSummary = SalesSummary.constructorStoreIdIsNotExistCase(payDto, BATCH);
                salesSummaryRepository.save(salesSummary);
            }
        }
    }
}
