package com.dpm.winwin.api.post.dto.request;

import com.dpm.winwin.domain.dto.link.LinkDto;

public record LinkRequest(Long id, String content) {

    public LinkDto toDto(){
        return new LinkDto(this.id, this.content);
    }
}
