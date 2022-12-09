package com.dpm.winwin.domain.entity.member.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Ranks {

    PRO("프로", "rank/pro.png", "좋아요 10k 이상", BigDecimal.valueOf(10000)),
    JUNIOR("주니어", "rank/junior.png", "좋아요 1k 이상", BigDecimal.valueOf(1000)),
    BEGINNER("비기너", "rank/beginner.png", "좋아요 100개 이상", BigDecimal.valueOf(100)),
    ROOKIE("루키", "rank/rookie.png", "좋아요 100개 미만", BigDecimal.valueOf(0));

    private final String name;
    private final String image;
    private final String condition;
    private final BigDecimal likeCount;

    Ranks(String message, String image, String condition, BigDecimal likeCount) {
        this.name = message;
        this.image = image;
        this.condition = condition;
        this.likeCount = likeCount;
    }
}
