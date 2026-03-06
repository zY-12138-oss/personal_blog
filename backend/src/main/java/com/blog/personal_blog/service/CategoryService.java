package com.blog.personal_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.personal_blog.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> getAllCategories();

    Category createCategory(String name, String description);
}
