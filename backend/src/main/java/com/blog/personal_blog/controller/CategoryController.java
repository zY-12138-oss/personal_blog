package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.Category;
import com.blog.personal_blog.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public R<List<Category>> getCategoryList() {
        return R.success(categoryService.getAllCategories());
    }

    @PostMapping
    public R<Category> createCategory(@RequestBody CreateCategoryRequest request) {
        Category category = categoryService.createCategory(request.getName(), request.getDescription());
        return R.success(category);
    }

    @Data
    public static class CreateCategoryRequest {
        private String name;
        private String description;
    }
}
