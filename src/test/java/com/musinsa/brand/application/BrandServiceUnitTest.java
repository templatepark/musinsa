package com.musinsa.brand.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musinsa.brand.application.dto.BrandSaveRequest;
import com.musinsa.brand.application.exception.AlreadyBrandNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandRepository;

@ExtendWith(MockitoExtension.class)
class BrandServiceUnitTest {

    @Mock private BrandRepository repository;

    @InjectMocks private BrandService service;

    @DisplayName("brand 저장 성공: brand 이름이 중복되지 않을 경우")
    @Test
    void saveBrand_WhenNotDuplicateBrandName_ShouldReturnBrandId() {
        // given
        String brandName = "musinsa";
        Brand returnBrand = new Brand(brandName);
        ReflectionTestUtils.setField(returnBrand, "id", 1L);
        BrandSaveRequest request = new BrandSaveRequest(brandName);

        when(repository.existsByName(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(returnBrand);

        // when
        Long resultId = service.saveBrand(request);

        // then
        assertThat(resultId).isEqualTo(1L);
        verify(repository).existsByName(any());
        verify(repository).save(any());
    }

    @DisplayName("brand 저장 실패: brand 이름이 중복될 경우")
    @Test
    void saveBrand_WhenDuplicateBrandName_ShouldThrowException() {
        // given
        String brandName = "musinsa";
        Brand returnBrand = new Brand(brandName);
        ReflectionTestUtils.setField(returnBrand, "id", 1L);
        BrandSaveRequest request = new BrandSaveRequest(brandName);

        when(repository.existsByName(any())).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> service.saveBrand(request))
                .isInstanceOf(AlreadyBrandNameException.class);
        verify(repository).existsByName(any());
    }
}
