package com.dpm.winwin.api.category.service;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.api.category.mapper.CategoryMapper;
import com.dpm.winwin.domain.repository.category.MainCategoryRepository;
import com.dpm.winwin.domain.repository.category.MidCategoryRepository;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final MainCategoryRepository mainCategoryRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public List<MainCategoryResponse> getAllMainCategories() {
        return mainCategoryRepository.findAll().stream()
            .map(CategoryMapper::toMainResponse)
            .toList();
    }

    public List<MidCategoryResponse> getAllMidCategories(Long mainCategoryId) {
        return midCategoryRepository.getAll(mainCategoryId).stream()
            .map(CategoryMapper::toMidResponse)
            .toList();
    }

    public List<SubCategoryResponse> getAllSubCategories(Long midCategoryId) {
        return subCategoryRepository.getAll(midCategoryId).stream()
            .map(CategoryMapper::toSubResponse)
            .toList();
    }

    public List<SubCategoryResponse> getTakenTalentsByMemberId(Long memberId) {
        return subCategoryRepository.getTakenTalentsByMemberId(memberId).stream()
            .map(CategoryMapper::toSubResponse).toList();
    }
}
