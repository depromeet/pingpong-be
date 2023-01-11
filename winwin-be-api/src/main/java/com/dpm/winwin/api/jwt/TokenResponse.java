package com.dpm.winwin.api.jwt;

public record TokenResponse(long memberId,
                            String accessToken,
                            String refreshToken,
                            String name,
                            boolean isExistNickname) {

}
