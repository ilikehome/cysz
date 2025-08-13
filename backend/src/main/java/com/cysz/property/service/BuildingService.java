package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Building;

import java.util.List;
import java.util.Map;

/**
 * 楼栋管理Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface BuildingService extends IService<Building> {

    /**
     * 分页查询楼栋列表
     */
    PageResult<Map<String, Object>> getBuildingPage(PageQuery pageQuery, String number, String name, 
                                                   Integer status, Integer type, Long projectId);

    /**
     * 根据ID查询楼栋详情
     */
    Map<String, Object> getBuildingDetail(Long id);

    /**
     * 创建楼栋
     */
    boolean createBuilding(Building building);

    /**
     * 更新楼栋
     */
    boolean updateBuilding(Building building);

    /**
     * 删除楼栋
     */
    boolean deleteBuilding(Long id);

    /**
     * 批量删除楼栋
     */
    boolean batchDeleteBuildings(List<Long> buildingIds);

    /**
     * 根据项目ID查询楼栋
     */
    List<Map<String, Object>> getBuildingsByProjectId(Long projectId);

    /**
     * 验证楼栋编号是否存在
     */
    boolean checkNumberExists(String number, Long projectId, Long excludeId);

    /**
     * 验证楼栋序号是否存在
     */
    boolean checkSequenceExists(Integer sequence, Long projectId, Long excludeId);

    /**
     * 获取楼栋统计信息
     */
    Map<String, Object> getBuildingStatistics(Long projectId);

    /**
     * 根据状态统计楼栋数量
     */
    List<Map<String, Object>> getBuildingCountByStatus(Long projectId);

    /**
     * 根据类型统计楼栋数量
     */
    List<Map<String, Object>> getBuildingCountByType(Long projectId);

    /**
     * 获取楼栋详细信息（包含楼层统计）
     */
    Map<String, Object> getBuildingDetailWithStats(Long id);

    /**
     * 获取楼栋的楼层列表
     */
    List<Map<String, Object>> getBuildingFloors(Long id);

    /**
     * 更新楼栋楼层数量和面积统计
     */
    boolean updateBuildingStats(Long id);

    /**
     * 获取楼栋租赁统计
     */
    Map<String, Object> getBuildingRentalStats(Long id);

    /**
     * 获取楼栋收益统计
     */
    Map<String, Object> getBuildingRevenueStats(Long id, Integer year);

    /**
     * 批量创建楼栋
     */
    Map<String, Object> batchCreateBuildings(Long projectId, Integer startBuilding, Integer endBuilding, Building buildingTemplate);

    /**
     * 获取最大序号
     */
    Integer getMaxSequence(Long projectId);

    /**
     * 获取楼栋单元列表
     */
    List<Map<String, Object>> getBuildingUnits(Long buildingId);

    /**
     * 导出楼栋数据
     */
    String exportBuildingData(String number, String name, Integer status, Integer type, Long projectId);
}