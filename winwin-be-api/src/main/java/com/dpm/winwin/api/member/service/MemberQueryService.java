package com.dpm.winwin.api.member.service;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.dto.response.RanksResponse;
import com.dpm.winwin.api.member.dto.response.TalentResponse;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public MemberRankReadResponse readMemberInfo(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        MemberReadResponse memberReadResponse =  memberRepository.readMemberInfo(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        BigDecimal likeCount = BigDecimal.valueOf(member.getLikeCount());
        String likeCounts = likeCount.toString();


        if (likeCount.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            likeCounts = likeCount.divide(BigDecimal.valueOf(1000),1, RoundingMode.DOWN).toPlainString() + 'k';
        }

        return new MemberRankReadResponse(
                memberReadResponse.memberId(),
                memberReadResponse.nickname(),
                memberReadResponse.image(),
                memberReadResponse.introduction(),
                member.getRanks().getName(),
                member.getRanks().getImage(),
                likeCounts,
                member.getProfileLink(),
                member.getTalents().stream()
                        .filter(memberTalent -> memberTalent.getType().equals(GIVE))
                        .map(TalentResponse::of)
                        .toList(),
                member.getTalents().stream()
                        .filter(memberTalent -> memberTalent.getType().equals(TAKE))
                        .map(TalentResponse::of)
                        .toList()
        );
    }

    public RanksListResponse getRankList() {
        List<RanksResponse> ranks = Arrays.stream(Ranks.values())
                .sorted(Collections.reverseOrder())
                .map(rank -> RanksResponse.of(rank.getName(), rank.getImage(), rank.getCondition()))
                .toList();

        return RanksListResponse.from(ranks);
    }

}
