package com.cysz.minimal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Building;
import com.cysz.minimal.entity.Project;
import com.cysz.minimal.enums.ProjectType;
import com.cysz.minimal.mapper.ProjectMapper;
import com.cysz.minimal.mapper.BuildingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 项目管理控制器
 */
@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    /**
     * 项目分页查询
     */
    @GetMapping("/page")
    public Map<String, Object> getProjectPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectType) {
        
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
    
    /**
     * 获取所有项目列表
     */
    @GetMapping("/list")
    public Map<String, Object> getProjectList() {
        Map<String, Object> response = new HashMap<>();
        
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
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", projects);
            
        } catch (Exception e) {
            System.err.println("获取项目列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    /**
     * 创建项目
     */
    @PostMapping
    public Map<String, Object> createProject(@RequestBody Map<String, Object> projectData) {
        Map<String, Object> response = new HashMap<>();
        
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
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "项目创建成功");
                response.put("data", null);
            } else {
                response.put("code", 500);
                response.put("message", "项目创建失败");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("创建项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 更新项目
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateProject(@PathVariable Integer id, @RequestBody Map<String, Object> projectData) {
        Map<String, Object> response = new HashMap<>();
        
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
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "项目更新成功");
                response.put("data", null);
            } else {
                response.put("code", 404);
                response.put("message", "项目不存在或已删除");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("更新项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 删除项目
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProject(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查项目下是否有楼栋
            QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("project_id", id);
            Long buildingCount = buildingMapper.selectCount(queryWrapper);
            
            if (buildingCount != null && buildingCount > 0) {
                response.put("code", 400);
                response.put("message", "该项目下存在楼栋，无法删除");
                response.put("data", null);
                return response;
            }
            
            int result = projectMapper.deleteById(id);
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "删除成功");
                response.put("data", null);
            } else {
                response.put("code", 404);
                response.put("message", "项目不存在");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("删除项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
}