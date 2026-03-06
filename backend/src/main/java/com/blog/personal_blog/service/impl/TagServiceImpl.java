package com.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.personal_blog.entity.Tag;
import com.blog.personal_blog.mapper.TagMapper;
import com.blog.personal_blog.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<Tag> getAllTags() {
        return list();
    }

    @Override
    public Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        save(tag);
        return tag;
    }
}
