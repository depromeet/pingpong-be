package com.dpm.winwin.domain.repository.post.impl;

import static com.dpm.winwin.domain.entity.category.QMainCategory.mainCategory;
import static com.dpm.winwin.domain.entity.category.QMidCategory.midCategory;
import static com.dpm.winwin.domain.entity.category.QSubCategory.subCategory;
import static com.dpm.winwin.domain.entity.link.QLink.link;
import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.post.QLikes.likes;
import static com.dpm.winwin.domain.entity.post.QPost.post;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.CustomPostRepository;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> getPostByMemberId(Long memberId, Long postId) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member)
                .where(post.id.eq(postId), member.id.eq(memberId))
                .fetchOne());
    }

    public Optional<Integer> getMemberLikeByMemberId(Long memberId) {
        return Optional.ofNullable(
                queryFactory
                        .select(post.likes.size())
                        .from(post)
                        .leftJoin(post.member, member)
                        .where(member.id.eq(memberId))
                        .fetchOne());
    }

    public Page<Post> getAllByIsShareAndMidCategory(
        PostListConditionRequest condition, Pageable pageable
    ) {
        List<Post> posts = queryFactory
            .select(post)
            .from(post)
            .leftJoin(post.member, member)
            .fetchJoin()
            .leftJoin(post.likes, likes)
            .fetchJoin()
            .where(
                isShareEq(condition.isShare()),
                mainCategoryEq(condition.mainCategory()),
                midCategoryEq(condition.midCategory()),
                subCategoryEq(condition.subCategory())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(post.count())
            .from(post)
            .leftJoin(post.member, member)
            .where(
                isShareEq(condition.isShare()),
                mainCategoryEq(condition.mainCategory()),
                midCategoryEq(condition.midCategory()),
                subCategoryEq(condition.subCategory())
            );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Post> getByIdFetchJoin(Long postId) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(post.links, link).fetchJoin()
                .leftJoin(post.mainCategory, mainCategory).fetchJoin()
                .leftJoin(post.midCategory, midCategory).fetchJoin()
                .leftJoin(post.subCategory, subCategory).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne()
        );
    }

    private BooleanExpression isShareEq(Boolean isShare) {
        if (isShare != null && isShare) {
            return post.isShare.eq(true);
        }

        return null;
    }

    private BooleanExpression mainCategoryEq(Long mainCategory) {
        if (mainCategory != null) {
            return post.mainCategory.id.eq(mainCategory);
        }

        return null;
    }

    private BooleanExpression midCategoryEq(Long midCategory) {
        if (midCategory != null) {
            return post.midCategory.id.eq(midCategory);
        }

        return null;
    }

    private BooleanExpression subCategoryEq(Long subCategory) {
        if (subCategory != null) {
            return post.subCategory.id.eq(subCategory);
        }

        return null;
    }
}
