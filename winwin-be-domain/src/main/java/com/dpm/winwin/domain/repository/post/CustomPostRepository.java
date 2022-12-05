package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.dto.post.MyPagePostDto;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Optional<Post> getByIdAndMemberId(Long memberId, Long postId);

    Page<Post> getAllByIsShareAndMidCategory(PostListConditionRequest condition, Pageable pageable);

    Optional<Post> getByIdFetchJoin(Long postId);

    Page<MyPagePostDto> getAllByMemberId(Long memberId, Pageable pageable);
}
