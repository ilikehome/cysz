package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageResult;
import com.cysz.property.dto.ProjectDTO;
import com.cysz.property.dto.ProjectQueryDTO;
import com.cysz.property.entity.Project;
import com.cysz.property.vo.ProjectVO;

import java.util.List;

/**
 * 项目服务接口
 *
 * @author CYSZ
 * @since 2024-01-01
 */
public interface ProjectService extends IService<Project> {

    /**
     * 分页查询项目列表
     *
     * @param query 查询条件
     * @return 项目列表
     */
    PageResult<ProjectVO> getProjectPage(ProjectQueryDTO query);

    /**
     * 根据ID查询项目详情
     *
     * @param id 项目ID
     * @return 项目详情
     */
    ProjectVO getProjectById(Long id);

    /**
     * 查询所有项目列表（用于下拉选择）
     *
     * @return 项目列表
     */
    List<ProjectVO> getProjectList();

    /**
     * 创建项目
     *
     * @param projectDTO 项目信息
     * @return 项目ID
     */
    Long createProject(ProjectDTO projectDTO);

    /**
     * 更新项目
     *
     * @param id 项目ID
     * @param projectDTO 项目信息
     * @return 是否成功
     */
    Boolean updateProject(Long id, ProjectDTO projectDTO);

    /**
     * 删除项目
     *
     * @param id 项目ID
     * @return 是否成功
     */
    Boolean deleteProject(Long id);

    /**
     * 批量删除项目
     *
     * @param ids 项目ID列表
     * @return 是否成功
     */
    Boolean deleteProjects(List<Long> ids);

    /**
     * 检查项目编码是否存在
     *
     * @param code 项目编码
     * @param excludeId 排除的项目ID
     * @return 是否存在
     */
    Boolean checkCodeExists(String code, Long excludeId);

    /**
     * 检查项目名称是否存在
     *
     * @param name 项目名称
     * @param excludeId 排除的项目ID
     * @return 是否存在
     */
    Boolean checkNameExists(String name, Long excludeId);
}