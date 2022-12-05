package com.dpm.winwin.domain.repository.category;

import com.dpm.winwin.domain.entity.category.SubCategory;
import java.util.List;

public interface CustomSubCategoryRepository {

    List<SubCategory> getAll(Long midCategoryId);
}
