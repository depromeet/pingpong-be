package com.dpm.winwin.api.report.dto.response;

import com.dpm.winwin.domain.entity.report.Report;

public record ReportResponse(
        Long reporterId,
        Long reportedId
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getReporterId(),
                report.getReportedId()
        );
    }
}
