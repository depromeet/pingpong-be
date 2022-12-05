package com.dpm.winwin.api.category.mapper;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static MainCategoryResponse toMainResponse(MainCategory mainCategory) {
        return new MainCategoryResponse(mainCategory.getId(), mainCategory.getName(), mainCategory.getImage(),
            mainCategory.getMidCategories().stream()
                .map(CategoryMapper::toMidResponse)
                .toList());
    }

    public static MidCategoryResponse toMidResponse(MidCategory midCategory) {
        return new MidCategoryResponse(midCategory.getId(), midCategory.getName(), midCategory.getImage(),
            midCategory.getSubCategories().stream()
                .map(CategoryMapper::toSubResponse)
                .toList()
        );
    }

    public static SubCategoryResponse toSubResponse(SubCategory subCategory) {
        return new SubCategoryResponse(subCategory.getId(), subCategory.getName(), subCategory.getImage());
    }
}
