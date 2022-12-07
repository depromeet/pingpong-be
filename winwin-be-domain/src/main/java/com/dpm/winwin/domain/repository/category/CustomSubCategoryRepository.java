package com.dpm.winwin.domain.repository.category;

import com.dpm.winwin.domain.entity.category.SubCategory;
import java.util.List;
import java.util.Optional;

public interface CustomSubCategoryRepository {

    Optional<SubCategory> getByIdWithMainCategoryAndMidCategory(Long subCategoryId);
    List<SubCategory> getAll(Long midCategoryId);

    List<SubCategory> getTakenTalentsByMemberId(Long memberId);
}
