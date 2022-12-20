package com.dpm.winwin.api.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record MemberNicknameRequest(

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,10}$", message = "한글/영어만 사용이 가능해요.")
    String nickname
) {

}
