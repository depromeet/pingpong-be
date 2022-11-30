package com.dpm.winwin.domain.entity.post.enums;

public enum ExchangePeriod {
    A_WEEK("1주일 이하"),
    LESS_THAN_A_MONTH("1주일 이상"),
    MORE_THAN_A_MONTH("1달 이상"),
    ANY_PERIOD("조율 가능");

    private final String message;

    ExchangePeriod(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
