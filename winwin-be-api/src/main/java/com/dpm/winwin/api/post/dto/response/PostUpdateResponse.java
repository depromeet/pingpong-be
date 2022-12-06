package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.List;

public record PostUpdateResponse(
    long id,
    String title,
    String content,
    boolean isShare,
    String mainCategory,
    String midCategory,
    String subCategory,
    List<LinkResponse> links,
    String chatLink,
    List<String> takenTalents,
    String takenContent,
    ExchangeType exchangeType,
    ExchangePeriod exchangePeriod,
    ExchangeTime exchangeTime
) {

}
