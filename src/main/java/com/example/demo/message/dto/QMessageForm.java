package com.example.demo.message.dto;

import com.example.demo.message.domain.ServiceType;
import com.example.demo.message.domain.TaskType;
import lombok.Getter;

@Getter
public class QMessageForm {
    private final TaskType taskType;
    private final String requesterId;
    private final String orderNo;
    private final ServiceType serviceType;

    public QMessageForm(TaskType taskType, String requesterId, String orderNo, ServiceType serviceType) {
        this.taskType = taskType;
        this.requesterId = requesterId;
        this.orderNo = orderNo;
        this.serviceType = serviceType;
    }
}
