package com.cysz.minimal.controller;

import com.cysz.minimal.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 项目管理控制器
 */
@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    /**
     * 项目分页查询
     */
    @GetMapping("/page")
    public Map<String, Object> getProjectPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectType) {
        
        return projectService.getProjectPage(current, size, keyword, projectType);
    }
    
    /**
     * 获取所有项目列表
     */
    @GetMapping("/list")
    public Map<String, Object> getProjectList() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> projects = projectService.getProjectList();
            
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
            boolean result = projectService.createProject(projectData);
            
            if (result) {
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
            boolean result = projectService.updateProject(id, projectData);
            
            if (result) {
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
            boolean result = projectService.deleteProject(id);
            
            if (result) {
                response.put("code", 200);
                response.put("message", "删除成功");
                response.put("data", null);
            } else {
                response.put("code", 400);
                response.put("message", "该项目下存在楼栋，无法删除或项目不存在");
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