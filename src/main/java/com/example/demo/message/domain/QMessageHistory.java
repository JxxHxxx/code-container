package com.example.demo.message.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class QMessageHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long QMessageId;
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;
    @Embedded
    private Requester requester;
    private String orderNo;
    private LocalDateTime executeTime;

    public QMessageHistory(Long QMessageId, String orderNo, TaskType taskType, MessageStatus messageStatus, Requester requester) {
        this.QMessageId = QMessageId;
        this.orderNo = orderNo;
        this.taskType = taskType;
        this.messageStatus = messageStatus;
        this.requester = requester;
        this.executeTime = LocalDateTime.now();
    }

    public String requesterId() {
        return this.requester.getId();
    }

    public LocalDateTime requestTime() {
        return this.requester.getRequestTime();
    }

}
