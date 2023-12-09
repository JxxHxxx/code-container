package com.example.demo.batch.mapper;

import com.example.demo.pay.domain.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class PayRowMapper implements RowMapper<Pay> {
    @Override
    public Pay mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderInformation orderInformation = new OrderInformation(
                rs.getString("order_no"),
                rs.getInt("order_amount"),
                rs.getString("store_id"));

        PayInformation payInformation = new PayInformation(
                rs.getString("payer_id"),
                rs.getInt("pay_amount"),
                rs.getInt("vat_amount"),
                PayType.valueOf(rs.getString("pay_type")),
                PayStatus.valueOf(rs.getString("pay_status")));

        return new Pay(orderInformation,
                payInformation,
                LocalDate.parse(rs.getString("created_date")),
                LocalTime.parse(rs.getString("created_time")));
    }
}
