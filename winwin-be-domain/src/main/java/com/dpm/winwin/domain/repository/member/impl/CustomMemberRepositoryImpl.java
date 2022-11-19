package com.dpm.winwin.domain.repository.member.impl;

import com.dpm.winwin.domain.repository.member.CustomMemberRepository;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import com.dpm.winwin.domain.repository.member.dto.response.QMemberReadResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.talent.QMemberTalent.memberTalent;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<MemberReadResponse> readMemberInfo(Long memberId){
        return Optional.ofNullable(
                jpaQueryFactory
                        .from(member)
                        .where(member.id.eq(memberId))
                        .leftJoin(memberTalent).on(memberTalent.member.id.eq(memberId))
                        .transform(
                                groupBy(member.id).as(
                                        new QMemberReadResponse(member.id,member.nickname, member.image,member.introductions,
                                                member.exchangeCount, member.profileLink,
                                                memberTalent.type.stringValue().as("GIVE"), memberTalent.type.stringValue().as("TAKE"))
                                )
                        ).get(memberId)
        );
    }

}
