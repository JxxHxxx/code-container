package com.example.demo.pay.batch;

import com.example.demo.pay.domain.Pay;
import com.example.demo.pay.domain.PayType;
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
        return new Pay(
                rs.getLong("id"),
                rs.getInt("total_amount"),
                rs.getInt("vat_amount"),
                rs.getString("sender_id"),
                rs.getString("store_id"),
                PayType.valueOf(rs.getString("pay_type")),
                LocalDate.parse(rs.getString("created_date")),
                LocalTime.parse(rs.getString("created_time"))
        );
    }
}
