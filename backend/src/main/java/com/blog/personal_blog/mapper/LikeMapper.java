package com.blog.personal_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.personal_blog.entity.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper extends BaseMapper<Like> {
}
