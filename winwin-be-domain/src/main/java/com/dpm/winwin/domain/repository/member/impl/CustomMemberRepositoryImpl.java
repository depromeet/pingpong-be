package com.dpm.winwin.domain.repository.member.impl;

import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.oauth.QOauthToken.oauthToken;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.enums.ProviderType;
import com.dpm.winwin.domain.repository.member.CustomMemberRepository;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<MemberReadResponse> readMemberInfo(Long memberId) {

        return Optional.ofNullable(
                jpaQueryFactory
                        .from(member)
                        .where(member.id.eq(memberId))
                        .transform(
                                groupBy(member.id).as(
                                        Projections.constructor(
                                                MemberReadResponse.class,
                                                member.id,
                                                member.nickname,
                                                member.image,
                                                member.introduction,
                                                member.ranks
                                                )
                                )
                        ).get(memberId)
        );
    }

    @Override
    public Optional<Member> findMemberWithOauthToken(Long memberId) {
        Member findMember = jpaQueryFactory.selectFrom(member)
            .where(member.id.eq(memberId))
            .join(member.oauthToken, oauthToken)
            .fetchJoin()
            .fetchOne();

        return Optional.ofNullable(findMember);
    }

    @Override
    public Optional<Member> findByMemberByOauthProviderAndSocialId(ProviderType provider, String socialId) {
        Member findMember = jpaQueryFactory.selectFrom(member)
            .join(member.oauthToken, oauthToken)
            .where(oauthToken.providerType.eq(provider)
                .and(oauthToken.socialId.eq(socialId)))
            .fetchJoin()
            .fetchOne();

        return Optional.ofNullable(findMember);
    }

}
