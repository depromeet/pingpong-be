package com.dpm.winwin.domain.repository.member.impl;

import com.dpm.winwin.domain.repository.member.CustomMemberRepository;
import com.dpm.winwin.domain.repository.member.dto.MemberDetailResponse;
import com.dpm.winwin.domain.repository.member.dto.QMemberDetailResponse;
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

    public Optional<MemberDetailResponse> findMemberDetailInfo(long memberId){
        return Optional.ofNullable(
                jpaQueryFactory
                        .from(member)
                        .where(member.id.eq(memberId))
                        .leftJoin(memberTalent).on(memberTalent.member.id.eq(memberId))
                        .transform(
                                groupBy(member.id).as(
                                        new QMemberDetailResponse(member,memberTalent.type.stringValue().as("GIVE"),memberTalent.type.stringValue().as("TAKE") )
                                )
                            ).get(memberId)
                        );
    }
}
