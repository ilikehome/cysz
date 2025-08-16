package com.cysz.minimal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Building;
import com.cysz.minimal.entity.Project;
import com.cysz.minimal.enums.ProjectType;
import com.cysz.minimal.mapper.BuildingMapper;
import com.cysz.minimal.mapper.ProjectMapper;
import com.cysz.minimal.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 项目服务实现类
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    @Override
    public Map<String, Object> getProjectPage(int current, int size, String keyword, String projectType) {
        System.out.println("项目分页查询 - current: " + current + ", size: " + size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证项目类型是否有效
            if (projectType != null && !projectType.trim().isEmpty() && !ProjectType.isValidCode(projectType)) {
                response.put("code", 400);
                response.put("message", "无效的项目类型: " + projectType);
                response.put("data", null);
                return response;
            }
            
            // 构建查询条件
            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("project_name", keyword)
                    .or()
                    .like("address", keyword)
                );
            }
            
            if (projectType != null && !projectType.trim().isEmpty()) {
                queryWrapper.eq("project_type", projectType);
            }
            
            queryWrapper.orderByDesc("id");
            
            // 分页查询
            Page<Project> page = new Page<>(current, size);
            IPage<Project> pageResult = projectMapper.selectPage(page, queryWrapper);
            
            // 转换为Map格式以保持原有返回格式
            List<Map<String, Object>> records = new ArrayList<>();
            for (Project project : pageResult.getRecords()) {
                Map<String, Object> projectMap = new HashMap<>();
                projectMap.put("id", project.getId());
                projectMap.put("projectName", project.getProjectName());
                projectMap.put("projectType", project.getProjectType());
                projectMap.put("managementOrg", project.getManagementOrg());
                projectMap.put("rentBillCompany", project.getRentBillCompany());
                projectMap.put("rentBillBankAccount", project.getRentBillBankAccount());
                projectMap.put("city", project.getCity());
                projectMap.put("address", project.getAddress());
                projectMap.put("projectManager", project.getProjectManager());
                projectMap.put("contactPhone", project.getContactPhone());
                projectMap.put("status", project.getStatus());
                projectMap.put("createTime", project.getCreateTime());
                projectMap.put("updateTime", project.getUpdateTime());
                records.add(projectMap);
            }
            
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", records);
            pageData.put("total", pageResult.getTotal());
            pageData.put("current", pageResult.getCurrent());
            pageData.put("size", pageResult.getSize());
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", pageData);
            
        } catch (Exception e) {
            System.err.println("查询项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    @Override
    public List<Map<String, Object>> getProjectList() {
        try {
            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1)
                       .orderByAsc("project_name")
                       .select("id", "project_name");
            
            List<Project> projectList = projectMapper.selectList(queryWrapper);
            
            // 转换为Map格式以保持原有返回格式
            List<Map<String, Object>> projects = new ArrayList<>();
            for (Project project : projectList) {
                Map<String, Object> projectMap = new HashMap<>();
                projectMap.put("id", project.getId());
                projectMap.put("projectName", project.getProjectName());
                projects.add(projectMap);
            }
            
            return projects;
            
        } catch (Exception e) {
            System.err.println("获取项目列表失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean createProject(Map<String, Object> projectData) {
        try {
            Project project = new Project();
            project.setProjectName((String) projectData.get("projectName"));
            project.setProjectType((String) projectData.get("projectType"));
            project.setManagementOrg((String) projectData.get("managementOrg"));
            project.setRentBillCompany((String) projectData.get("rentBillCompany"));
            project.setRentBillBankAccount((String) projectData.get("rentBillBankAccount"));
            project.setCity((String) projectData.get("city"));
            project.setAddress((String) projectData.get("address"));
            project.setProjectManager((String) projectData.get("projectManager"));
            project.setContactPhone((String) projectData.get("contactPhone"));
            project.setStatus(1);
            
            int result = projectMapper.insert(project);
            return result > 0;
            
        } catch (Exception e) {
            System.err.println("创建项目失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateProject(Integer id, Map<String, Object> projectData) {
        try {
            Project project = new Project();
            project.setId(id);
            project.setProjectName((String) projectData.get("projectName"));
            project.setProjectType((String) projectData.get("projectType"));
            project.setManagementOrg((String) projectData.get("managementOrg"));
            project.setRentBillCompany((String) projectData.get("rentBillCompany"));
            project.setRentBillBankAccount((String) projectData.get("rentBillBankAccount"));
            project.setCity((String) projectData.get("city"));
            project.setAddress((String) projectData.get("address"));
            project.setProjectManager((String) projectData.get("projectManager"));
            project.setContactPhone((String) projectData.get("contactPhone"));
            
            UpdateWrapper<Project> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id).eq("status", 1);
            
            int result = projectMapper.update(project, updateWrapper);
            return result > 0;
            
        } catch (Exception e) {
            System.err.println("更新项目失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteProject(Integer id) {
        try {
            // 检查项目下是否有楼栋
            QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("project_id", id);
            Long buildingCount = buildingMapper.selectCount(queryWrapper);
            
            if (buildingCount != null && buildingCount > 0) {
                return false; // 项目下存在楼栋，无法删除
            }
            
            int result = projectMapper.deleteById(id);
            return result > 0;
            
        } catch (Exception e) {
            System.err.println("删除项目失败: " + e.getMessage());
            return false;
        }
    }
}