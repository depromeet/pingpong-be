package com.dpm.winwin.api.post.dto.request;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;

import java.util.List;

public record PostAddRequest(
        String title,
        String content,
        boolean isShare,
        long mainCategoryId,
        long midCategoryId,
        long subCategoryId,
        List<String> links,
        List<Long> takeCategories,
        String takeContent,
        ExchangeType exchangeType,
        ExchangePeriod exchangePeriod,
        ExchangeTime exchangeTime
) {
    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .isShare(this.isShare)
                .exchangeType(this.exchangeType)
                .exchangePeriod(this.exchangePeriod)
                .exchangeTime(this.exchangeTime)
                .build();
    }
}
