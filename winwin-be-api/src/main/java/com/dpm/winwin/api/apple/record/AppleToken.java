package com.dpm.winwin.api.apple.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("id_token") String idToken,
        @JsonProperty("error") String error,
        @JsonProperty("error_description") String errorDescription) {
}
