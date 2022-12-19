package com.dpm.winwin.domain.repository.member.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record MemberNicknameRequest(
    @NotEmpty(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,10}$", message = "한글/영어만 사용이 가능해요.")
    String nickname
) {

}
