package com.musinsa.category.domain;

import jakarta.persistence.*;

import com.musinsa.common.domain.BaseTimeEntity;

@Entity
@Table(name = "category")
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", unique = true)
    private String name;

    protected Category() {}

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
}
