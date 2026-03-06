package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.Tag;
import com.blog.personal_blog.service.TagService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public R<List<Tag>> getTagList() {
        return R.success(tagService.getAllTags());
    }

    @PostMapping
    public R<Tag> createTag(@RequestBody CreateTagRequest request) {
        Tag tag = tagService.createTag(request.getName());
        return R.success(tag);
    }

    @Data
    public static class CreateTagRequest {
        private String name;
    }
}
