package com.dpm.winwin.domain.repository.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record PostListDto(
    String title,
    String subCategory,
    Integer likes,
    String member,
    String image
//    String ranks,
//    List<String> takenTalents
) {

    @QueryProjection
    public PostListDto {
    }
}
