package com.example.demo.message.infra;

import com.example.demo.message.model.OrderMessageParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderMessageRepository {

    @Qualifier(value = "orderJdbcTemplate")
    private final JdbcTemplate orderJdbcTemplate;


    public boolean insert(OrderMessageParam param) {
        log.info("orderMessageParam {}", param);

        int insertedRowsNumber = orderJdbcTemplate.update(
                "INSERT INTO PAY_TO_ORDERS (order_no, sent_time, task_type) " +
                "values (?,?,?)", preparedStatementSetter(param));

        return insertedRowsNumber == 0 ? false : true;
    }

    private PreparedStatementSetter preparedStatementSetter(OrderMessageParam param) {
        return ps -> {
            ps.setString(1, param.getOrderNo());
            ps.setString(2, param.getSentTime().toString());
            ps.setString(3, param.getTaskType().name());
        };
    }
}
