package com.example.demo.message.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
@Slf4j @Getter @ToString @NoArgsConstructor
@Entity
public class QMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    private TaskType taskType;
    private LocalDateTime requestTime;
    private MessageStatus messageStatus;
    @Embedded
    private Requester requester;

    public QMessage(long messageId, TaskType taskType, Requester requester) {
        log.info("CALLED QMessage CONSTRUCTOR");
        this.messageId = messageId;
        this.taskType = taskType;
        this.requester = requester;
        this.requestTime = LocalDateTime.now();
        this.messageStatus = MessageStatus.SENT;
    }

}
