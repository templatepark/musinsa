package com.musinsa.product.presentation.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.musinsa.product.application.*;
import com.musinsa.product.application.dto.BrandLowestTotalPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestAndHighestPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestPricesResponse;

@Controller
public class ProductUIController {

    private final ProductQueryService productQueryService;

    public ProductUIController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
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
    public String saveProductPage() {
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
}
