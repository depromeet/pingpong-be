package com.dpm.winwin.domain.entity.member.enums;

import lombok.Getter;

@Getter
public enum Ranks {
    ROOKIE("루키", "rank/rookie.png", "좋아요 10개 미만"),
    BEGINNER("비기너", "rank/beginner.png", "좋아요 100개 이상"),
    JUNIOR("주니어", "rank/junior.png", "좋아요 1k 이상"),
    PRO("프로", "rank/pro.png", "좋아요 10k 이상");


    private final String name;
    private final String image;
    private final String condition;

    Ranks(String message, String image, String condition) {
        this.name = message;
        this.image = image;
        this.condition = condition;
    }
}
