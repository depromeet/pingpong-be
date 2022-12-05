package com.dpm.winwin.api.category.dto;

import java.util.List;

public record MidCategoryResponse(Long id, String name, List<SubCategoryResponse> subCategories) {

}
