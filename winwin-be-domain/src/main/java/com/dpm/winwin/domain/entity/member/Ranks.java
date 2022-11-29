package com.dpm.winwin.domain.entity.member;

public enum Ranks {
    ROOKIE("루키"),
    BEGINNER("비기너"),
    JUNIOR("주니어"),
    PRO("프로");


    private final String message;

    Ranks(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
