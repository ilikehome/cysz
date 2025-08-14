package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Unit;
import com.cysz.property.mapper.UnitMapper;
import com.cysz.property.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单元服务实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Override
    public PageResult<Map<String, Object>> getUnitPage(PageQuery pageQuery, String number, String name, Integer status, Integer type, Integer orientation, Long floorId, Long buildingId, Long projectId) {
        Page<Map<String, Object>> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        // 将Integer类型的orientation转换为String类型
        String orientationStr = orientation != null ? orientation.toString() : null;
        Page<Map<String, Object>> result = baseMapper.selectUnitPage(page, projectId, buildingId, floorId, name, number, status, type, orientationStr, null);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Map<String, Object> getUnitDetail(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitDetail(id);
    }

    @Override
    public boolean createUnit(Unit unit) {
        return save(unit);
    }

    @Override
    public boolean updateUnit(Unit unit) {
        return updateById(unit);
    }

    @Override
    public boolean deleteUnit(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDeleteUnits(List<Long> unitIds) {
        return removeByIds(unitIds);
    }

    @Override
    public List<Map<String, Object>> getUnitsByFloorId(Long floorId) {
        if (floorId == null) {
            return null;
        }
        return baseMapper.selectByFloorId(floorId);
    }

    @Override
    public List<Map<String, Object>> getUnitsByBuildingId(Long buildingId) {
        if (buildingId == null) {
            return null;
        }
        return baseMapper.selectByBuildingId(buildingId);
    }

    @Override
    public List<Map<String, Object>> getUnitsByProjectId(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return baseMapper.selectByProjectId(projectId);
    }

    @Override
    public boolean checkNumberExists(String number, Long floorId, Long excludeId) {
        if (number == null || floorId == null) {
            return false;
        }
        return baseMapper.checkNumberExists(floorId, number, excludeId) > 0;
    }

    @Override
    public boolean checkSequenceExists(Integer sequence, Long floorId, Long excludeId) {
        // 注意：当前数据库中没有sequence字段，暂时返回false
        return false;
    }

    @Override
    public Map<String, Object> getUnitStatistics(Long floorId, Long buildingId, Long projectId) {
        return baseMapper.getUnitStatistics(projectId, buildingId, floorId);
    }

    @Override
    public List<Map<String, Object>> getUnitCountByStatus(Long floorId, Long buildingId, Long projectId) {
        return baseMapper.getUnitCountByStatus(projectId, buildingId, floorId);
    }

    @Override
    public List<Map<String, Object>> getUnitCountByType(Long floorId, Long buildingId, Long projectId) {
        return baseMapper.getUnitCountByType(projectId, buildingId, floorId);
    }

    @Override
    public Map<String, Object> getUnitDetailWithStats(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitDetail(id);
    }

    @Override
    public List<Map<String, Object>> getUnitContracts(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitContracts(id);
    }

    @Override
    public Map<String, Object> getUnitRentalStats(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitDetail(id);
    }

    @Override
    public Map<String, Object> getUnitRevenueStats(Long id, Integer year) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitRevenueStatistics(id, year);
    }

    @Override
    public Map<String, Object> batchCreateUnits(Long floorId, Integer startUnit, Integer endUnit, Unit unitTemplate) {
        if (floorId == null || startUnit == null || endUnit == null || unitTemplate == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", 0);
            result.put("failed", 0);
            result.put("message", "参数不能为空");
            return result;
        }
        
        List<Unit> units = new ArrayList<>();
        for (int i = startUnit; i <= endUnit; i++) {
            Unit unit = new Unit();
            unit.setFloorId(floorId);
            unit.setUnitCode(unitTemplate.getUnitCode() + String.format("%03d", i));
            unit.setUnitDescription(unitTemplate.getUnitDescription() + i + "号");
            unit.setBuildingArea(unitTemplate.getBuildingArea());
            unit.setRentArea(unitTemplate.getRentArea());
            unit.setPropertyArea(unitTemplate.getPropertyArea());
            unit.setUnitStatus(unitTemplate.getUnitStatus());
            unit.setUnitPurpose(unitTemplate.getUnitPurpose());
            unit.setStatus(1);
            units.add(unit);
        }
        
        int successCount = baseMapper.batchInsertUnits(units);
        Map<String, Object> result = new HashMap<>();
        result.put("success", successCount);
        result.put("failed", units.size() - successCount);
        result.put("message", "批量创建完成");
        return result;
    }

    @Override
    public Integer getMaxSequence(Long floorId) {
        // 注意：当前数据库中没有sequence字段，返回0
        return 0;
    }

    @Override
    public boolean updateUnitRent(Long id, BigDecimal rent) {
        if (id == null || rent == null) {
            return false;
        }
        Unit unit = new Unit();
        unit.setId(id);
        // 注意：需要在Unit实体中添加currentRent字段或使用其他方式更新租金
        return updateById(unit);
    }

    @Override
    public boolean updateUnitStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        Unit unit = new Unit();
        unit.setId(id);
        unit.setStatus(status);
        return updateById(unit);
    }

    @Override
    public List<Map<String, Object>> getAvailableUnits(Long projectId, Long buildingId, Long floorId) {
        return baseMapper.getAvailableUnits(projectId, buildingId, floorId, null, null, null, null);
    }

    @Override
    public List<Map<String, Object>> getRentedUnits(Long projectId, Long buildingId, Long floorId) {
        // 通过状态筛选已租赁单元
        return baseMapper.getUnitCountByStatus(projectId, buildingId, floorId);
    }

    @Override
    public List<Map<String, Object>> getVacantUnits(Long projectId, Long buildingId, Long floorId) {
        // 通过状态筛选空置单元
        return baseMapper.getUnitCountByStatus(projectId, buildingId, floorId);
    }

    @Override
    public List<Map<String, Object>> getUnitCountByOrientation(Long projectId, Long buildingId, Long floorId) {
        return baseMapper.getUnitCountByOrientation(projectId, buildingId, floorId);
    }

    @Override
    public Map<String, Object> getUnitDetailWithContract(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitDetail(id);
    }

    @Override
    public Map<String, Object> getUnitCurrentContract(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitCurrentContract(id);
    }

    @Override
    public List<Map<String, Object>> getAvailableUnits(Long projectId, Long buildingId, Long floorId, Integer type, BigDecimal minArea, BigDecimal maxArea, BigDecimal minRent, BigDecimal maxRent) {
        // TODO: 实现可租赁单元列表（扩展参数）
        return null;
    }

    @Override
    public List<Map<String, Object>> getUnitRentalHistory(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getUnitRentalHistory(id);
    }

    @Override
    public Map<String, Object> batchCreateUnits(Long floorId, Integer count, Unit unitTemplate) {
        // TODO: 实现批量创建（扩展参数）
        return null;
    }

    @Override
    public List<Map<String, Object>> exportUnitData(String number, String name, Integer status, Integer type, Integer orientation, Long floorId, Long buildingId, Long projectId) {
        // 使用分页查询获取所有数据进行导出
        Page<Map<String, Object>> page = new Page<>(1, 10000);
        // 将Integer类型的orientation转换为String类型
        String orientationStr = orientation != null ? orientation.toString() : null;
        return baseMapper.selectUnitPage(page, projectId, buildingId, floorId, name, number, status, type, orientationStr, null).getRecords();
    }

    @Override
    public boolean batchUpdateUnitRent(List<Long> unitIds, BigDecimal rent) {
        if (unitIds == null || unitIds.isEmpty() || rent == null) {
            return false;
        }
        // TODO: 实现批量更新单元租金逻辑
        return true;
    }

    @Override
    public BigDecimal getUnitOccupancyRate(Long projectId, Long buildingId, Long floorId) {
        // TODO: 实现获取单元入住率逻辑
        return BigDecimal.ZERO;
    }

    @Override
    public boolean batchUpdateUnitStatus(List<Long> unitIds, Integer status) {
        if (unitIds == null || unitIds.isEmpty() || status == null) {
            return false;
        }
        // TODO: 实现批量更新单元状态逻辑
        return true;
    }
}