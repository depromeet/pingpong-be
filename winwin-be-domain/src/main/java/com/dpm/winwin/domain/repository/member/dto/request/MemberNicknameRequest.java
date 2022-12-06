package com.dpm.winwin.domain.repository.member.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record MemberNicknameRequest(
        @NotEmpty(message = "닉네임은 필수 입력값입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,10}$" , message = "닉네임은 한글과 영어만 가능하며, 2~10자리여야 합니다.")
        String nickname
) {

}
