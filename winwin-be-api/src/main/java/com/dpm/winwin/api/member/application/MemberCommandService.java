package com.dpm.winwin.api.member.application;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.member.dto.request.MemberCreateRequest;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberCreateResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.member.dto.request.MemberImageRequest;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final SubCategoryRepository subCategoryRepository;

    public Long updateMemberNickname(Long memberId,
                                     MemberNicknameRequest memberNicknameRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateNickname(memberNicknameRequest.nickname());
        return member.getId();
    }

    public Long updateMemberImage(Long memberId,
                                  MemberImageRequest memberImageRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateImage(memberImageRequest.image());
        return member.getId();
    }

    public MemberCreateResponse createMember(MemberCreateRequest memberCreateRequest){
        Member memberRequest = memberCreateRequest.toEntity();
        Member member = memberRepository.save(memberRequest);
        return MemberCreateResponse.from(member);
    }

    public MemberUpdateResponse updateMember(Long memberId,
                                         MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        List<SubCategory> takenTalents = subCategoryRepository.findAllById(
                memberUpdateRequest.takenTalents());
        List<SubCategory> givenTalents = subCategoryRepository.findAllById(
                memberUpdateRequest.givenTalents());

        member.update(memberUpdateRequest.toDto(),takenTalents,givenTalents);

        return new MemberUpdateResponse(
                member.getId(),
                member.getNickname(),
                member.getImage(),
                member.getIntroductions(),
                member.getProfileLink(),
                member.getGivenTalents().stream()
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList(),
                member.getTakenTalents().stream()
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList()
        );
    }

}
