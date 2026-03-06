package com.blog.personal_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.personal_blog.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<Tag> getAllTags();

    Tag createTag(String name);
}
