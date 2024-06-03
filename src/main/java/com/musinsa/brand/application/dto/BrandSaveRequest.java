package com.musinsa.brand.application.dto;

import jakarta.validation.constraints.NotBlank;

import com.musinsa.brand.domain.Brand;

public record BrandSaveRequest(@NotBlank String brandName) {
    public Brand toBrand() {
        return new Brand(brandName);
    }
}
