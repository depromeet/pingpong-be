package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;
import lombok.Builder;

@Builder
public record PostReadResponse(Long id,
                               String title,
                               String content,
                               boolean isShare,
                               String subCategory,
                               List<LinkResponse> links,
                               String chatLink,
                               int likes,
                               String takenContent,
                               List<String> takenTalents,
                               ExchangeType exchangeType,
                               ExchangePeriod exchangePeriod,
                               ExchangeTime exchangeTime,
                               Long memberId,
                               String nickname,
                               String image,
                               String ranks,
                               Boolean isLike) {

    public static PostReadResponse from(
        Long id,
        String title,
        String content,
        boolean isShare,
        String subCategory,
        List<LinkResponse> links,
        String chatLink,
        int likes,
        ExchangeType exchangeType,
        ExchangePeriod exchangePeriod,
        ExchangeTime exchangeTime,
        String takenContent,
        List<String> takenTalents,
        Long memberId,
        String nickname,
        String image,
        String ranks,
        Boolean isLike) {
        return PostReadResponse.builder()
            .id(id)
            .title(title)
            .content(content)
            .isShare(isShare)
            .subCategory(subCategory)
            .links(links)
            .chatLink(chatLink)
            .likes(likes)
            .exchangeType(exchangeType)
            .exchangePeriod(exchangePeriod)
            .exchangeTime(exchangeTime)
            .takenContent(takenContent)
            .takenTalents(takenTalents)
            .memberId(memberId)
            .nickname(nickname)
            .image(image)
            .ranks(ranks)
            .isLike(isLike)
            .build();
    }
}
