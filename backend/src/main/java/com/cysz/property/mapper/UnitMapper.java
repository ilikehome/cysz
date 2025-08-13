package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Unit;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 单元Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface UnitMapper extends BaseMapper<Unit> {

    /**
     * 分页查询单元列表
     *
     * @param page 分页参数
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @param floorId 楼层ID
     * @param name 单元名称
     * @param number 单元编号
     * @param status 状态
     * @param type 类型
     * @param orientation 朝向
     * @param decorationStatus 装修状态
     * @return 单元列表
     */
    Page<Map<String, Object>> selectUnitPage(Page<Map<String, Object>> page,
                                             @Param("projectId") Long projectId,
                                             @Param("buildingId") Long buildingId,
                                             @Param("floorId") Long floorId,
                                             @Param("name") String name,
                                             @Param("number") String number,
                                             @Param("status") Integer status,
                                             @Param("type") Integer type,
                                             @Param("orientation") String orientation,
                                             @Param("decorationStatus") Integer decorationStatus);

    /**
     * 根据楼层ID查询单元列表
     *
     * @param floorId 楼层ID
     * @return 单元列表
     */
    @Select("SELECT id, name, number, status, type, building_area, usable_area, current_rent " +
            "FROM units WHERE floor_id = #{floorId} AND deleted = 0 " +
            "ORDER BY number ASC")
    List<Map<String, Object>> selectByFloorId(@Param("floorId") Long floorId);

    /**
     * 根据楼栋ID查询单元列表
     *
     * @param buildingId 楼栋ID
     * @return 单元列表
     */
    List<Map<String, Object>> selectByBuildingId(@Param("buildingId") Long buildingId);

    /**
     * 根据项目ID查询单元列表
     *
     * @param projectId 项目ID
     * @return 单元列表
     */
    List<Map<String, Object>> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 检查单元编号是否存在
     *
     * @param floorId 楼层ID
     * @param number 单元编号
     * @param excludeId 排除的单元ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM units " +
            "WHERE floor_id = #{floorId} " +
            "AND number = #{number} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkNumberExists(@Param("floorId") Long floorId,
                         @Param("number") String number,
                         @Param("excludeId") Long excludeId);

    /**
     * 获取单元统计信息
     *
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @param floorId 楼层ID
     * @return 统计信息
     */
    Map<String, Object> getUnitStatistics(@Param("projectId") Long projectId,
                                          @Param("buildingId") Long buildingId,
                                          @Param("floorId") Long floorId);

    /**
     * 根据状态统计单元数量
     *
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @param floorId 楼层ID
     * @return 状态统计
     */
    List<Map<String, Object>> getUnitCountByStatus(@Param("projectId") Long projectId,
                                                   @Param("buildingId") Long buildingId,
                                                   @Param("floorId") Long floorId);

    /**
     * 根据类型统计单元数量
     *
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @param floorId 楼层ID
     * @return 类型统计
     */
    List<Map<String, Object>> getUnitCountByType(@Param("projectId") Long projectId,
                                                 @Param("buildingId") Long buildingId,
                                                 @Param("floorId") Long floorId);

    /**
     * 根据朝向统计单元数量
     *
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @param floorId 楼层ID
     * @return 朝向统计
     */
    @Select("SELECT orientation, COUNT(*) as count, AVG(current_rent) as avgRent " +
            "FROM units WHERE deleted = 0 " +
            "AND (#{projectId} IS NULL OR id IN (" +
            "  SELECT u.id FROM units u " +
            "  INNER JOIN floors f ON u.floor_id = f.id " +
            "  INNER JOIN buildings b ON f.building_id = b.id " +
            "  WHERE b.project_id = #{projectId}" +
            ")) " +
            "AND (#{buildingId} IS NULL OR id IN (" +
            "  SELECT u.id FROM units u " +
            "  INNER JOIN floors f ON u.floor_id = f.id " +
            "  WHERE f.building_id = #{buildingId}" +
            ")) " +
            "AND (#{floorId} IS NULL OR floor_id = #{floorId}) " +
            "GROUP BY orientation")
    List<Map<String, Object>> getUnitCountByOrientation(@Param("projectId") Long projectId,
                                                        @Param("buildingId") Long buildingId,
                                                        @Param("floorId") Long floorId);

    /**
     * 获取单元详细信息（包含合同信息）
     *
     * @param unitId 单元ID
     * @return 详细信息
     */
    Map<String, Object> getUnitDetail(@Param("unitId") Long unitId);

    /**
     * 获取单元的合同列表
     *
     * @param unitId 单元ID
     * @return 合同列表
     */
    List<Map<String, Object>> getUnitContracts(@Param("unitId") Long unitId);

    /**
     * 获取单元的当前合同
     *
     * @param unitId 单元ID
     * @return 当前合同
     */
    Map<String, Object> getUnitCurrentContract(@Param("unitId") Long unitId);

    /**
     * 获取可租赁单元列表
     *
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @param floorId 楼层ID
     * @param minArea 最小面积
     * @param maxArea 最大面积
     * @param minRent 最小租金
     * @param maxRent 最大租金
     * @return 可租赁单元列表
     */
    List<Map<String, Object>> getAvailableUnits(@Param("projectId") Long projectId,
                                                @Param("buildingId") Long buildingId,
                                                @Param("floorId") Long floorId,
                                                @Param("minArea") BigDecimal minArea,
                                                @Param("maxArea") BigDecimal maxArea,
                                                @Param("minRent") BigDecimal minRent,
                                                @Param("maxRent") BigDecimal maxRent);

    /**
     * 获取单元租赁历史
     *
     * @param unitId 单元ID
     * @return 租赁历史
     */
    List<Map<String, Object>> getUnitRentalHistory(@Param("unitId") Long unitId);

    /**
     * 获取单元收益统计
     *
     * @param unitId 单元ID
     * @param year 年份
     * @return 收益统计
     */
    Map<String, Object> getUnitRevenueStatistics(@Param("unitId") Long unitId,
                                                 @Param("year") Integer year);

    /**
     * 批量创建单元
     *
     * @param units 单元列表
     * @return 创建数量
     */
    int batchInsertUnits(@Param("units") List<Unit> units);

    /**
     * 批量更新单元状态
     *
     * @param unitIds 单元ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("unitIds") List<Long> unitIds,
                         @Param("status") Integer status);

    /**
     * 批量更新单元租金
     *
     * @param unitIds 单元ID列表
     * @param rentAdjustment 租金调整
     * @param adjustmentType 调整类型（1-固定金额，2-百分比）
     * @return 更新数量
     */
    int batchUpdateRent(@Param("unitIds") List<Long> unitIds,
                       @Param("rentAdjustment") BigDecimal rentAdjustment,
                       @Param("adjustmentType") Integer adjustmentType);

    /**
     * 获取单元出租率统计
     *
     * @param projectId 项目ID
     * @param buildingId 楼栋ID
     * @return 出租率统计
     */
    Map<String, Object> getUnitOccupancyStatistics(@Param("projectId") Long projectId,
                                                   @Param("buildingId") Long buildingId);

}