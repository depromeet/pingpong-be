package com.dpm.winwin.domain.dto.post;

import com.dpm.winwin.domain.dto.link.LinkDto;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;

public record PostUpdateDto(
    String title,
    String content,
    boolean isShare,
    List<LinkDto> links,
    String chatLink,
    String takenContent,
    ExchangeType exchangeType,
    ExchangePeriod exchangePeriod,
    ExchangeTime exchangeTime
) {

}
