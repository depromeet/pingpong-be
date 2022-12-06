package com.dpm.winwin.domain.dto.post;

import com.querydsl.core.annotations.QueryProjection;

public record MyPagePostDto(Long id, String title, String subCategory, boolean isShare,
                            Long likes) {


    @QueryProjection
    public MyPagePostDto {
    }

}
