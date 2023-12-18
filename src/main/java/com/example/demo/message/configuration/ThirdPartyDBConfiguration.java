package com.example.demo.message.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ThirdPartyDBConfiguration {

    /**
     * default datasource
     */
    @Bean
    @Primary
    public DataSource dataSource(@Value("${spring.datasource.url}") String url,
                                 @Value("${spring.datasource.username}") String username,
                                 @Value("${spring.datasource.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Value("${3rd-party.pay.datasource.url}")
    private String payServerUrl;
    @Value("${3rd-party.pay.datasource.username}")
    private String payServerUsername;
    @Value("${3rd-party.pay.datasource.password}")
    private String payServerPassword;

    @Bean(name = "cardJdbcTemplate")
    public JdbcTemplate cardJdbcTemplate() {
        return new JdbcTemplate(payDataSource());
    }
    @Bean
    public DataSource payDataSource() {

        return DataSourceBuilder.create()
                .url(payServerUrl)
                .username(payServerUsername)
                .password(payServerPassword)
                .build();
    }
}
