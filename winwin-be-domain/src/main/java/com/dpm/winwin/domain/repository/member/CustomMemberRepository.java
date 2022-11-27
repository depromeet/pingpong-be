package com.dpm.winwin.domain.repository.member;

import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

import java.util.Optional;

public interface CustomMemberRepository {

    Optional<MemberReadResponse> readMemberInfo(Long memberId);

}
