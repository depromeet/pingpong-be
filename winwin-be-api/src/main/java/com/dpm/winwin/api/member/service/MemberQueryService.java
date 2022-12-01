package com.dpm.winwin.api.member.service;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.dto.response.RanksResponse;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberQueryService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberRankReadResponse readMemberInfo(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        MemberReadResponse memberReadResponse =  memberRepository.readMemberInfo(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        RanksResponse rank = RanksResponse.of(memberReadResponse.ranks().getName(),
                memberReadResponse.ranks().getImage(), memberReadResponse.ranks().getCondition());

        return new MemberRankReadResponse(
                memberReadResponse,
                rank,
                member.getGivenTalents().stream()
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList(),
                member.getTakenTalents().stream()
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public RanksListResponse getRankList() {
        List<RanksResponse> ranks = Arrays.stream(Ranks.values())
            .map(rank -> RanksResponse.of(rank.getName(), rank.getImage(), rank.getCondition()))
            .toList();

        return RanksListResponse.from(ranks);
    }

}
