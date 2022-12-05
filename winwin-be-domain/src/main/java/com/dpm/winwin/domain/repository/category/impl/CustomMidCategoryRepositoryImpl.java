package com.dpm.winwin.domain.repository.category.impl;

import static com.dpm.winwin.domain.entity.category.QMidCategory.midCategory;

import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.repository.category.CustomMidCategoryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
@RequiredArgsConstructor
public class CustomMidCategoryRepositoryImpl implements CustomMidCategoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MidCategory> getAll(Long mainCategoryId) {
        return queryFactory.selectFrom(midCategory)
            .where(mainCategoryIdEq(mainCategoryId))
            .fetch();
    }

    private BooleanExpression mainCategoryIdEq(Long mainCategoryId) {
        if (ObjectUtils.isEmpty(mainCategoryId)) {
            return null;
        }
        return midCategory.mainCategory.id.eq(mainCategoryId);
    }
}
