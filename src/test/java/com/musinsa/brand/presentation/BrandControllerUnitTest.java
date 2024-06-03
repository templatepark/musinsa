package com.musinsa.brand.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.brand.application.BrandService;
import com.musinsa.brand.application.dto.BrandSaveRequest;
import com.musinsa.brand.presentation.api.BrandController;

@WebMvcTest(BrandController.class)
class BrandControllerUnitTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private BrandService brandService;

    @DisplayName("브랜드 추가")
    @Test
    void test() throws Exception {
        // given
        String brandName = "musinsa";
        BrandSaveRequest request = new BrandSaveRequest(brandName);
        when(brandService.saveBrand(any())).thenReturn(1L);

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/brands")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/v1/brands/1"));
    }
}
