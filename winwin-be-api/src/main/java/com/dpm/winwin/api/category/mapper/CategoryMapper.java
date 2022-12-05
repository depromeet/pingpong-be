package com.dpm.winwin.api.category.mapper;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryOfMainResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static MainCategoryResponse toMainResponse(MainCategory mainCategory) {
        return new MainCategoryResponse(mainCategory.getId(), mainCategory.getName(),
            mainCategory.getImage(),
            mainCategory.getMidCategories().stream()
                .map(CategoryMapper::toMidResponseOfMain)
                .toList());
    }

    public static MidCategoryOfMainResponse toMidResponseOfMain(MidCategory midCategory) {
        return new MidCategoryOfMainResponse(midCategory.getId(), midCategory.getName());
    }

    public static MidCategoryResponse toMidResponse(MidCategory midCategory) {
        return new MidCategoryResponse(midCategory.getId(), midCategory.getName(),
            midCategory.getSubCategories().stream()
                .map(CategoryMapper::toSubResponse)
                .toList()
        );
    }

    public static SubCategoryResponse toSubResponse(SubCategory subCategory) {
        return new SubCategoryResponse(subCategory.getId(), subCategory.getName());
    }
}
