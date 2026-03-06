package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simple")
public class SimpleUploadController {

    @PostMapping("/upload")
    public R<Map<String, String>> simpleUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("