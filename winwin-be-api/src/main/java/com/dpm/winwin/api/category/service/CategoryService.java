package com.dpm.winwin.api.category.service;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.domain.repository.category.MainCategoryRepository;
import com.dpm.winwin.domain.repository.category.MidCategoryRepository;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final MainCategoryRepository mainCategoryRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public List<MainCategoryResponse> getMainCategories() {
        return mainCategoryRepository.findAll().stream()
            .map(MainCategoryResponse::of)
            .toList();
    }

    public List<MidCategoryResponse> getMidCategories() {
        return midCategoryRepository.findAll().stream()
            .map(MidCategoryResponse::of)
            .toList();
    }

    public List<MidCategoryResponse> getMidCategoriesByMainId(Long mainCategoryId) {
        return midCategoryRepository.findAllByMainCategoryId(mainCategoryId).stream()
            .map(MidCategoryResponse::of)
            .toList();
    }

    public List<SubCategoryResponse> getSubCategories() {
        return subCategoryRepository.findAll().stream()
            .map(SubCategoryResponse::of)
            .toList();
    }

    public List<SubCategoryResponse> getSubCategoriesByMidId(Long midCategoryId) {
        return subCategoryRepository.findAllByMidCategoryId(midCategoryId).stream()
            .map(SubCategoryResponse::of)
            .toList();
    }
}
