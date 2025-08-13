package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.dto.ProjectQueryDTO;
import com.cysz.property.entity.Project;
import com.cysz.property.vo.ProjectVO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目Mapper接口
 *
 * @author CYSZ
 * @since 2024-01-01
 */

public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 分页查询项目列表
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 项目列表
     */
    IPage<ProjectVO> selectProjectPage(Page<ProjectVO> page, @Param("query") ProjectQueryDTO query);

    /**
     * 根据ID查询项目详情
     *
     * @param id 项目ID
     * @return 项目详情
     */
    ProjectVO selectProjectById(@Param("id") Long id);

    /**
     * 查询所有项目列表（用于下拉选择）
     *
     * @return 项目列表
     */
    List<ProjectVO> selectProjectList();

    /**
     * 根据编码查询项目
     *
     * @param code 项目编码
     * @param excludeId 排除的项目ID
     * @return 项目
     */
    Project selectByCode(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * 根据名称查询项目
     *
     * @param name 项目名称
     * @param excludeId 排除的项目ID
     * @return 项目
     */
    Project selectByName(@Param("name") String name, @Param("excludeId") Long excludeId);
}