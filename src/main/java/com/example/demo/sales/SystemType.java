package com.example.demo.sales;

public enum SystemType {
    BATCH("배치 시스템"), API("API"), ADMIN("관리자 시스템");

    private final String description;

    SystemType(String description) {
        this.description = description;
    }
}
