package com.musinsa.product.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.AbstractRestDocsTests;
import com.musinsa.product.application.ProductService;
import com.musinsa.product.application.dto.ProductSaveRequest;
import com.musinsa.product.presentation.api.ProductController;

@WebMvcTest(ProductController.class)
class ProductControllerUnitTest extends AbstractRestDocsTests {

    //    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProductService productService;

    @DisplayName("product 저장 성공: request가 valid한 경우")
    @Test
    void saveProduct_WhenValidRequest_ShouldReturnProductIdLocation() throws Exception {
        // given
        Long expectedId = 1L;
        ProductSaveRequest request = new ProductSaveRequest(1L, 1L, new BigDecimal(1L));
        given(productService.saveProduct(any())).willReturn(expectedId);

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isCreated())
                .andExpect(
                        header().string(
                                        HttpHeaders.LOCATION,
                                        String.format("/api/v1/products/%d", expectedId)));
    }

    @DisplayName("product 저장 실패: request가 invalid한 경우")
    @ParameterizedTest(name = "[{index}]: brandId={0}, categoryId={1}, price={2}")
    @MethodSource("provideParametersForSaveProduct")
    void saveProduct_WhenInValidRequest_ShouldThrowException(
            Long brandId, Long categoryId, BigDecimal price) throws Exception {
        // given
        ProductSaveRequest request = new ProductSaveRequest(brandId, categoryId, price);

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));
        // TODO: 공통 에러처리(Advice)
        // then
        result.andExpect(status().isBadRequest());
        then(productService).shouldHaveNoInteractions();
    }

    static Stream<Arguments> provideParametersForSaveProduct() {
        return Stream.of(
                Arguments.of(0L, 1L, new BigDecimal(1L)),
                Arguments.of(1L, 0L, new BigDecimal(1L)),
                Arguments.of(1L, 1L, new BigDecimal(0L)));
    }
    //
    //    @DisplayName("product 삭제 성공: request가 valid한 경우")
    //    @Test
    //    void deleteProduct_WhenValidRequest_ShouldReturnOk() throws Exception {
    //        // given
    //        Long deleteTargetId = 1L;
    //
    //        // when
    //        ResultActions result =
    //                mockMvc.perform(delete("/api/v1/products/{productId}", deleteTargetId));
    //        // then
    //        result.andExpect(status().isOk());
    //        then(productService).should().deleteProduct(anyLong());
    //    }
    //
    //    @DisplayName("product 삭제 실패: request가 invalid한 경우")
    //    @Test
    //    void deleteProduct_WhenInValidRequest_ShouldThrowException() throws Exception {
    //        // given
    //        Long deleteTargetId = 0L;
    //
    //        // when
    //        ResultActions result =
    //                mockMvc.perform(delete("/api/v1/products/{productId}", deleteTargetId));
    //
    //        // then
    //        result.andExpect(status().isBadRequest());
    //        then(productService).shouldHaveNoInteractions();
    //    }
    //
    //    @DisplayName("product 업데이트 성공: request가 valid한 경우")
    //    @Test
    //    void updateProduct_WhenValidRequest_ShouldReturnOk() throws Exception {
    //        // given
    //        Long updateTargetId = 1L;
    //        ProductUpdateRequest request = new ProductUpdateRequest(1L, 1L, new BigDecimal(1L));
    //
    //        // when
    //        ResultActions result =
    //                mockMvc.perform(
    //                        put("/api/v1/products/{productId}", updateTargetId)
    //                                .contentType(MediaType.APPLICATION_JSON)
    //                                .content(objectMapper.writeValueAsString(request)));
    //
    //        // then
    //        result.andExpect(status().isOk());
    //        then(productService).should().updateProduct(anyLong(), any());
    //    }
    //
    //        @DisplayName("product 업데이트 실패: request가 invalid한 경우")
    //        @ParameterizedTest(name = "[{index}]: updateTargetId={0}, request={1}")
    //        @MethodSource("provideParametersForUpdateProduct")
    //        void updateProduct_WhenInValidRequest_ShouldThrowException(
    //                Long updateTargetId, ProductUpdateRequest request) throws Exception {
    //            // given, when
    //            ResultActions result =
    //                    mockMvc.perform(
    //                            put("/api/v1/products/{productId}", updateTargetId)
    //                                    .contentType(MediaType.APPLICATION_JSON)
    //                                    .content(objectMapper.writeValueAsString(request)));
    //
    //            // then
    //            result.andExpect(status().isBadRequest());
    //            then(productService).shouldHaveNoInteractions();
    //        }
    //
    //        static Stream<Arguments> provideParametersForUpdateProduct() {
    //            return Stream.of(
    //                    Arguments.of(0L, new ProductUpdateRequest(1L, 1L, new BigDecimal(1L))),
    //                    Arguments.of(1L, new ProductUpdateRequest(0L, 1L, new BigDecimal(1L))),
    //                    Arguments.of(1L, new ProductUpdateRequest(1L, 0L, new BigDecimal(1L))),
    //                    Arguments.of(1L, new ProductUpdateRequest(1L, 1L, new BigDecimal(0L))));
    //        }
}
