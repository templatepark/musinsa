package com.musinsa.product.infra;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.blazebit.persistence.querydsl.JPQLNextExpressions;
import com.musinsa.brand.domain.QBrand;
import com.musinsa.category.domain.QCategory;
import com.musinsa.product.application.dto.*;
import com.musinsa.product.domain.ProductQueryRepository;
import com.musinsa.product.domain.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<CategoryBrandPriceWithRank> getCategoryLowestPrices() {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;

        return queryFactory
                .select(
                        Projections.constructor(
                                CategoryBrandPriceWithRank.class,
                                category.name,
                                brand.name,
                                product.price.value,
                                JPQLNextExpressions.rowNumber()
                                        .over()
                                        .partitionBy(product.categoryId)
                                        .orderBy(product.price.value.asc(), brand.name.asc())
                                        .as("rn")))
                .from(product)
                .innerJoin(category)
                .on(product.categoryId.eq(category.id))
                .innerJoin(brand)
                .on(product.brandId.eq(brand.id))
                .where(product.deleted.isFalse())
                .orderBy(category.name.asc())
                .orderBy(brand.name.asc())
                .fetch();
    }

    @Override
    public Optional<BrandIdAndName> getLowestTotalBrandIdAndName() {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(BrandIdAndName.class, brand.id, brand.name))
                        .from(product)
                        .innerJoin(category)
                        .on(product.categoryId.eq(category.id))
                        .innerJoin(brand)
                        .on(product.brandId.eq(brand.id))
                        .where(product.deleted.isFalse())
                        .groupBy(product.brandId)
                        .orderBy(product.price.value.sum().asc())
                        .limit(1)
                        .fetchOne());
    }

    @Override
    public List<CategoryPrice> getBrandLowestTotalPrice(Long brandId) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        return queryFactory
                .select(
                        Projections.constructor(
                                CategoryPrice.class, category.name, product.price.value))
                .from(product)
                .innerJoin(category)
                .on(product.categoryId.eq(category.id))
                .where(product.brandId.eq(brandId))
                .orderBy(category.name.asc())
                .fetch();
    }

    @Override
    public List<BrandNameAndPriceWithRank> getCategoryLowestAndHighestPrices(String categoryName) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;

        return queryFactory
                .select(
                        Projections.constructor(
                                BrandNameAndPriceWithRank.class,
                                brand.name,
                                product.price.value,
                                JPQLNextExpressions.denseRank()
                                        .over()
                                        .partitionBy(product.categoryId)
                                        .orderBy(product.price.value.asc())
                                        .as("min_rn"),
                                JPQLNextExpressions.denseRank()
                                        .over()
                                        .partitionBy(product.categoryId)
                                        .orderBy(product.price.value.desc())
                                        .as("max_rn")))
                .from(product)
                .innerJoin(category)
                .on(product.categoryId.eq(category.id))
                .innerJoin(brand)
                .on(product.brandId.eq(brand.id))
                .where(product.deleted.isFalse().and(category.name.eq(categoryName)))
                .orderBy(category.name.asc())
                .orderBy(brand.name.asc())
                .fetch();
    }
}
