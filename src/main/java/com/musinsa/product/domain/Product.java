package com.musinsa.product.domain;

import jakarta.persistence.*;

import com.musinsa.common.domain.BaseTimeEntity;

@Entity
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "categoryId")
    private Long categoryId;

    @Embedded private Money price;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected Product() {}

    public Product(Long brandId, Long categoryId, Money price) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = Boolean.TRUE;
    }
}
