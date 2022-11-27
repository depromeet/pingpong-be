package com.dpm.winwin.api.category.dto;

import com.dpm.winwin.domain.entity.category.MainCategory;

public record MainCategoryResponse(Long id, String name, String images) {

    public static MainCategoryResponse of(MainCategory mainCategory){
        return new MainCategoryResponse(mainCategory.getId(), mainCategory.getName(),
            mainCategory.getImage());
    }
}
