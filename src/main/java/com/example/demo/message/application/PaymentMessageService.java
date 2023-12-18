package com.example.demo.message.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMessageService {
    @Qualifier(value = "cardJdbcTemplate")
    private final JdbcTemplate cardJdbcTemplate;

    @Transactional
    public void call(String param) throws SQLException {
        cardJdbcTemplate.update("INSERT INTO test values (?)", ps -> ps.setString(1, param));
    }
}
