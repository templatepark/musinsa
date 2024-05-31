package com.musinsa.product.domain;

import jakarta.persistence.*;

import org.hibernate.annotations.SQLRestriction;

import com.musinsa.common.domain.BaseTimeEntity;

@SQLRestriction("deleted = false")
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

    public Long getBrandId() {
        return brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Money getPrice() {
        return price;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = Boolean.TRUE;
    }

    public void updateFrom(Product product) {
        this.brandId = product.getBrandId();
        this.categoryId = product.getCategoryId();
        this.price = product.getPrice();
    }
}
