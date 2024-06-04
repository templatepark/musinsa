package com.musinsa.product.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.common.support.AbstractRestDocsTests;
import com.musinsa.product.application.*;
import com.musinsa.product.application.dto.*;
import com.musinsa.product.presentation.api.ProductQueryController;

@WebMvcTest(ProductQueryController.class)
class ProductQueryControllerUnitTest extends AbstractRestDocsTests {

    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProductQueryService productQueryService;

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 데이터가 존재할 경우")
    @Test
    void getCategoryLowestPrices_WhenDataExist_ShouldReturnData() throws Exception {
        // given
        CategoryLowestPriceResponse expectedResponse =
                new CategoryLowestPriceResponse(
                        List.of(
                                new CategoryBrandPriceResponse(
                                        "CATEGORY_A", "BRAND_A", new BigDecimal(100L))),
                        new BigDecimal(100L));
        given(productQueryService.getCategoryLowestPrices()).willReturn(expectedResponse);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/category-lowest-prices")
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getCategoryLowestPrices_WhenDataNotExist_ShouldReturnData() throws Exception {
        // given
        CategoryLowestPriceResponse expectedResponse =
                new CategoryLowestPriceResponse(List.of(), new BigDecimal(0L));
        given(productQueryService.getCategoryLowestPrices()).willReturn(expectedResponse);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/category-lowest-prices")
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 데이터가 존재할 경우")
    @Test
    void getLowestTotalBrandPrice_WhenDataExist_ShouldReturnData() throws Exception {
        // given
        BrandLowestPriceResponse expectedResponse =
                new BrandLowestPriceResponse(
                        new BrandCategoryTotalResponse(
                                "BRAND_A",
                                List.of(
                                        new CategoryPriceResponse(
                                                "CATEGORY_A", new BigDecimal(100L))),
                                new BigDecimal(100L)));
        given(productQueryService.getLowestTotalBrandPrice()).willReturn(expectedResponse);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/lowest-total-brand-price")
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        then(productQueryService).should().getLowestTotalBrandPrice();
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getLowestTotalBrandPrice_WhenDataNotExist_ShouldReturnEmpty() throws Exception {
        // given
        BrandLowestPriceResponse expectedResponse =
                new BrandLowestPriceResponse(
                        new BrandCategoryTotalResponse(null, List.of(), new BigDecimal(0L)));
        given(productQueryService.getLowestTotalBrandPrice()).willReturn(expectedResponse);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/lowest-total-brand-price")
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        then(productQueryService).should().getLowestTotalBrandPrice();
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회: 데이터가 존재할 경우")
    @Test
    void getLowestAndHighestPricesByCategoryName_WhenDataExist_ShouldReturnData() throws Exception {
        // given
        String categoryName = "CATEGORY_A";
        CategoryLowestAndHighestPriceResponse expectedResponse =
                new CategoryLowestAndHighestPriceResponse(
                        categoryName,
                        List.of(new BrandPriceResponse("BRAND_A", new BigDecimal(10L))),
                        List.of(new BrandPriceResponse("BRAND_B", new BigDecimal(100L))));
        given(productQueryService.getLowestAndHighestPricesByCategoryName(anyString()))
                .willReturn(expectedResponse);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/category-lowest-highest-prices")
                                .param("categoryName", categoryName)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        then(productQueryService).should().getLowestAndHighestPricesByCategoryName(anyString());
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getLowestAndHighestPricesByCategoryName_WhenDataNotExist_ShouldReturnData()
            throws Exception {
        // given
        String categoryName = "CATEGORY_A";
        CategoryLowestAndHighestPriceResponse expectedResponse =
                new CategoryLowestAndHighestPriceResponse(categoryName, List.of(), List.of());
        given(productQueryService.getLowestAndHighestPricesByCategoryName(anyString()))
                .willReturn(expectedResponse);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/category-lowest-highest-prices")
                                .param("categoryName", categoryName)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        then(productQueryService).should().getLowestAndHighestPricesByCategoryName(anyString());
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회 실패: 입력이 empty, null일 경우")
    @ParameterizedTest(name = "[{index}]: updateTargetId={0}, request={1}")
    @MethodSource("provideParametersForGetLowestAndHighestPrice")
    void getLowestAndHighestPricesByCategoryName_WhenCategoryNameNullOrEmpty_ShouldReturnData(
            String categoryName) throws Exception {
        // given, when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/products/category-lowest-highest-prices")
                                .param("categoryName", categoryName)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isBadRequest());
        then(productQueryService).shouldHaveNoInteractions();
    }

    static Stream<Arguments> provideParametersForGetLowestAndHighestPrice() {
        return Stream.of(Arguments.of(""), Arguments.of((String) null));
    }
}
