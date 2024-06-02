package com.musinsa.product.infra;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.musinsa.product.application.BrandCategoryTotalResponse;
import com.musinsa.product.application.BrandLowestPriceResponse;
import com.musinsa.product.application.BrandPriceResponse;
import com.musinsa.product.application.CategoryBrandPriceResponse;
import com.musinsa.product.application.CategoryLowestAndHighestPriceResponse;
import com.musinsa.product.application.CategoryPriceResponse;
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
                (rs) -> {
                    String categoryName = rs.getString("category_name");
                    String brandName = rs.getString("brand_name");
                    BigDecimal price = rs.getBigDecimal("price");

                    categoryBrandPrices.add(
                            new CategoryBrandPriceResponse(categoryName, brandName, price));
                });

        return categoryBrandPrices;
    }

    @Override
    public BrandLowestPriceResponse getLowestTotalBrandPrice() {
        String sql =
                "WITH MinBrand AS ( "
                        + "    SELECT p.brand_id, b.brand_name, SUM(p.price) AS total_price "
                        + "    FROM product p "
                        + "    INNER JOIN brand b ON p.brand_id = b.brand_id "
                        + "    WHERE p.deleted = false "
                        + "    GROUP BY p.brand_id, b.brand_name "
                        + "    ORDER BY total_price "
                        + "    LIMIT 1 "
                        + ") "
                        + "SELECT c.category_name, mb.brand_name, p.price "
                        + "FROM product p "
                        + "INNER JOIN category c ON p.category_id = c.category_id "
                        + "INNER JOIN MinBrand mb ON p.brand_id = mb.brand_id "
                        + "WHERE p.deleted = false "
                        + "ORDER BY c.category_name";

        List<CategoryPriceResponse> categoryPrices = new ArrayList<>();
        AtomicReference<String> brandName = new AtomicReference<>(null);
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);

        jdbcTemplate.query(
                connection -> connection.prepareStatement(sql),
                (rs, rowNum) -> {
                    String categoryName = rs.getString("category_name");
                    String brand = rs.getString("brand_name");
                    BigDecimal price = rs.getBigDecimal("price");

                    categoryPrices.add(new CategoryPriceResponse(categoryName, price));
                    brandName.set(brand);
                    totalPrice.set(totalPrice.get().add(price));
                    return null;
                });

        return new BrandLowestPriceResponse(
                new BrandCategoryTotalResponse(brandName.get(), categoryPrices, totalPrice.get()));
    }

    @Override
    public CategoryLowestAndHighestPriceResponse getLowestAndHighestPricesByCategoryName(
            String categoryName) {
        String sql =
                "WITH RankedPrices AS ( "
                        + "    SELECT c.category_name, b.brand_name, p.price, "
                        + "           DENSE_RANK() OVER (PARTITION BY p.category_id ORDER BY p.price ASC) AS min_rn, "
                        + "           DENSE_RANK() OVER (PARTITION BY p.category_id ORDER BY p.price DESC) AS max_rn "
                        + "    FROM product p "
                        + "    INNER JOIN category c ON p.category_id = c.category_id "
                        + "    INNER JOIN brand b ON p.brand_id = b.brand_id "
                        + "    WHERE p.deleted = false "
                        + "      AND c.category_name = ? "
                        + ") "
                        + "SELECT category_name, brand_name, price, min_rn, max_rn "
                        + "FROM RankedPrices "
                        + "WHERE min_rn = 1 OR max_rn = 1 "
                        + "ORDER BY category_name, brand_name";

        List<BrandPriceResponse> lowestPrices = new ArrayList<>();
        List<BrandPriceResponse> highestPrices = new ArrayList<>();

        jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, categoryName);
                    return ps;
                },
                (rs) -> {
                    String brandName = rs.getString("brand_name");
                    BigDecimal price = rs.getBigDecimal("price");
                    int minRn = rs.getInt("min_rn");
                    int maxRn = rs.getInt("max_rn");

                    if (minRn == 1) {
                        lowestPrices.add(new BrandPriceResponse(brandName, price));
                    }
                    if (maxRn == 1) {
                        highestPrices.add(new BrandPriceResponse(brandName, price));
                    }
                });

        return new CategoryLowestAndHighestPriceResponse(categoryName, lowestPrices, highestPrices);
    }
}
