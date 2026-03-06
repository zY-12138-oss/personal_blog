package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public R<String> hello() {
        return R.success("后端正常运行！");
    }
}
