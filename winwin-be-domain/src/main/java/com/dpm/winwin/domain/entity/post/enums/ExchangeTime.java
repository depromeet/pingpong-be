package com.dpm.winwin.domain.entity.post.enums;

public enum ExchangeTime {
    NOON("오전"),
    AFTERNOON("오후"),
    EVENING("밤"),
    ANY_TIME("조율 가능");

    private final String message;

    ExchangeTime(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
