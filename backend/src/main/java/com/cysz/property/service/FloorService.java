package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Floor;

import java.util.List;
import java.util.Map;

/**
 * 楼层管理Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface FloorService extends IService<Floor> {

    /**
     * 分页查询楼层列表
     */
    PageResult<Map<String, Object>> getFloorPage(PageQuery pageQuery, String floorName, String floorCode, 
                                                Long buildingId, Long projectId);

    /**
     * 根据ID查询楼层详情
     */
    Map<String, Object> getFloorDetail(Long id);

    /**
     * 创建楼层
     */
    boolean createFloor(Floor floor);

    /**
     * 更新楼层
     */
    boolean updateFloor(Floor floor);

    /**
     * 删除楼层
     */
    boolean deleteFloor(Long id);

    /**
     * 批量删除楼层
     */
    boolean batchDeleteFloors(List<Long> floorIds);

    /**
     * 根据楼栋ID查询楼层
     */
    List<Map<String, Object>> getFloorsByBuildingId(Long buildingId);

    /**
     * 验证楼层编号是否存在
     */
    boolean checkCodeExists(String floorCode, Long buildingId, Long excludeId);

    /**
     * 获取楼层统计信息
     */
    Map<String, Object> getFloorStatistics(Long buildingId, Long projectId);

    /**
     * 根据状态统计楼层数量
     */
    List<Map<String, Object>> getFloorCountByStatus(Long buildingId);

    /**
     * 根据类型统计楼层数量
     */
    List<Map<String, Object>> getFloorCountByType(Long buildingId);

    /**
     * 获取楼层详细信息（包含单元统计）
     */
    Map<String, Object> getFloorDetailWithStats(Long id);

    /**
     * 获取楼层的单元列表
     */
    List<Map<String, Object>> getFloorUnits(Long id);

    /**
     * 更新楼层单元数量和面积统计
     */
    boolean updateFloorStats(Long id);

    /**
     * 获取楼层租赁统计
     */
    Map<String, Object> getFloorRentalStats(Long id);

    /**
     * 获取楼层收益统计
     */
    Map<String, Object> getFloorRevenueStats(Long id, Integer year);

    /**
     * 批量创建楼层
     */
    Map<String, Object> batchCreateFloors(Long buildingId, Integer count, Floor floorTemplate);

    /**
     * 获取楼层最大序号
     */
    Integer getMaxSequence(Long buildingId);

    /**
     * 获取楼层最小序号
     */
    Integer getMinSequence(Long buildingId);

    /**
     * 导出楼层数据
     */
    List<Map<String, Object>> exportFloorData(String floorName, String floorCode, Long buildingId, Long projectId);
}