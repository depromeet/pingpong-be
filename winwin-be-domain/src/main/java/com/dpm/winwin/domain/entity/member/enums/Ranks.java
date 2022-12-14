package com.dpm.winwin.domain.entity.member.enums;

import lombok.Getter;

@Getter
public enum Ranks {

    PRO("프로", "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/rank/pro.png", "좋아요 10k 이상", 10000),
    JUNIOR("주니어", "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/rank/junior.png", "좋아요 1k 이상", 1000),
    BEGINNER("비기너", "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/rank/beginner.png", "좋아요 100개 이상", 100),
    ROOKIE("루키", "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/rank/rookie.png", "좋아요 100개 미만", 0);

    private final String name;
    private final String image;
    private final String condition;
    private final Integer likeCount;

    Ranks(String message, String image, String condition, Integer likeCount) {
        this.name = message;
        this.image = image;
        this.condition = condition;
        this.likeCount = likeCount;
    }
}
