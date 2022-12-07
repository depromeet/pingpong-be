package com.dpm.winwin.domain.repository.member.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record MemberNicknameRequest(
        @NotEmpty
        @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,10}$")
        String nickname
) {

}
