package com.dpm.winwin.api.post.dto.response;

import static com.dpm.winwin.domain.entity.post.Likes.changeFormatCountToString;

import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;

public record PostReadResponse(
    Long id,
    String title,
    String content,
    boolean isShare,
    String subCategory,
    List<LinkResponse> links,
    String chatLink,
    String likes,
    String takenContent,
    List<String> takenTalents,
    ExchangeType exchangeType,
    ExchangePeriod exchangePeriod,
    ExchangeTime exchangeTime,
    Long memberId,
    String nickname,
    String image,
    String ranks,
    Boolean isLike,
    String backgroundImage
) {

    public static PostReadResponse from(
        Long id,
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
        Boolean isLike,
        String backgroundImage) {
        return new PostReadResponse(
            id,
            title,
            content,
            isShare,
            subCategory,
            links,
            chatLink,
            changeFormatCountToString(likes),
            takenContent,
            takenTalents,
            exchangeType,
            exchangePeriod,
            exchangeTime,
            memberId,
            nickname,
            image,
            ranks,
            isLike,
            backgroundImage
        );
    }
}
