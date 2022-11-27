package com.dpm.winwin.domain.repository.post.impl;

import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.post.QPost.post;

import com.dpm.winwin.domain.repository.post.CustomPostRepository;
import com.dpm.winwin.domain.repository.post.dto.QPostListDto;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import com.dpm.winwin.domain.repository.post.dto.PostListDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
    public Page<PostListDto> getAllByIsShareAndMidCategory(
        PostListConditionRequest condition, Pageable pageable
    ) {
        List<PostListDto> content = queryFactory
            .select(
                new QPostListDto(post.title, post.subCategory.name, post.likes.size(),
                    member.nickname, member.image
                ))
            .from(post)
            .leftJoin(post.member, member)
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

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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