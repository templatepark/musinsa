package com.musinsa.brand.presentation.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.brand.application.dto.BrandSaveRequest;
import com.musinsa.brand.application.BrandService;

@RestController
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(path = "/api/v1/brands")
    public ResponseEntity<Void> saveBrand(@RequestBody @Validated BrandSaveRequest request) {
        Long saveBrandId = brandService.saveBrand(request);
        return ResponseEntity.created(URI.create(String.format("/api/v1/brands/%d", saveBrandId)))
                .build();
    }
}
