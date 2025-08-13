package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Floor;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 楼层Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface FloorMapper extends BaseMapper<Floor> {

    /**
     * 分页查询楼层列表
     *
     * @param page 分页参数
     * @param buildingId 楼栋ID
     * @param name 楼层名称
     * @param number 楼层编号
     * @param status 状态
     * @param type 类型
     * @return 楼层列表
     */
    Page<Map<String, Object>> selectFloorPage(Page<Map<String, Object>> page,
                                              @Param("buildingId") Long buildingId,
                                              @Param("name") String name,
                                              @Param("number") String number,
                                              @Param("status") Integer status,
                                              @Param("type") Integer type);

    /**
     * 根据楼栋ID查询楼层列表
     *
     * @param buildingId 楼栋ID
     * @return 楼层列表
     */
    @Select("SELECT id, name, number, floor_order, status, type FROM floors " +
            "WHERE building_id = #{buildingId} AND deleted = 0 " +
            "ORDER BY floor_order ASC")
    List<Map<String, Object>> selectByBuildingId(@Param("buildingId") Long buildingId);

    /**
     * 检查楼层编号是否存在
     *
     * @param buildingId 楼栋ID
     * @param number 楼层编号
     * @param excludeId 排除的楼层ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM floors " +
            "WHERE building_id = #{buildingId} " +
            "AND number = #{number} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkNumberExists(@Param("buildingId") Long buildingId,
                         @Param("number") String number,
                         @Param("excludeId") Long excludeId);

    /**
     * 检查楼层序号是否存在
     *
     * @param buildingId 楼栋ID
     * @param floorOrder 楼层序号
     * @param excludeId 排除的楼层ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM floors " +
            "WHERE building_id = #{buildingId} " +
            "AND floor_order = #{floorOrder} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkFloorOrderExists(@Param("buildingId") Long buildingId,
                             @Param("floorOrder") Integer floorOrder,
                             @Param("excludeId") Long excludeId);

    /**
     * 获取楼层统计信息
     *
     * @param buildingId 楼栋ID
     * @return 统计信息
     */
    @Select("SELECT COUNT(*) as totalFloors, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as planningFloors, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as constructingFloors, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as completedFloors, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as deliveredFloors, " +
            "SUM(building_area) as totalBuildingArea, " +
            "SUM(rentable_area) as totalRentableArea, " +
            "SUM(unit_count) as totalUnits " +
            "FROM floors WHERE building_id = #{buildingId} AND deleted = 0")
    Map<String, Object> getFloorStatistics(@Param("buildingId") Long buildingId);

    /**
     * 根据状态统计楼层数量
     *
     * @param buildingId 楼栋ID
     * @return 状态统计
     */
    @Select("SELECT status, COUNT(*) as count " +
            "FROM floors WHERE building_id = #{buildingId} AND deleted = 0 " +
            "GROUP BY status")
    List<Map<String, Object>> getFloorCountByStatus(@Param("buildingId") Long buildingId);

    /**
     * 根据类型统计楼层数量
     *
     * @param buildingId 楼栋ID
     * @return 类型统计
     */
    @Select("SELECT type, COUNT(*) as count, SUM(building_area) as totalArea " +
            "FROM floors WHERE building_id = #{buildingId} AND deleted = 0 " +
            "GROUP BY type")
    List<Map<String, Object>> getFloorCountByType(@Param("buildingId") Long buildingId);

    /**
     * 获取楼层详细信息（包含单元统计）
     *
     * @param floorId 楼层ID
     * @return 详细信息
     */
    Map<String, Object> getFloorDetail(@Param("floorId") Long floorId);

    /**
     * 获取楼层的单元列表
     *
     * @param floorId 楼层ID
     * @return 单元列表
     */
    @Select("SELECT id, name, number, status, type, building_area, usable_area " +
            "FROM units WHERE floor_id = #{floorId} AND deleted = 0 " +
            "ORDER BY number ASC")
    List<Map<String, Object>> getFloorUnits(@Param("floorId") Long floorId);

    /**
     * 更新楼层单元数量
     *
     * @param floorId 楼层ID
     * @return 更新结果
     */
    int updateFloorUnitCount(@Param("floorId") Long floorId);

    /**
     * 更新楼层面积统计
     *
     * @param floorId 楼层ID
     * @return 更新结果
     */
    int updateFloorAreaStatistics(@Param("floorId") Long floorId);

    /**
     * 获取楼层租赁统计
     *
     * @param floorId 楼层ID
     * @return 租赁统计
     */
    Map<String, Object> getFloorRentalStatistics(@Param("floorId") Long floorId);

    /**
     * 获取楼层收益统计
     *
     * @param floorId 楼层ID
     * @param year 年份
     * @return 收益统计
     */
    Map<String, Object> getFloorRevenueStatistics(@Param("floorId") Long floorId,
                                                  @Param("year") Integer year);

    /**
     * 批量创建楼层
     *
     * @param floors 楼层列表
     * @return 创建数量
     */
    int batchInsertFloors(@Param("floors") List<Floor> floors);

    /**
     * 获取楼层的最大序号
     *
     * @param buildingId 楼栋ID
     * @return 最大序号
     */
    @Select("SELECT IFNULL(MAX(floor_order), 0) FROM floors " +
            "WHERE building_id = #{buildingId} AND deleted = 0")
    Integer getMaxFloorOrder(@Param("buildingId") Long buildingId);

    /**
     * 获取楼层的最小序号
     *
     * @param buildingId 楼栋ID
     * @return 最小序号
     */
    @Select("SELECT IFNULL(MIN(floor_order), 0) FROM floors " +
            "WHERE building_id = #{buildingId} AND deleted = 0")
    Integer getMinFloorOrder(@Param("buildingId") Long buildingId);

}