package com.example.demo.message.model;

import com.example.demo.message.domain.TaskType;
import com.example.demo.message.dto.OrderMessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * table schema : mssql 기준
 * CREATE TABLE PAY_TO_ORDERS (
 * 	order_pk bigint IDENTITY(1,1) NOT NULL PRIMARY KEY,
 * 	order_no varchar(255) NOT NULL,
 * 	sent_time datetime2 NOT NULL,
 * 	order_message_type varchar(255) NOT NULL,
 * );
 */

@Getter
@Setter
@ToString
public class OrderMessageParam {

    private final String orderNo;
    private final LocalDateTime sentTime;
    private final TaskType taskType;

    public OrderMessageParam(String orderNo, TaskType taskType) {
        this.orderNo = orderNo;
        this.sentTime = LocalDateTime.now();
        this.taskType = taskType;
    }

    public static OrderMessageParam create(String orderNo, TaskType taskType) {
        return new OrderMessageParam(orderNo, taskType);
    }
}
