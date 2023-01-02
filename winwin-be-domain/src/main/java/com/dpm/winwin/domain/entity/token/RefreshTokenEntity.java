package com.dpm.winwin.domain.entity.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 86400)
@RequiredArgsConstructor
public class RefreshTokenEntity {

    @Id
    private final Long id;
    private final String refreshToken;

}
