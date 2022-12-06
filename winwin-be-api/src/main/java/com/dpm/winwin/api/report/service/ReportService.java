package com.dpm.winwin.api.report.service;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.report.dto.request.ReportRequest;
import com.dpm.winwin.api.report.dto.response.ReportResponse;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.report.Report;
import com.dpm.winwin.domain.entity.report.enums.ReportType;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.post.PostRepository;
import com.dpm.winwin.domain.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    public ReportResponse reportPost(Long memberId, Long postId,
                                       ReportType reportType, ReportRequest reportRequest){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        Report report = reportRequest.toEntity(memberId, post.getMember().getId(), reportType, postId);

        Report savedReport = reportRepository.save(report);

        return ReportResponse.from(savedReport);
    }

}
