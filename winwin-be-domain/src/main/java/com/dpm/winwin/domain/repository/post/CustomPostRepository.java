package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.repository.post.dto.PostListCondition;
import com.dpm.winwin.domain.repository.post.dto.PostMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Page<PostMemberDto> getAllByIsShareAndMidCategory(PostListCondition condition, Pageable pageable);
}
