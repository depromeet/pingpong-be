package com.dpm.winwin.domain.repository.category;

import com.dpm.winwin.domain.entity.category.MidCategory;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidCategoryRepository extends JpaRepository<MidCategory, Long> {

    List<MidCategory> findAllByMainCategoryId(Long mainCategoryId);
}
