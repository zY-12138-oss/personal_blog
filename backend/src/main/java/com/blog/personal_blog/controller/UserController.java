package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.service.UserService;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public R<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return R.success();
    }

    @PostMapping("/login")
    public R<com.blog.personal_blog.dto.LoginResponse> login(@RequestBody LoginRequest request) {
        com.blog.personal_blog.dto.LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        return R.success(response);
    }

    @GetMapping("/{id}")
    public R<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setPassword(null);
        return R.success(user);
    }

    @PutMapping("/{id}")
    public R<Void> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        userService.updateUserInfo(id, request.getBio(), request.getAvatar());
        return R.success();
    }

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class UpdateUserRequest {
        private String bio;
        private String avatar;
    }
}
