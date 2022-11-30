package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.member.Ranks;
import java.util.List;

public record PostReadResponse(Long id, String title, String content, boolean isShare,
                               String mainCategory, String midCategory, String subCategory,
                               List<LinkResponse> links, String chatLink, int likes,
                               String exchangeType, String exchangePeriod, String exchangeTime, Long memberId,
                               String nickname, String image, Ranks ranks) {

    public static PostReadResponse from(Long id, String title, String content, boolean isShare,
        String mainCategory, String midCategory, String subCategory,
        List<LinkResponse> links, String chatLink, int likes, String exchangeType,
        String exchangePeriod, String exchangeTime, Long memberId, String nickname, String image, Ranks ranks) {
        return new PostReadResponse(id, title, content, isShare, mainCategory, midCategory,
            subCategory, links, chatLink, likes, exchangeType, exchangePeriod,
            exchangeTime, memberId, nickname, image, ranks);
    }
}
