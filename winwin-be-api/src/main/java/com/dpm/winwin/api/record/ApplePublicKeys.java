package com.dpm.winwin.api.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public record ApplePublicKeys(List<KeyInfo> keys) {

    @Builder
    public record KeyInfo(@JsonProperty("kty") String kty,
                          @JsonProperty("kid") String kid,
                          @JsonProperty("use") String use,
                          @JsonProperty("alg") String alg,
                          @JsonProperty("n") String n,
                          @JsonProperty("e") String e) {

    }
}
