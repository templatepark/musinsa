package com.musinsa.brand.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandRepository;

@Transactional
@SpringBootTest
class BrandServiceIntegrationTest {

    @Autowired private BrandService service;

    @Autowired private BrandRepository repository;

    private static final String DUP_BRAND_NAME = "ThisIsNeverThat";

    @BeforeEach
    void setUp() {
        repository.save(new Brand(DUP_BRAND_NAME));
    }

    @DisplayName("brand 저장 성공: brand 이름이 중복되지 않을 경우")
    @Test
    void saveBrand_WhenNotDuplicateBrandName_ShouldReturnBrandId() {
        // given
        String brandName = "musinsa";
        BrandSaveRequest request = new BrandSaveRequest(brandName);

        // when, then
        assertDoesNotThrow(() -> service.saveBrand(request));
    }

    @DisplayName("brand 저장 실패: brand 이름이 중복될 경우")
    @Test
    void saveBrand_WhenDuplicateBrandName_ShouldThrowException() {
        // given
        String brandName = DUP_BRAND_NAME;
        BrandSaveRequest request = new BrandSaveRequest(brandName);

        // when, then
        assertThatThrownBy(() -> service.saveBrand(request))
                .isInstanceOf(AlreadyBrandNameException.class);
    }
}
