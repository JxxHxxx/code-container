package com.example.demo.message.application;

import com.example.demo.message.infra.OrderMessageRepository;
import com.example.demo.message.model.OrderMessageParam;
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
public class OrderMessageService {
    private final OrderMessageRepository orderMessageRepository;

    @Transactional
    public void sendInsertQuery(OrderMessageParam param) throws SQLException {
        orderMessageRepository.insert(param);
    }
}
