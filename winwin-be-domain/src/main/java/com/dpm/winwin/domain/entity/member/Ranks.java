package com.dpm.winwin.domain.entity.member;

public enum Ranks {
    RANK1("등급1"),
    RANK2("등급2"),
    RANK3("등급3"),
    RANK4("등급4");

    private final String message;

    Ranks(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
