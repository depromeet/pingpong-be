package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.dto.post.MyPagePostDto;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.dto.request.PostCustomizedConditionRequest;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Optional<Post> getByIdAndMemberId(Long memberId, Long postId);

    Page<Post> getAllByIsShareAndMidCategory(Long memberId, PostListConditionRequest condition, Pageable pageable);

    Page<Post> getAllByMemberTalents(Long memberId, PostCustomizedConditionRequest condition, Pageable pageable);

    Optional<Post> getByIdFetchJoin(Long postId);

    Page<MyPagePostDto> getAllByMemberId(Long memberId, Pageable pageable);

    Optional<Integer> getMemberLikeByMemberId(Long memberId);

    Boolean hasLikeByMemberId(Long postId, Long memberId);
}
