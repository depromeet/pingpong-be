package com.dpm.winwin.api.member.application;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.domain.repository.member.dto.response.MemberDetailResponse;
import com.dpm.winwin.domain.repository.member.impl.CustomMemberRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final CustomMemberRepositoryImpl customMemberRepository;

    @Transactional(readOnly = true)
    public MemberDetailResponse readMemberDetailInfo(long memberId){
        return customMemberRepository.readMemberDetailInfo(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

}
