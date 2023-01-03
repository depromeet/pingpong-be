package com.dpm.winwin.api.category.controller;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.api.category.service.CategoryService;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.dto.PingPongMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/main")
    public BaseResponseDto<List<MainCategoryResponse>> getAllMainCategories() {
        return BaseResponseDto.ok(categoryService.getAllMainCategories());
    }

    @GetMapping("/mid")
    public BaseResponseDto<List<MidCategoryResponse>> getAllMidCategories(
        @RequestParam(required = false) Long mainCategoryId) {
        return BaseResponseDto.ok(categoryService.getAllMidCategories(mainCategoryId));
    }

    @GetMapping("/sub")
    public BaseResponseDto<List<SubCategoryResponse>> getAllSubCategories(
        @RequestParam(required = false) Long midCategoryId) {
        return BaseResponseDto.ok(categoryService.getAllSubCategories(midCategoryId));
    }

    @GetMapping("/custom")
    public BaseResponseDto<List<SubCategoryResponse>> getCustomCategories(
        @AuthenticationPrincipal PingPongMember member) {
        return BaseResponseDto.ok(categoryService.getTakenTalentsByMemberId(member.getMemberId()));
    }
}
