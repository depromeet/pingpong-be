package com.dpm.winwin.api.member.dto.request;

import com.dpm.winwin.domain.dto.member.MemberUpdateDto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record MemberUpdateRequest(

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,10}$", message = "한글/영어만 사용이 가능해요.")
    String nickname,

    @NotBlank(message = "자기 소개는 필수입니다.")
    @Size(max = 300, message = "자기 소개는 300자 이하이어야 합니다.")
    String introduction,
    String profileLink,

    @NotNull(message = "주고 싶은 재능을 선택해 주세요.")
    @Size(max = 5, message = "주고 싶은 재능은 최대 5개입니다.")
    List<Long> givenTalents,

    @NotNull(message = "받고 싶은 재능을 선택해 주세요.")
    @Size(max = 5, message = "받고 싶은 재능은 최대 5개입니다.")
    List<Long> takenTalents
) {

    public MemberUpdateDto toDto() {
        return new MemberUpdateDto(
            this.nickname,
            this.introduction,
            this.profileLink
        );
    }

}
