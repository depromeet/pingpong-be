package com.dpm.winwin.domain.repository.post.impl;

import static com.dpm.winwin.domain.entity.category.QMainCategory.mainCategory;
import static com.dpm.winwin.domain.entity.category.QMidCategory.midCategory;
import static com.dpm.winwin.domain.entity.category.QSubCategory.subCategory;
import static com.dpm.winwin.domain.entity.link.QLink.link;
import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.post.QPost.post;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.CustomPostRepository;
import com.dpm.winwin.domain.repository.post.dto.PostListDto;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Post getPostByMemberId(Long memberId, Long postId) {
        return queryFactory
            .selectFrom(post)
            .leftJoin(post.member, member)
            .where(post.id.eq(postId), member.id.eq(memberId))
            .fetchOne();
    }

    @Override
    public Page<PostListDto> getAllByIsShareAndMidCategory(
        PostListConditionRequest condition, Pageable pageable
    ) {
        return null;
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

    @Override
    public Optional<Post> getId(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(post)
                .leftJoin(post.mainCategory, mainCategory).fetchJoin()
                .leftJoin(post.midCategory, midCategory).fetchJoin()
                .leftJoin(post.subCategory, subCategory).fetchJoin()
                .leftJoin(post.links, link).fetchJoin()
                .where(post.id.eq(id))
                .fetchOne());
    }
}
