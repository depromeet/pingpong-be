package com.dpm.winwin.domain.dto.link;

import com.dpm.winwin.domain.entity.link.Link;

public record LinkDto(Long id, String content) {
    public static Link toEntity(LinkDto linkDto){
    return Link.from(linkDto.content());
    }
}
