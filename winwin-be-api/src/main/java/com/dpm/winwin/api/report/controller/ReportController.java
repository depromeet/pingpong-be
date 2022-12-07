package com.dpm.winwin.api.report.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.report.dto.request.ReportRequest;
import com.dpm.winwin.api.report.dto.response.ReportResponse;
import com.dpm.winwin.api.report.service.ReportService;
import com.dpm.winwin.domain.entity.report.enums.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/posts/{postId}")
    public BaseResponseDto<ReportResponse> reportPost(
            @PathVariable Long postId,
            @RequestBody ReportRequest reportRequest){
        Long reporterId = 1L; //임시 회원
        ReportResponse reportResponse = reportService.reportPost(reporterId, postId, ReportType.POST, reportRequest);
        return BaseResponseDto.ok(reportResponse);
    }
}
