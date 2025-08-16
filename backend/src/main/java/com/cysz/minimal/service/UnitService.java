package com.cysz.minimal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Unit;

import java.util.List;
import java.util.Map;

/**
 * 单元管理服务接口
 */
public interface UnitService {
    
    /**
     * 分页查询单元
     */
    Map<String, Object> getUnitPage(int current, int size, Integer projectId, Integer buildingId, Integer floorId, String unitStatus);
    
    /**
     * 获取单元列表
     */
    Map<String, Object> getUnitList(Integer projectId, Integer buildingId, Integer floorId, String unitStatus);
    
    /**
     * 根据楼层ID获取单元列表
     */
    Map<String, Object> getUnitsByFloor(Integer floorId);
    
    /**
     * 根据项目ID获取可用单元列表
     */
    Map<String, Object> getAvailableUnits(Integer projectId);
    
    /**
     * 创建单元
     */
    Map<String, Object> createUnit(Map<String, Object> unitData);
    
    /**
     * 更新单元
     */
    Map<String, Object> updateUnit(Integer id, Map<String, Object> unitData);
    
    /**
     * 删除单元
     */
    Map<String, Object> deleteUnit(Integer id);
    
    /**
     * 单元合并
     */
    Map<String, Object> mergeUnits(Map<String, Object> mergeData);
    
    /**
     * 单元拆分
     */
    Map<String, Object> splitUnit(Map<String, Object> splitData);
}