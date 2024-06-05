package com.musinsa.product.presentation.ui;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandQueryRepository;
import com.musinsa.category.domain.Category;
import com.musinsa.category.domain.CategoryQueryRepository;
import com.musinsa.product.application.*;
import com.musinsa.product.application.dto.BrandLowestTotalPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestAndHighestPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestPricesResponse;
import com.musinsa.product.application.dto.ProductWithDetails;
import com.musinsa.product.domain.Product;
import com.musinsa.product.domain.ProductRepository;

@Controller
public class ProductUIController {

    private final ProductQueryService productQueryService;
    private final ProductRepository productRepository;
    private final BrandQueryRepository brandQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    public ProductUIController(
            ProductQueryService productQueryService,
            ProductRepository productRepository,
            BrandQueryRepository brandQueryRepository,
            CategoryQueryRepository categoryQueryRepository) {
        this.productQueryService = productQueryService;
        this.productRepository = productRepository;
        this.brandQueryRepository = brandQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/products/category-lowest-prices")
    public String getCategoryLowestPricesPage(Model model) {
        CategoryLowestPricesResponse response = productQueryService.getCategoryLowestPrices();
        model.addAttribute("response", response);
        return "categoryLowestPrices";
    }

    @GetMapping("/products/lowest-total-brand-price")
    public String getLowestBrandPricePage(Model model) {
        BrandLowestTotalPriceResponse response = productQueryService.getBrandLowestTotalPrice();
        model.addAttribute("response", response);
        return "lowestTotalBrandPrice";
    }

    @GetMapping("/products/category-search-form")
    public String getCategorySearchFormPage() {
        return "categorySearchForm";
    }

    @GetMapping("/products/category")
    public String getCategoryPricesPage(
            @RequestParam(name = "categoryName", required = false) String categoryName,
            Model model) {
        if (categoryName == null || categoryName.isEmpty()) {
            return "redirect:/products/category-search-form";
        }
        CategoryLowestAndHighestPriceResponse response =
                productQueryService.getCategoryLowestAndHighestPrices(categoryName);
        model.addAttribute("response", response);
        return "categoryLowestHighestPrices";
    }

    @GetMapping("/products/save")
    public String addProductPage(Model model) {
        List<Brand> brands = brandQueryRepository.findAll();
        List<Category> categories = categoryQueryRepository.findAll();
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "saveProduct";
    }

    @GetMapping("/products/update")
    public String updateProductPage() {
        return "updateProduct";
    }

    @GetMapping("/products/delete")
    public String deleteProductPage() {
        return "deleteProduct";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<ProductWithDetails> allProducts = productQueryService.findAllProducts();
        model.addAttribute("products", allProducts);
        return "admin";
    }

    @GetMapping("/products/{id}")
    public String productDetailPage(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).get();
        List<Brand> brands = brandQueryRepository.findAll();
        List<Category> categories = categoryQueryRepository.findAll();
        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "productDetail";
    }
}
