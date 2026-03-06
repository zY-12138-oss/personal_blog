package com.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.personal_blog.dto.LoginResponse;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.exception.BusinessException;
import com.blog.personal_blog.mapper.UserMapper;
import com.blog.personal_blog.service.UserService;
import com.blog.personal_blog.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(String username, String email, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).or().eq(User::getEmail, email);
        if (count(wrapper) > 0) {
            throw new BusinessException("用户名或邮箱已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        save(user);
    }

    @Override
    public LoginResponse login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = getOne(wrapper);
        
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public void updateUserInfo(Long id, String bio, String avatar) {
        User user = new User();
        user.setId(id);
        if (bio != null) user.setBio(bio);
        if (avatar != null) user.setAvatar(avatar);
        updateById(user);
    }
}
