package com.musinsa.brand.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
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
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.brand.application.BrandService;
import com.musinsa.brand.application.dto.BrandSaveRequest;
import com.musinsa.brand.presentation.api.BrandController;
import com.musinsa.common.support.AbstractRestDocsTests;

@WebMvcTest(BrandController.class)
class BrandControllerUnitTest extends AbstractRestDocsTests {

    @Autowired private ObjectMapper objectMapper;

    @MockBean private BrandService brandService;

    @DisplayName("brand 저장 성공: request가 valid한 경우")
    @Test
    void saveBrand_WhenValidRequest_ShouldReturnBrandIdLocation() throws Exception {
        // given
        Long expectedId = 1L;
        String brandName = "musinsa";
        BrandSaveRequest request = new BrandSaveRequest(brandName);
        when(brandService.saveBrand(any())).thenReturn(expectedId);

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/brands")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isCreated())
                .andExpect(
                        header().string(
                                        HttpHeaders.LOCATION,
                                        String.format("/api/v1/brands/%d", expectedId)));
    }

    @DisplayName("brand 저장 성공: request가 invalid한 경우")
    @Test
    void saveBrand_WhenInValidRequest_ShouldThrowException() throws Exception {
        // given
        String brandName = "";
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
        result.andExpect(status().isBadRequest());
        then(brandService).shouldHaveNoInteractions();
    }
}
