package com.dpm.winwin.domain.repository.category;

import com.dpm.winwin.domain.entity.category.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long>, CustomMainCategoryRepository {
}
