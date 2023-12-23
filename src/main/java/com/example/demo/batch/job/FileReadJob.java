package com.example.demo.batch.job;

import com.example.demo.pay.domain.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileReadJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final FieldSetMapper payFieldSetMapper;
    private final LineTokenizer payLineTokenizer;

    @Bean(name = "file.read.job")
    public Job fileReadJob() throws IOException {
        return jobBuilderFactory.get("file.read.job")
                .start(fileReadStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = "file.read.step")
//    @StepScope 달았더니 오류남 스코프 오류... 이유 찾자
    public Step fileReadStep() throws IOException {
        return stepBuilderFactory.get("file.read.step")
                .<FieldSet, Pay>chunk(100)
                .reader(flatFileItemReaderV2(null))
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader flatFileItemReaderV2(@Value("#{jobParameters['payFile']}") String inputFileName) throws IOException {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("fileItemReader")
                .resource(new ClassPathResource(inputFileName))
                .linesToSkip(1)
                .skippedLinesCallback(line -> log.info("skipped line : {}", line))
                .lineTokenizer(payLineTokenizer)
                .fieldSetMapper(payFieldSetMapper)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<FieldSet> flatFileItemReader(@Value("#{jobParameters['payFile']}") String inputFileName) throws IOException {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("fileItemReader")
                .resource(new ClassPathResource(inputFileName))
                .linesToSkip(1)
                .skippedLinesCallback(line -> log.info("skipped line : {}", line))
//                .lineTokenizer(new DelimitedLineTokenizer()) // FlatFile 에서 라인(행)을 처리, 구분자를 통해 한 행에서 열들을 추출함
                .delimited()
                .names(new String[]{"pay_id", "created_date", "created_time", "order_amount", "order_no",
                                    "store_id", "pay_amount", "pay_status", "pay_type", "payer_id", "vat_amount"})
                .fieldSetMapper(payFieldSetMapper)
                .build();
    }

    private LineMapper<FieldSet> payLineMapper() {
        DefaultLineMapper<FieldSet> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(payFieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public ItemWriter<Pay> itemWriter() {
        return items -> items.forEach(item -> log.info("item {}", item));
    }
}
