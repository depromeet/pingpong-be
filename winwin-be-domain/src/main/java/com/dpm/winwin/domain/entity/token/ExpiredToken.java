package com.dpm.winwin.domain.entity.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "expiredToken", timeToLive = 86400)
@RequiredArgsConstructor
public class ExpiredToken {

    @Id
    private final String accessToken;

}
