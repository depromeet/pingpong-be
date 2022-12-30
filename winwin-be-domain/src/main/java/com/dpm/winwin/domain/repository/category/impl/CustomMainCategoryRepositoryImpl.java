package com.dpm.winwin.domain.repository.category.impl;

import static com.dpm.winwin.domain.entity.category.QMainCategory.mainCategory;

import com.dpm.winwin.domain.repository.category.CustomMainCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomMainCategoryRepositoryImpl implements CustomMainCategoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public String getBackgroundImageById(Long id) {
        return queryFactory.select(mainCategory.backgroundImage)
            .from(mainCategory)
            .where(mainCategory.id.eq(id))
            .fetchOne();
    }
}
