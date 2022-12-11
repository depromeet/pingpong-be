package com.dpm.winwin.api.report.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.report.dto.request.ReportRequest;
import com.dpm.winwin.api.report.dto.response.ReportResponse;
import com.dpm.winwin.api.report.service.ReportService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;

import com.dpm.winwin.domain.entity.report.enums.ReportType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class ReportControllerTest extends RestDocsTestSupport {

    @MockBean
    private ReportService reportService;

    @Test
    public void 게시물에서_신고한다() throws Exception {
        // given
        Long reportedId = 1L;
        Long postId = 10L;
        ReportType reportType = ReportType.POST;

        ReportRequest request = new ReportRequest(
                "사용자 정보가 부정확해요."
        );

        ReportResponse response = new ReportResponse(
                reportedId,
                reportType,
                postId
        );

        // when
        given(reportService.reportPost(reportedId, postId, reportType, request))
                .willReturn(response);

        ResultActions result = mockMvc.perform(
                post("/api/v1/reports/posts/{postId}",10L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("신고 내용")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("성공 여부"),
                                fieldWithPath("data.reporterId").type(JsonFieldType.NUMBER)
                                        .description("신고자 Id"),
                                fieldWithPath("data.reportType").type(JsonFieldType.STRING)
                                        .description("신고 타입"),
                                fieldWithPath("data.typeId").type(JsonFieldType.NUMBER)
                                        .description("신고글 Id")
                        )
                ));
    }

}
