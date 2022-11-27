package com.dpm.winwin.api.post.dto.request;

import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;

public record PostUpdateRequest(
    Long id,
    String title,
    String content,
    boolean isShare,
    long mainCategory,
    long midCategory,
    long subCategory,
    List<LinkRequest> links,
    List<Long> takeCategories,
    String takenContent,
    String chatLink,
    ExchangeType exchangeType,
    ExchangePeriod exchangePeriod,
    ExchangeTime exchangeTime
) {

    public List<LinkRequest> getExistentLinks(){
        return this.links()
            .stream()
            .filter(link -> link.id() != null)
            .toList();
    }
}
