package com.dpm.winwin.domain.repository.member.dto.response;

import com.dpm.winwin.domain.entity.member.enums.Ranks;

import java.util.List;

public record MemberReadResponse(Long memberId,
                                 String nickname,
                                 String image,
                                 String introduction,
                                 int exchangeCount,
                                 String profileLink,
                                 List<String> givenTalent,
                                 List<String> takenTalent,
                                 Ranks ranks) {

}
