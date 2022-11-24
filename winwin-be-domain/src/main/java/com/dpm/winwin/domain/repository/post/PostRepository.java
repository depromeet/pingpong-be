package com.dpm.winwin.domain.repository.post;

import com.dpm.winwin.domain.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
}
