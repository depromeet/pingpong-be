package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Post getPostByMemberId(Long memberId, Long postId);
    Page<Post> getAllByIsShareAndMidCategory(PostListConditionRequest condition, Pageable pageable);
}
