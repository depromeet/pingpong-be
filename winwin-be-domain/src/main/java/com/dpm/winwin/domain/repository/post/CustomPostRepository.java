package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.post.dto.PostMemberDto;
import java.util.List;
import java.util.Optional;

public interface CustomPostRepository {

    Optional<List<PostMemberDto>> getAllByIsShareAndMidCategory(Boolean isShare, Long midCategory);
}
