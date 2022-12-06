package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.dto.post.MyPagePostDto;

public record MyPagePostResponse(Long id, String title, String subCategory, boolean isShare,
                                 Long likes) {

    public static MyPagePostResponse of(MyPagePostDto myPagePostDto) {
        return new MyPagePostResponse(myPagePostDto.id(), myPagePostDto.title(),
            myPagePostDto.subCategory(), myPagePostDto.isShare(), myPagePostDto.likes());
    }
}
