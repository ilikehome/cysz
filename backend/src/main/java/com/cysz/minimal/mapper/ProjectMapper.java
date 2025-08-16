package com.cysz.minimal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cysz.minimal.entity.Project;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目Mapper接口
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}