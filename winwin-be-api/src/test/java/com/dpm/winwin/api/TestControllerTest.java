package com.dpm.winwin.api;

import com.dpm.winwin.api.utils.RestDocsTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.dpm.winwin.api.utils.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerTest extends RestDocsTestSupport {

    @Test
    @DisplayName("restdocs 가 성공적으로 작동하는지 테스트한다.")
    void restdocsTest() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/"));

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("key").type(JsonFieldType.STRING).description("키 값").attributes(field("constraint", "3자 이내"))
                )
            ));
    }
}