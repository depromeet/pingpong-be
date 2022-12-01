package com.dpm.winwin.api.member.dto.request;

import com.dpm.winwin.domain.entity.member.Member;

public record MemberCreateRequest(String nickname,
                                  String image,
                                  String introduction,
                                  int exchangeCount,
                                  String profileLink
) {
    public Member toEntity(){
        return Member.builder()
                .nickname(this.nickname)
                .image(this.image)
                .introduction(this.introduction)
                .exchangeCount(this.exchangeCount)
                .profileLink(this.profileLink)
                .build();
    }
}
