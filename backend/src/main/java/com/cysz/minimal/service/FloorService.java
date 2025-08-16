package com.cysz.minimal.service;

import java.util.Map;

/**
 * 楼层服务接口
 */
public interface FloorService {
    
    /**
     * 分页查询楼层
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键字
     * @param buildingId 楼栋ID
     * @return 分页结果
     */
    Map<String, Object> getFloorPage(int current, int size, String keyword, Integer buildingId);
    
    /**
     * 根据楼栋ID获取楼层列表
     * @param buildingId 楼栋ID
     * @return 楼层列表
     */
    Map<String, Object> getFloorsByBuilding(Integer buildingId);
    
    /**
     * 创建楼层
     * @param floorData 楼层数据
     * @return 创建结果
     */
    Map<String, Object> createFloor(Map<String, Object> floorData);
    
    /**
     * 更新楼层
     * @param id 楼层ID
     * @param floorData 楼层数据
     * @return 更新结果
     */
    Map<String, Object> updateFloor(Integer id, Map<String, Object> floorData);
    
    /**
     * 删除楼层
     * @param id 楼层ID
     * @return 删除结果
     */
    Map<String, Object> deleteFloor(Integer id);
}