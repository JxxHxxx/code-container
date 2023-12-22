package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    /**
     * default datasource
     */
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "jdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    @Value("${3rd-party.order.datasource.url}")
    private String orderDbUrl;
    @Value("${3rd-party.order.datasource.username}")
    private String orderDbUsername;
    @Value("${3rd-party.order.datasource.password}")
    private String orderDbPassword;

    @Bean(name = "orderJdbcTemplate")
    public JdbcTemplate orderJdbcTemplate() {
        return new JdbcTemplate(orderDataSource());
    }

    @Bean(name = "orderNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate orderNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(orderDataSource());
    }
    @Bean
    public DataSource orderDataSource() {

        return DataSourceBuilder.create()
                .url(orderDbUrl)
                .username(orderDbUsername)
                .password(orderDbPassword)
                .build();
    }
}
