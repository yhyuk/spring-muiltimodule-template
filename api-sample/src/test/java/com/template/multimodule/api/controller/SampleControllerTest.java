package com.template.multimodule.api.controller;

import com.template.multimodule.api.dto.SampleResponse;
import com.template.multimodule.api.service.SampleService;
import com.template.multimodule.common.exception.BusinessException;
import com.template.multimodule.common.exception.ErrorCode;
import com.template.multimodule.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {SampleController.class, GlobalExceptionHandler.class})
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SampleService sampleService;

    @Test
    @DisplayName("GET /api/samples - 전체 조회")
    void findAll() throws Exception {
        // given
        SampleResponse response = SampleResponse.builder()
                .id(1L)
                .name("test")
                .description("desc")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        given(sampleService.findAll()).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/samples"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("test"));
    }

    @Test
    @DisplayName("GET /api/samples/{id} - 존재하지 않는 ID")
    void findById_notFound() throws Exception {
        // given
        given(sampleService.findById(999L))
                .willThrow(new BusinessException(ErrorCode.SAMPLE_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/samples/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("S001"));
    }

    @Test
    @DisplayName("POST /api/samples - 생성 성공")
    void create_success() throws Exception {
        // given
        SampleResponse response = SampleResponse.builder()
                .id(1L)
                .name("new-sample")
                .description("new-desc")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        given(sampleService.create(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"new-sample\",\"description\":\"new-desc\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("new-sample"));
    }

    @Test
    @DisplayName("POST /api/samples - Validation 실패 (name 빈값)")
    void create_validationFail() throws Exception {
        // when & then
        mockMvc.perform(post("/api/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"desc\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("DELETE /api/samples/{id} - 삭제 성공")
    void delete_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/samples/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
