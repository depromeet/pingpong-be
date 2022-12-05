package com.dpm.winwin.api.post.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record MyPagePostListResponse(
    List<MyPagePostResponse> content,
    long totalElements,
    int totalPages,
    boolean hasNextPages) {

    public static MyPagePostListResponse of(Page page) {
        return new MyPagePostListResponse(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext()
        );
    }

}
