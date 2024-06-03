package com.musinsa.brand.domain;

import jakarta.persistence.*;

import com.musinsa.common.domain.BaseTimeEntity;

@Entity
@Table(name = "brand")
public class Brand extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @Column(name = "brand_name", unique = true)
    private String name;

    protected Brand() {}

    public Brand(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
