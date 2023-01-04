package com.dpm.winwin.api.report.controller;

import static com.dpm.winwin.api.member.controller.MemberControllerTest.MEMBER_ID;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.report.dto.request.ReportRequest;
import com.dpm.winwin.api.report.dto.response.ReportResponse;
import com.dpm.winwin.api.report.service.ReportService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;

import com.dpm.winwin.api.utils.WithMockCustomUser;
import com.dpm.winwin.domain.entity.report.enums.ReportType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WithMockCustomUser
public class ReportControllerTest extends RestDocsTestSupport {

    static final String MEMBER_ID = "1";
    @MockBean
    private ReportService reportService;

    @Test
    public void 게시물을_신고한다() throws Exception {
        // given
        Long reportedId = Long.parseLong(MEMBER_ID);
        Long postId = 1L;
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
                post("/api/v1/reports/posts/{postId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("신고할 게시물 id")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("신고 내용")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("성공 여부"),
                                fieldWithPath("data.reporterId").type(JsonFieldType.NUMBER)
                                        .description("신고자 id"),
                                fieldWithPath("data.reportType").type(JsonFieldType.STRING)
                                        .description("신고 유형"),
                                fieldWithPath("data.typeId").type(JsonFieldType.NUMBER)
                                        .description("신고할 게시글 id")
                        )
                ));
    }

}
