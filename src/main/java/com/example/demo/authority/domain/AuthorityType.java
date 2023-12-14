package com.example.demo.authority.domain;

import lombok.Getter;

@Getter
public enum AuthorityType {

    APPOINTMENT(1, "발령권한"),
    APPLY(2, "신청권한");

    private final int code;
    private final String description;

    AuthorityType(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
