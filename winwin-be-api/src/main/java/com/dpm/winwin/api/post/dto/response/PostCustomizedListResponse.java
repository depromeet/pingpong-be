package com.dpm.winwin.api.post.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PostCustomizedListResponse(
    List<PostCustomizedResponse> content,
    long totalElements,
    int totalPages,
    boolean hasNextPages
) {

    public static PostCustomizedListResponse of(Page page) {
        return new PostCustomizedListResponse(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext()
        );
    }

}
