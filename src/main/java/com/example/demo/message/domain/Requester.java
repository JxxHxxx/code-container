package com.example.demo.message.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter @NoArgsConstructor
@Embeddable
public class Requester {

    @Column(name = "requester_id")
    private String id;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    public Requester(String id, LocalDateTime requestTime) {
        this.id = id;
        this.requestTime = requestTime;
    }

}
