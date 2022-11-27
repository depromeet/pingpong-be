package com.dpm.winwin.domain.repository.post.impl;

import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.post.QPost.post;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.CustomPostRepository;
import com.dpm.winwin.domain.repository.post.dto.PostListCondition;
import com.dpm.winwin.domain.repository.post.dto.PostMemberDto;
import com.dpm.winwin.domain.repository.post.dto.QPostMemberDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<PostMemberDto>> getAllByIsShareAndMidCategory(PostListCondition condition) {
        return Optional.ofNullable(
            queryFactory.select(
                new QPostMemberDto(post.title, post.subCategory.name, post.likes.size(),
                    member.nickname, member.image
                ))
                .from(post)
                .leftJoin(post.member, member)
                .where(
                    isShareEq(condition.isShare()),
                    categoryEq(condition.midCategory())
                )
                .fetch()
        );
    }

    private BooleanExpression isShareEq(Boolean isShare) {
        if (isShare != null && isShare) {
            return post.isShare.eq(true);
        }

        return null;
    }

    private BooleanExpression categoryEq(Long midCategory) {
        if (midCategory != null) {
            return post.midCategory.id.eq(midCategory);
        }

        return null;
    }
}
