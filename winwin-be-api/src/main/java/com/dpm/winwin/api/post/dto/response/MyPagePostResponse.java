package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.dto.post.MyPagePostDto;
import java.util.List;

public record MyPagePostResponse(Long id, String title, String subCategory, boolean isShare, List<String> takenTalent,
                                 Long likes) {

    public static MyPagePostResponse of(MyPagePostDto myPagePostDto) {
        return new MyPagePostResponse(myPagePostDto.id(), myPagePostDto.title(),
            myPagePostDto.subCategory(), myPagePostDto.isShare(), myPagePostDto.takenTalents(), myPagePostDto.likes());
    }
}
