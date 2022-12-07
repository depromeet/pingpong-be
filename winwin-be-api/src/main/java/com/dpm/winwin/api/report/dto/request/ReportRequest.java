package com.dpm.winwin.api.report.dto.request;

import com.dpm.winwin.domain.entity.report.Report;
import com.dpm.winwin.domain.entity.report.enums.ReportType;

public record ReportRequest(
        String content
) {
    public Report toEntity(Long reporterId, Long reportedId, ReportType reportType, Long typeId) {
        return Report.builder()
                .reporterId(reporterId)
                .reportedId(reportedId)
                .content(this.content)
                .type(reportType)
                .typeId(typeId)
                .build();
    }
}