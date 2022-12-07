package com.dpm.winwin.api.report.dto.response;

import com.dpm.winwin.domain.entity.report.Report;
import com.dpm.winwin.domain.entity.report.enums.ReportType;

public record ReportResponse(
        Long reporterId,
        ReportType reportType,
        Long typeId
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getReporterId(),
                report.getType(),
                report.getTypeId()
        );
    }
}
