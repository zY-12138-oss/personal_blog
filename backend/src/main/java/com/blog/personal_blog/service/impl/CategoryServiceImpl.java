package com.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.personal_blog.entity.Category;
import com.blog.personal_blog.mapper.CategoryMapper;
import com.blog.personal_blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getAllCategories() {
        return list();
    }

    @Override
    public Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        save(category);
        return category;
    }
}
