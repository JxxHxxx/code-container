package com.example.demo.message.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter(value = AccessLevel.PROTECTED)
@NoArgsConstructor
@Embeddable
public class Requester {

    @Column(name = "requester_id")
    private String id;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    public Requester(String id) {
        this.id = id;
        this.requestTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Requester{" +
                "id='" + id + '\'' +
                ", requestTime=" + requestTime +
                '}';
    }
}
