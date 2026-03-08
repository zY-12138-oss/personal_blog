package com.blog.personal_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.personal_blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
  
}
