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
    private LocalDateTime executeTime;

    @Builder
    public QMessageHistory(Long QMessageId, TaskType taskType, MessageStatus messageStatus, Requester requester) {
        this.QMessageId = QMessageId;
        this.taskType = taskType;
        this.messageStatus = messageStatus;
        this.requester = requester;
        this.executeTime = LocalDateTime.now();
    }
}
