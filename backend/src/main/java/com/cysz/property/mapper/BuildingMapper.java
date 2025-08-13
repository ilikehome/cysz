package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Building;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 楼栋Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface BuildingMapper extends BaseMapper<Building> {

    /**
     * 分页查询楼栋列表
     *
     * @param page 分页参数
     * @param projectId 项目ID
     * @param name 楼栋名称
     * @param number 楼栋编号
     * @param status 状态
     * @param type 类型
     * @return 楼栋列表
     */
    Page<Map<String, Object>> selectBuildingPage(Page<Map<String, Object>> page,
                                                 @Param("projectId") Long projectId,
                                                 @Param("name") String name,
                                                 @Param("number") String number,
                                                 @Param("status") Integer status,
                                                 @Param("type") Integer type);

    /**
     * 根据项目ID查询楼栋列表
     *
     * @param projectId 项目ID
     * @return 楼栋列表
     */
    @Select("SELECT id, name, number, status FROM buildings " +
            "WHERE project_id = #{projectId} AND deleted = 0 " +
            "ORDER BY number ASC")
    List<Map<String, Object>> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 检查楼栋编号是否存在
     *
     * @param projectId 项目ID
     * @param number 楼栋编号
     * @param excludeId 排除的楼栋ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM buildings " +
            "WHERE project_id = #{projectId} " +
            "AND number = #{number} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkNumberExists(@Param("projectId") Long projectId,
                         @Param("number") String number,
                         @Param("excludeId") Long excludeId);

    /**
     * 获取楼栋统计信息
     *
     * @param projectId 项目ID
     * @return 统计信息
     */
    @Select("SELECT COUNT(*) as totalBuildings, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as planningBuildings, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as constructingBuildings, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as completedBuildings, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as deliveredBuildings, " +
            "SUM(building_area) as totalBuildingArea, " +
            "SUM(rentable_area) as totalRentableArea, " +
            "SUM(unit_count) as totalUnits " +
            "FROM buildings WHERE project_id = #{projectId} AND deleted = 0")
    Map<String, Object> getBuildingStatistics(@Param("projectId") Long projectId);

    /**
     * 根据状态统计楼栋数量
     *
     * @param projectId 项目ID
     * @return 状态统计
     */
    @Select("SELECT status, COUNT(*) as count " +
            "FROM buildings WHERE project_id = #{projectId} AND deleted = 0 " +
            "GROUP BY status")
    List<Map<String, Object>> getBuildingCountByStatus(@Param("projectId") Long projectId);

    /**
     * 根据类型统计楼栋数量
     *
     * @param projectId 项目ID
     * @return 类型统计
     */
    @Select("SELECT type, COUNT(*) as count, SUM(building_area) as totalArea " +
            "FROM buildings WHERE project_id = #{projectId} AND deleted = 0 " +
            "GROUP BY type")
    List<Map<String, Object>> getBuildingCountByType(@Param("projectId") Long projectId);

    /**
     * 获取楼栋详细信息（包含楼层和单元统计）
     *
     * @param buildingId 楼栋ID
     * @return 详细信息
     */
    Map<String, Object> getBuildingDetail(@Param("buildingId") Long buildingId);

    /**
     * 获取楼栋的楼层列表
     *
     * @param buildingId 楼栋ID
     * @return 楼层列表
     */
    @Select("SELECT id, name, number, floor_order, status, type " +
            "FROM floors WHERE building_id = #{buildingId} AND deleted = 0 " +
            "ORDER BY floor_order ASC")
    List<Map<String, Object>> getBuildingFloors(@Param("buildingId") Long buildingId);

    /**
     * 获取楼栋的单元列表
     *
     * @param buildingId 楼栋ID
     * @return 单元列表
     */
    List<Map<String, Object>> getBuildingUnits(@Param("buildingId") Long buildingId);

    /**
     * 更新楼栋单元数量
     *
     * @param buildingId 楼栋ID
     * @return 更新结果
     */
    int updateBuildingUnitCount(@Param("buildingId") Long buildingId);

    /**
     * 更新楼栋面积统计
     *
     * @param buildingId 楼栋ID
     * @return 更新结果
     */
    int updateBuildingAreaStatistics(@Param("buildingId") Long buildingId);

    /**
     * 获取楼栋租赁统计
     *
     * @param buildingId 楼栋ID
     * @return 租赁统计
     */
    Map<String, Object> getBuildingRentalStatistics(@Param("buildingId") Long buildingId);

    /**
     * 获取楼栋收益统计
     *
     * @param buildingId 楼栋ID
     * @param year 年份
     * @return 收益统计
     */
    Map<String, Object> getBuildingRevenueStatistics(@Param("buildingId") Long buildingId,
                                                     @Param("year") Integer year);

}