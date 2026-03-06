package com.blog.personal_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.personal_blog.dto.LoginResponse;
import com.blog.personal_blog.entity.User;

public interface UserService extends IService<User> {

    void register(String username, String email, String password);

    LoginResponse login(String username, String password);

    User getUserById(Long id);

    void updateUserInfo(Long id, String bio, String avatar);
}
