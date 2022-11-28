package com.dpm.winwin.domain.repository.member.dto.response;

import java.util.List;

public record MemberReadResponse(Long memberId,
                                 String nickname,
                                 String image,
                                 String introductions,
                                 int exchangeCount,
                                 String profileLink,
                                 List<String> givenTalent,
                                 List<String> takenTalent) {


}
