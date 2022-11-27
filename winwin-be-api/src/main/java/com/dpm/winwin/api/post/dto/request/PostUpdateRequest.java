package com.dpm.winwin.api.post.dto.request;

import com.dpm.winwin.domain.dto.post.PostUpdateDto;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;

public record PostUpdateRequest(
    Long id,
    String title,
    String content,
    boolean isShare,
    Long mainCategory,
    Long midCategory,
    Long subCategory,
    List<LinkRequest> links,
    List<Long> takeCategories,
    String takenContent,
    String chatLink,
    ExchangeType exchangeType,
    ExchangePeriod exchangePeriod,
    ExchangeTime exchangeTime
) {

    public PostUpdateDto toDto() {
        return new PostUpdateDto(this.title, this.content, this.isShare,
            this.links().stream().map(LinkRequest::toDto).toList(), this.takenContent,
            this.chatLink, this.exchangeType, this.exchangePeriod, this.exchangeTime);
    }

    public List<LinkRequest> getExistentLinks() {
        return this.links()
            .stream()
            .filter(link -> link.id() != null)
            .toList();
    }
}
