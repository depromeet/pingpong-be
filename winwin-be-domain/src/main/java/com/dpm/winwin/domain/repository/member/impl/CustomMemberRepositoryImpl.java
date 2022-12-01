package com.dpm.winwin.domain.repository.member.impl;

import com.dpm.winwin.domain.entity.member.enums.TalentType;
import com.dpm.winwin.domain.repository.member.CustomMemberRepository;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.member.QMemberTalent.memberTalent;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

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
                                        Projections.constructor(
                                                MemberReadResponse.class,
                                                member.id,
                                                member.nickname,
                                                member.image,
                                                member.introduction,
                                                member.exchangeCount,
                                                member.profileLink,
                                                member.ranks
                                                )
                                )
                        ).get(memberId)
        );
    }
}
