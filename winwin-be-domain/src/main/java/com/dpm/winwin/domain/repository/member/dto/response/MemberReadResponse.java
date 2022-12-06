package com.dpm.winwin.domain.repository.member.dto.response;

public record MemberReadResponse(Long memberId, String nickname, String image,
                                 String introductions, int exchangeCount, String profileLink,
                                 String givenTalent, String takenTalent) {


}
