package com.musinsa.product.infra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.musinsa.product.application.CategoryBrandPriceResponse;
import com.musinsa.product.domain.ProductQueryRepository;

@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductQueryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CategoryBrandPriceResponse> getCategoryLowestPrices() {
        String sql =
                "WITH CategoryMinPrices AS ( "
                        + "    SELECT c.category_name, b.brand_name, p.price, "
                        + "           ROW_NUMBER() OVER (PARTITION BY p.category_id ORDER BY p.price, b.brand_name) AS rn "
                        + "    FROM product p "
                        + "    INNER JOIN category c ON p.category_id = c.category_id "
                        + "    INNER JOIN brand b ON p.brand_id = b.brand_id "
                        + "    WHERE p.deleted = false "
                        + ") "
                        + "SELECT category_name, brand_name, price "
                        + "FROM CategoryMinPrices "
                        + "WHERE rn = 1 "
                        + "ORDER BY category_name, brand_name";

        List<CategoryBrandPriceResponse> categoryBrandPrices = new ArrayList<>();

        jdbcTemplate.query(
                connection -> connection.prepareStatement(sql),
                (rs, rowNum) -> {
                    String categoryName = rs.getString("category_name");
                    String brandName = rs.getString("brand_name");
                    BigDecimal price = rs.getBigDecimal("price");

                    categoryBrandPrices.add(
                            new CategoryBrandPriceResponse(categoryName, brandName, price));
                    return null;
                });

        return categoryBrandPrices;
    }
}
