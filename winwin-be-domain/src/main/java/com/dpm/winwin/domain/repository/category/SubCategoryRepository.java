package com.dpm.winwin.domain.repository.category;

import com.dpm.winwin.domain.entity.category.SubCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findAllByMidCategoryId(Long midCategoryId);
}
