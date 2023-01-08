package com.dpm.winwin.domain.repository.post.impl;

import static com.dpm.winwin.domain.entity.category.QMainCategory.mainCategory;
import static com.dpm.winwin.domain.entity.category.QMidCategory.midCategory;
import static com.dpm.winwin.domain.entity.category.QSubCategory.subCategory;
import static com.dpm.winwin.domain.entity.link.QLink.link;
import static com.dpm.winwin.domain.entity.member.QMember.member;
import static com.dpm.winwin.domain.entity.member.QMemberTalent.memberTalent;
import static com.dpm.winwin.domain.entity.post.QLikes.likes;
import static com.dpm.winwin.domain.entity.post.QPost.post;
import static com.dpm.winwin.domain.entity.report.QReport.report;

import com.dpm.winwin.domain.dto.post.MyPagePostDto;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.member.MemberTalent;
import com.dpm.winwin.domain.entity.member.enums.TalentType;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.report.Report;
import com.dpm.winwin.domain.entity.report.enums.ReportType;
import com.dpm.winwin.domain.repository.post.CustomPostRepository;
import com.dpm.winwin.domain.repository.post.dto.request.PostCustomizedConditionRequest;
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
import org.springframework.util.ObjectUtils;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> getByIdAndMemberId(Long memberId, Long postId) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member)
                .where(post.id.eq(postId), member.id.eq(memberId))
                .fetchOne());
    }

    @Override
    public Optional<Integer> getMemberLikeByMemberId(Long memberId) {
        return Optional.ofNullable(
            queryFactory
                .select(post.likes.size())
                .from(post)
                .leftJoin(post.member, member)
                .where(member.id.eq(memberId))
                .fetchOne());
    }

    @Override
    public Boolean hasLikeByMemberId(Long postId, Long memberId) {
        return queryFactory.selectFrom(likes)
            .where(likes.member.id.eq(memberId), likes.post.id.eq(postId))
            .fetchOne() != null;
    }

    @Override
    public Page<Post> getAllByIsShareAndCategory(Long memberId,
                                                 PostListConditionRequest condition,
                                                 Pageable pageable
    ) {
        List<Long> reportedPostIds = getReportedPostIds(memberId);
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
                subCategoryEq(condition.subCategory()),
                post.id.in(reportedPostIds).not()
            )
            .orderBy(post.createdDate.desc())
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
                subCategoryEq(condition.subCategory()),
                post.id.in(reportedPostIds).not()
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

    @Override
    public Page<MyPagePostDto> getAllByMemberId(Long memberId, Pageable pageable) {
        List<Post> posts = queryFactory.selectFrom(post)
            .leftJoin(post.likes, likes).fetchJoin()
            .leftJoin(post.subCategory, subCategory).fetchJoin()
            .leftJoin(post.member, member).fetchJoin()
            .where(post.member.id.eq(memberId))
            .orderBy(post.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<MyPagePostDto> results = posts.stream().map(
                post -> new MyPagePostDto(
                    post.getId(),
                    post.getTitle(),
                    post.getSubCategory().getName(),
                    post.isShare(),
                    post.getTakenTalents().stream()
                        .map(takenTalent -> takenTalent.getTalent().getName())
                        .toList(),
                    post.getLikes().size()))
            .toList();

        JPAQuery<Long> countQuery = queryFactory.select(post.count())
            .from(post)
            .leftJoin(post.likes, likes)
            .leftJoin(post.subCategory, subCategory)
            .leftJoin(post.member, member)
            .where(post.member.id.eq(memberId));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }
  
    @Override
    public Page<Post> getAllByMemberTalents(Long memberId,
                                            PostCustomizedConditionRequest condition,
                                            Pageable pageable) {
        List<SubCategory> subCategories = queryFactory.selectFrom(memberTalent)
            .leftJoin(memberTalent.member, member).fetchJoin()
            .where(
                memberTalent.member.id.eq(memberId),
                memberTalent.type.eq(TalentType.TAKE)
            )
            .fetch()
            .stream().map(MemberTalent::getTalent)
            .toList();

        List<Long> reportedPostIds = getReportedPostIds(memberId);
        List<Post> posts = queryFactory.selectFrom(post)
            .leftJoin(post.member, member).fetchJoin()
            .leftJoin(post.likes, likes).fetchJoin()
            .leftJoin(post.subCategory, subCategory).fetchJoin()
            .where(
                member.id.eq(memberId).not(),
                post.subCategory.in(subCategories),
                subCategoryEq(condition.subCategoryId()),
                post.id.in(reportedPostIds).not()
            )
            .orderBy(post.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(post.count())
            .from(post)
            .leftJoin(post.member, member)
            .leftJoin(post.likes, likes)
            .leftJoin(post.subCategory, subCategory)
            .where(
                member.id.eq(memberId).not(),
                post.subCategory.in(subCategories),
                subCategoryEq(condition.subCategoryId()),
                post.id.in(reportedPostIds).not()
            );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    private List<Long> getReportedPostIds(Long memberId) {
        return queryFactory
            .selectFrom(report)
            .where(
                report.type.eq(ReportType.POST),
                report.reporterId.eq(memberId)
            )
            .fetch()
            .stream().map(Report::getTypeId)
            .toList();
    }

    private BooleanExpression isShareEq(Boolean isShare) {
        if (isShare != null && isShare) {
            return post.isShare.eq(true);
        }

        return null;
    }

    private BooleanExpression mainCategoryEq(Long mainCategoryId) {
        if (ObjectUtils.isEmpty(mainCategoryId) || mainCategoryId == 0) {
            return null;
        }
        return post.mainCategory.id.eq(mainCategoryId);
    }

    private BooleanExpression midCategoryEq(Long midCategoryId) {
        if (ObjectUtils.isEmpty(midCategoryId) || midCategoryId == 0) {
            return null;
        }
        return post.midCategory.id.eq(midCategoryId);
    }

    private BooleanExpression subCategoryEq(Long subCategoryId) {
        if (ObjectUtils.isEmpty(subCategoryId) || subCategoryId == 0) {
            return null;
        }
        return post.subCategory.id.eq(subCategoryId);
    }
}
