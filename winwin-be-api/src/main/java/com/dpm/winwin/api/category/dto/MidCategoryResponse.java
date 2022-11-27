package com.dpm.winwin.api.category.dto;

import com.dpm.winwin.domain.entity.category.MidCategory;

public record MidCategoryResponse(Long id, String name) {

    public static MidCategoryResponse of(MidCategory midCategory){
        return new MidCategoryResponse(midCategory.getId(), midCategory.getName());
    }
}
