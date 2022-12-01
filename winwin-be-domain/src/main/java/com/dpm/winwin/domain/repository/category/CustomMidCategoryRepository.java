package com.dpm.winwin.domain.repository.category;

import com.dpm.winwin.domain.entity.category.MidCategory;
import java.util.List;

public interface CustomMidCategoryRepository {

    List<MidCategory> getAll(Long mainCategoryId);
}
