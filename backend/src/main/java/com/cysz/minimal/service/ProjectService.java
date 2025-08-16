package com.cysz.minimal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cysz.minimal.entity.Project;

import java.util.List;
import java.util.Map;

/**
 * 项目服务接口
 */
public interface ProjectService {
    
    /**
     * 项目分页查询
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param projectType 项目类型
     * @return 分页结果
     */
    Map<String, Object> getProjectPage(int current, int size, String keyword, String projectType);
    
    /**
     * 获取所有项目列表
     * @return 项目列表
     */
    List<Map<String, Object>> getProjectList();
    
    /**
     * 创建项目
     * @param projectData 项目数据
     * @return 创建结果
     */
    boolean createProject(Map<String, Object> projectData);
    
    /**
     * 更新项目
     * @param id 项目ID
     * @param projectData 项目数据
     * @return 更新结果
     */
    boolean updateProject(Integer id, Map<String, Object> projectData);
    
    /**
     * 删除项目
     * @param id 项目ID
     * @return 删除结果
     */
    boolean deleteProject(Integer id);
}