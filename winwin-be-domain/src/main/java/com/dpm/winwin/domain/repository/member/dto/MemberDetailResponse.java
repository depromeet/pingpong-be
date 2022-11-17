package com.dpm.winwin.domain.repository.member.dto;

import com.dpm.winwin.domain.entity.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MemberDetailResponse {
    private Long memberId;
    private String nickname;
    private String image;
    private String introductions;
    private int exchangeCount;
    private String profileLink;
    private String givenTalent;
    private String takenTalent;

    @QueryProjection
    public MemberDetailResponse(Member member, String givenTalent, String takenTalent){
        this.memberId = member.getId();
        this.nickname = member.getImage();
        this.image = member.getImage();
        this.introductions = member.getIntroductions();
        this.exchangeCount = member.getExchangeCount();
        this.profileLink = member.getProfileLink();
        this.givenTalent = givenTalent;
        this.takenTalent = takenTalent;
    }
}
