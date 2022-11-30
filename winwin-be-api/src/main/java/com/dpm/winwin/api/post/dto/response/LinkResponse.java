package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.link.Link;

public record LinkResponse(
        Long id,
        String content
) {
    public static LinkResponse of(Link link) {
        return new LinkResponse(
                link.getId(),
                link.getContent()
        );
    }
}
