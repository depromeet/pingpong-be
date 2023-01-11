package com.dpm.winwin.domain.repository.member.dto.response;

import com.dpm.winwin.domain.entity.member.enums.Ranks;

public record MemberReadResponse(Long memberId,
                                 String name,
                                 String nickname,
                                 String image,
                                 String introduction,
                                 Ranks ranks
) {

}
