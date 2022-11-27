package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import com.dpm.winwin.domain.repository.post.dto.PostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Page<PostListDto> getAllByIsShareAndMidCategory(PostListConditionRequest condition, Pageable pageable);
}
