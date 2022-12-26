package com.dpm.winwin.domain.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record MyPagePostDto(
    Long id,
    String title,
    String subCategory,
    boolean isShare,
    List<String> takenTalents,
    int likes) {

    @QueryProjection
    public MyPagePostDto {
    }

}
