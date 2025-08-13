package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Unit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 单元管理Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface UnitService extends IService<Unit> {

    /**
     * 分页查询单元列表
     */
    PageResult<Map<String, Object>> getUnitPage(PageQuery pageQuery, String number, String name, 
                                               Integer status, Integer type, Integer orientation, Long floorId, Long buildingId, Long projectId);

    /**
     * 根据ID查询单元详情
     */
    Map<String, Object> getUnitDetail(Long id);

    /**
     * 创建单元
     */
    boolean createUnit(Unit unit);

    /**
     * 更新单元
     */
    boolean updateUnit(Unit unit);

    /**
     * 删除单元
     */
    boolean deleteUnit(Long id);

    /**
     * 批量删除单元
     */
    boolean batchDeleteUnits(List<Long> unitIds);

    /**
     * 根据楼层ID查询单元
     */
    List<Map<String, Object>> getUnitsByFloorId(Long floorId);

    /**
     * 根据楼栋ID查询单元
     */
    List<Map<String, Object>> getUnitsByBuildingId(Long buildingId);

    /**
     * 根据项目ID查询单元
     */
    List<Map<String, Object>> getUnitsByProjectId(Long projectId);

    /**
     * 验证单元编号是否存在
     */
    boolean checkNumberExists(String number, Long floorId, Long excludeId);

    /**
     * 验证单元序号是否存在
     */
    boolean checkSequenceExists(Integer sequence, Long floorId, Long excludeId);

    /**
     * 获取单元统计信息
     */
    Map<String, Object> getUnitStatistics(Long floorId, Long buildingId, Long projectId);

    /**
     * 根据状态统计单元数量
     */
    List<Map<String, Object>> getUnitCountByStatus(Long floorId, Long buildingId, Long projectId);

    /**
     * 根据类型统计单元数量
     */
    List<Map<String, Object>> getUnitCountByType(Long floorId, Long buildingId, Long projectId);

    /**
     * 获取单元详细信息（包含合同统计）
     */
    Map<String, Object> getUnitDetailWithStats(Long id);

    /**
     * 获取单元的合同列表
     */
    List<Map<String, Object>> getUnitContracts(Long id);

    /**
     * 获取单元租赁统计
     */
    Map<String, Object> getUnitRentalStats(Long id);

    /**
     * 获取单元收益统计
     */
    Map<String, Object> getUnitRevenueStats(Long id, Integer year);

    /**
     * 批量创建单元
     */
    Map<String, Object> batchCreateUnits(Long floorId, Integer startUnit, Integer endUnit, Unit unitTemplate);

    /**
     * 获取单元最大序号
     */
    Integer getMaxSequence(Long floorId);

    /**
     * 更新单元租金
     */
    boolean updateUnitRent(Long id, BigDecimal rent);

    /**
     * 更新单元状态
     */
    boolean updateUnitStatus(Long id, Integer status);

    /**
     * 获取可租赁单元列表
     */
    List<Map<String, Object>> getAvailableUnits(Long projectId, Long buildingId, Long floorId);

    /**
     * 获取已租赁单元列表
     */
    List<Map<String, Object>> getRentedUnits(Long projectId, Long buildingId, Long floorId);

    /**
     * 获取空置单元列表
     */
    List<Map<String, Object>> getVacantUnits(Long projectId, Long buildingId, Long floorId);

    /**
     * 根据朝向统计单元数量
     */
    List<Map<String, Object>> getUnitCountByOrientation(Long projectId, Long buildingId, Long floorId);

    /**
     * 获取单元详细信息（包含合同信息）
     */
    Map<String, Object> getUnitDetailWithContract(Long id);

    /**
     * 获取单元当前合同
     */
    Map<String, Object> getUnitCurrentContract(Long id);

    /**
     * 获取可租赁单元列表（扩展参数）
     */
    List<Map<String, Object>> getAvailableUnits(Long projectId, Long buildingId, Long floorId, 
                                               Integer type, BigDecimal minArea, BigDecimal maxArea, 
                                               BigDecimal minRent, BigDecimal maxRent);

    /**
     * 获取单元租赁历史
     */
    List<Map<String, Object>> getUnitRentalHistory(Long id);

    /**
     * 批量创建单元（扩展参数）
     */
    Map<String, Object> batchCreateUnits(Long floorId, Integer count, Unit unitTemplate);

    /**
     * 导出单元数据
     */
    List<Map<String, Object>> exportUnitData(String number, String name, Integer status, Integer type, 
                                            Integer orientation, Long floorId, Long buildingId, Long projectId);

    /**
     * 获取单元入住率
     */
    BigDecimal getUnitOccupancyRate(Long projectId, Long buildingId, Long floorId);

    /**
     * 批量更新单元状态
     */
    boolean batchUpdateUnitStatus(List<Long> unitIds, Integer status);

    /**
     * 批量更新单元租金
     */
    boolean batchUpdateUnitRent(List<Long> unitIds, BigDecimal rent);
}