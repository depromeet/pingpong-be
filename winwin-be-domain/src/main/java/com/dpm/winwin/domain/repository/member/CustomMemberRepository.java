package com.dpm.winwin.domain.repository.member;

import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.enums.ProviderType;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

import java.util.Optional;

public interface CustomMemberRepository {

    Optional<MemberReadResponse> readMemberInfo(Long memberId);

    Optional<Member> findMemberWithToken(Long memberId);

    Optional<Member> findByMemberByOauthProviderAndSocialId(ProviderType provider, String socialId);
}
