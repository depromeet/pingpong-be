package com.dpm.winwin.api.category.dto;

import com.dpm.winwin.domain.entity.category.SubCategory;

public record SubCategoryResponse(Long id, String name) {

    public static SubCategoryResponse of(SubCategory subCategory){
        return new SubCategoryResponse(subCategory.getId(), subCategory.getName());
    }
}
