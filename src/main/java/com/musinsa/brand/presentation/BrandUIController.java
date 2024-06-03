package com.musinsa.brand.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandUIController {

    @GetMapping("/brands/save")
    public String index() {
        return "saveBrand";
    }
}
