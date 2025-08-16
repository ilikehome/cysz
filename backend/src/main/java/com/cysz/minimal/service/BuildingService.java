package com.cysz.minimal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Building;

import java.util.List;
import java.util.Map;

/**
 * 楼栋管理服务接口
 */
public interface BuildingService {
    
    /**
     * 楼栋分页查询
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param projectId 项目ID
     * @return 分页结果
     */
    Map<String, Object> getBuildingPage(int current, int size, String keyword, Integer projectId);
    
    /**
     * 根据项目ID查询楼栋
     * @param projectId 项目ID
     * @return 楼栋列表
     */
    Map<String, Object> getBuildingsByProjectId(Integer projectId);
    
    /**
     * 创建楼栋
     * @param buildingData 楼栋数据
     * @return 创建结果
     */
    Map<String, Object> createBuilding(Map<String, Object> buildingData);
    
    /**
     * 更新楼栋
     * @param id 楼栋ID
     * @param buildingData 楼栋数据
     * @return 更新结果
     */
    Map<String, Object> updateBuilding(Integer id, Map<String, Object> buildingData);
    
    /**
     * 删除楼栋
     * @param id 楼栋ID
     * @return 删除结果
     */
    Map<String, Object> deleteBuilding(Integer id);
    
    /**
     * 根据ID获取楼栋详情
     * @param id 楼栋ID
     * @return 楼栋详情
     */
    Map<String, Object> getBuildingById(Integer id);
}