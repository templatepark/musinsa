package com.musinsa.brand.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.application.dto.BrandSaveRequest;
import com.musinsa.brand.application.exception.AlreadyBrandNameException;
import com.musinsa.brand.domain.BrandRepository;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Transactional
    public Long saveBrand(BrandSaveRequest request) {
        if (brandRepository.existsByName(request.brandName())) {
            throw new AlreadyBrandNameException(
                    String.format("브랜드 이름(%s)이 이미 존재합니다.", request.brandName()));
        }
        return brandRepository.save(request.toBrand()).getId();
    }
}
