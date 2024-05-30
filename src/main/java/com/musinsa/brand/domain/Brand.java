package com.musinsa.brand.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}