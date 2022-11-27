package com.dpm.winwin.api.category.controller;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.api.category.service.CategoryService;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/main")
    public BaseResponseDto<List<MainCategoryResponse>> getMainCategories(){
        return BaseResponseDto.ok(categoryService.getMainCategories());
    }

    @GetMapping("/mid")
    public BaseResponseDto<List<MidCategoryResponse>> getMidCategories(){
        return BaseResponseDto.ok(categoryService.getMidCategories());
    }

    @GetMapping("/mid/{mainCategoryId}")
    public BaseResponseDto<List<MidCategoryResponse>> getMidCategoriesByMainId(@PathVariable Long mainCategoryId){
        return BaseResponseDto.ok(categoryService.getMidCategoriesByMainId(mainCategoryId));
    }

    @GetMapping("/sub")
    public BaseResponseDto<List<SubCategoryResponse>> getSubCategories(){
        return BaseResponseDto.ok(categoryService.getSubCategories());
    }

    @GetMapping("/sub/{midCategoryId}")
    public BaseResponseDto<List<SubCategoryResponse>> getSubCategoriesByMidId(@PathVariable Long midCategoryId){
        return BaseResponseDto.ok(categoryService.getSubCategoriesByMidId(midCategoryId));
    }
}
