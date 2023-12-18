package com.example.demo.message.domain;

import lombok.Getter;

@Getter
public enum TaskType {
    I("INSERT","쓰기:생성"),
    S("SELECT","읽기"),
    U("UPDATE","쓰기:수정"),
    SD("SOFT_DELETE","쓰기:논리삭제"),

    OC01("ORDER CANCEL BY ORDERER", "주문 취소:주문자에 의한"),
    OC02("ORDER CANCEL BY STORE","주문 취소:가게주에 의한"),
    HD("HARD_DELETE","쓰기:물리삭제");

    private final String fullName;
    private final String description;

    TaskType(String fullName, String description) {
        this.fullName = fullName;
        this.description = description;
    }
}
