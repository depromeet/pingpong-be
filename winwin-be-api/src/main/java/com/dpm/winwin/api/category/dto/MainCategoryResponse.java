package com.dpm.winwin.api.category.dto;


import java.util.List;

public record MainCategoryResponse(Long id, String name, String image, List<MidCategoryResponse> midCategories) {

    public static MainCategoryResponse of(Long id, String name, String image, List<MidCategoryResponse> midCategories) {
        return new MainCategoryResponse(id, name, image, midCategories);
    }
}
