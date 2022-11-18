package com.dpm.winwin.domain.entity.post.enums;

public enum ExchangeType {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    ANY_TYPE("상관 없음");

    private final String message;

    ExchangeType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
