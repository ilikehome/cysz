package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.ResultCode;
import com.cysz.property.dto.ProjectDTO;
import com.cysz.property.dto.ProjectQueryDTO;
import com.cysz.property.entity.Project;
import com.cysz.property.exception.BusinessException;
import com.cysz.property.mapper.ProjectMapper;
import com.cysz.property.service.ProjectService;
import com.cysz.property.vo.ProjectVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目服务实现类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    private final ProjectMapper projectMapper;

    @Override
    public PageResult<ProjectVO> getProjectPage(ProjectQueryDTO query) {
        Page<ProjectVO> page = new Page<>(query.getCurrent(), query.getSize());
        projectMapper.selectProjectPage(page, query);
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public ProjectVO getProjectById(Long id) {
        if (id == null) {
            throw BusinessException.of(ResultCode.PARAM_ERROR, "项目ID不能为空");
        }
        ProjectVO projectVO = projectMapper.selectProjectById(id);
        if (projectVO == null) {
            throw BusinessException.of(ResultCode.PROJECT_NOT_FOUND);
        }
        return projectVO;
    }

    @Override
    public List<ProjectVO> getProjectList() {
        return projectMapper.selectProjectList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProject(ProjectDTO projectDTO) {
        // 校验项目编码是否存在
        if (checkCodeExists(projectDTO.getCode(), null)) {
            throw BusinessException.of(ResultCode.PROJECT_CODE_EXISTS);
        }
        
        // 校验项目名称是否存在
        if (checkNameExists(projectDTO.getName(), null)) {
            throw BusinessException.of(ResultCode.PROJECT_NAME_EXISTS);
        }

        Project project = new Project();
        BeanUtils.copyProperties(projectDTO, project);
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        project.setDeleted(0);

        if (!save(project)) {
            throw BusinessException.of(ResultCode.PROJECT_CREATE_FAILED);
        }

        log.info("创建项目成功，项目ID: {}, 项目名称: {}", project.getId(), project.getProjectName());
        return project.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProject(Long id, ProjectDTO projectDTO) {
        if (id == null) {
            throw BusinessException.of(ResultCode.PARAM_ERROR, "项目ID不能为空");
        }

        Project existProject = getById(id);
        if (existProject == null || existProject.getDeleted() == 1) {
            throw BusinessException.of(ResultCode.PROJECT_NOT_FOUND);
        }

        // 校验项目编码是否存在
        if (checkCodeExists(projectDTO.getCode(), id)) {
            throw BusinessException.of(ResultCode.PROJECT_CODE_EXISTS);
        }
        
        // 校验项目名称是否存在
        if (checkNameExists(projectDTO.getName(), id)) {
            throw BusinessException.of(ResultCode.PROJECT_NAME_EXISTS);
        }

        Project project = new Project();
        BeanUtils.copyProperties(projectDTO, project);
        project.setId(id);
        project.setUpdateTime(LocalDateTime.now());

        if (!updateById(project)) {
            throw BusinessException.of(ResultCode.PROJECT_UPDATE_FAILED);
        }

        log.info("更新项目成功，项目ID: {}, 项目名称: {}", id, project.getProjectName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProject(Long id) {
        if (id == null) {
            throw BusinessException.of(ResultCode.PARAM_ERROR, "项目ID不能为空");
        }

        Project project = getById(id);
        if (project == null || project.getDeleted() == 1) {
            throw BusinessException.of(ResultCode.PROJECT_NOT_FOUND);
        }

        // TODO: 检查项目下是否有楼栋、单元等关联数据

        project.setDeleted(1);
        project.setUpdateTime(LocalDateTime.now());

        if (!updateById(project)) {
            throw BusinessException.of(ResultCode.PROJECT_DELETE_FAILED);
        }

        log.info("删除项目成功，项目ID: {}, 项目名称: {}", id, project.getProjectName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProjects(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw BusinessException.of(ResultCode.PARAM_ERROR, "项目ID列表不能为空");
        }

        for (Long id : ids) {
            deleteProject(id);
        }

        log.info("批量删除项目成功，项目数量: {}", ids.size());
        return true;
    }

    @Override
    public Boolean checkCodeExists(String code, Long excludeId) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        Project project = projectMapper.selectByCode(code.trim(), excludeId);
        return project != null;
    }

    @Override
    public Boolean checkNameExists(String name, Long excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        Project project = projectMapper.selectByName(name.trim(), excludeId);
        return project != null;
    }
}