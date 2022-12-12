package com.dpm.winwin.api.member.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerUploadImageTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void imageUpload() throws Exception {
        mockMvc.perform(post("/api/v1/members/image"))
                .andExpect(status().isOk());
    }
}
