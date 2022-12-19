package com.dpm.winwin.api.post.dto.request;

import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PostAddRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 30, message = "제목이 30자 이하이어야 합니다.")
    String title,

    @NotBlank(message = "상세 설명은 필수입니다.")
    @Size(max = 30, message = "상세 설명이 300자 이하이어야 합니다.")
    String content,

    @NotNull(message = "재능 나눔 유무는 필수입니다.")
    Boolean isShare,

    @NotNull(message = "재능 카테고리를 선택해 주세요.")
    Long subCategoryId,

    @Size(max = 3, message = "링크는 3개 이하이어야 합니다.")
    List<String> links,

    @NotBlank(message = "오픈 채팅 링크는 필수입니다.")
    String chatLink,

    @Size(max = 5, message = "받고 싶은 재능은 최대 5개입니다.")
    List<Long> takenTalentIds,

    @Size(max = 300, message = "받고 싶은 재능의 상세 설명은 500자 이하이어야 합니다.")
    String takenContent,

    @NotNull(message = "재능 나눔 환경을 선택해 주세요")
    ExchangeType exchangeType,

    @NotNull(message = "재능 나눔 기간을 선택해 주세요")
    ExchangePeriod exchangePeriod,

    @NotNull(message = "선호하는 시간대를 선택해 주세요")
    ExchangeTime exchangeTime
) {

    public Post toEntity() {
        return Post.builder()
            .title(this.title)
            .content(this.content)
            .isShare(this.isShare)
            .chatLink(this.chatLink)
            .takenContent(this.takenContent)
            .exchangeType(this.exchangeType)
            .exchangePeriod(this.exchangePeriod)
            .exchangeTime(this.exchangeTime)
            .build();
    }
}
