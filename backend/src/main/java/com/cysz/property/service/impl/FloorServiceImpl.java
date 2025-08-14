package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Floor;
import com.cysz.property.mapper.FloorMapper;
import com.cysz.property.service.FloorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼层服务实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {

    @Override
    public PageResult<Map<String, Object>> getFloorPage(PageQuery pageQuery, String floorName, String floorCode, Long buildingId, Long projectId) {
        Page<Map<String, Object>> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        Page<Map<String, Object>> result = baseMapper.selectFloorPage(page, buildingId, floorName, floorCode, null, null);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Map<String, Object> getFloorDetail(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getFloorDetail(id);
    }

    @Override
    public boolean createFloor(Floor floor) {
        return save(floor);
    }

    @Override
    public boolean updateFloor(Floor floor) {
        return updateById(floor);
    }

    @Override
    public boolean deleteFloor(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDeleteFloors(List<Long> floorIds) {
        return removeByIds(floorIds);
    }

    @Override
    public boolean checkCodeExists(String floorCode, Long buildingId, Long excludeId) {
        if (floorCode == null || buildingId == null) {
            return false;
        }
        return baseMapper.checkNumberExists(buildingId, floorCode, excludeId) > 0;
    }

    @Override
    public Map<String, Object> getFloorStatistics(Long buildingId, Long projectId) {
        if (buildingId == null) {
            return null;
        }
        return baseMapper.getFloorStatistics(buildingId);
    }

    @Override
    public List<Map<String, Object>> getFloorCountByStatus(Long buildingId) {
        if (buildingId == null) {
            return null;
        }
        return baseMapper.getFloorCountByStatus(buildingId);
    }

    @Override
    public List<Map<String, Object>> getFloorCountByType(Long buildingId) {
        if (buildingId == null) {
            return null;
        }
        return baseMapper.getFloorCountByType(buildingId);
    }

    @Override
    public Map<String, Object> getFloorDetailWithStats(Long id) {
        if (id == null) {
            return null;
        }
        // 获取基本详情
        Map<String, Object> detail = getFloorDetail(id);
        if (detail == null) {
            return null;
        }
        
        // 获取单元统计
        List<Map<String, Object>> units = getFloorUnits(id);
        detail.put("units", units);
        detail.put("unitCount", units != null ? units.size() : 0);
        
        return detail;
    }

    @Override
    public List<Map<String, Object>> getFloorUnits(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getFloorUnits(id);
    }

    @Override
    public boolean updateFloorStats(Long id) {
        if (id == null) {
            return false;
        }
        // 更新楼层单元数量统计
        int unitResult = baseMapper.updateFloorUnitCount(id);
        // 更新楼层面积统计
        int areaResult = baseMapper.updateFloorAreaStatistics(id);
        return unitResult >= 0 && areaResult >= 0;
    }

    @Override
    public Map<String, Object> getFloorRentalStats(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getFloorRentalStatistics(id);
    }

    @Override
    public Map<String, Object> getFloorRevenueStats(Long id, Integer year) {
        if (id == null) {
            return null;
        }
        return baseMapper.getFloorRevenueStatistics(id, year);
    }

    @Override
    public Map<String, Object> batchCreateFloors(Long buildingId, Integer count, Floor floorTemplate) {
        if (buildingId == null || count == null || count <= 0 || floorTemplate == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", 0);
            result.put("failed", 0);
            result.put("message", "参数无效");
            return result;
        }
        
        List<Floor> floors = new ArrayList<>();
        int maxOrder = getMaxSequence(buildingId);
        
        for (int i = 1; i <= count; i++) {
            Floor floor = new Floor();
            floor.setBuildingId(buildingId);
            floor.setFloorName(floorTemplate.getFloorName() + i);
            floor.setFloorCode(floorTemplate.getFloorCode() + String.format("%02d", i));
            floor.setFloorNumber(maxOrder + i);
            floor.setType(floorTemplate.getType());
            floor.setStatus(floorTemplate.getStatus());
            floor.setDescription(floorTemplate.getDescription());
            floors.add(floor);
        }
        
        int successCount = baseMapper.batchInsertFloors(floors);
        Map<String, Object> result = new HashMap<>();
        result.put("success", successCount);
        result.put("failed", count - successCount);
        result.put("message", "批量创建完成");
        return result;
    }

    @Override
    public Integer getMaxSequence(Long buildingId) {
        if (buildingId == null) {
            return 0;
        }
        return baseMapper.getMaxFloorOrder(buildingId);
    }

    @Override
    public Integer getMinSequence(Long buildingId) {
        if (buildingId == null) {
            return 0;
        }
        return baseMapper.getMinFloorOrder(buildingId);
    }

    @Override
    public List<Map<String, Object>> exportFloorData(String floorName, String floorCode, Long buildingId, Long projectId) {
        log.info("导出楼层数据: floorName={}, floorCode={}, buildingId={}, projectId={}", floorName, floorCode, buildingId, projectId);
        // 使用分页查询获取所有数据进行导出
        PageQuery pageQuery = new PageQuery();
        pageQuery.setCurrent(1);
        pageQuery.setSize(10000); // 设置较大的页面大小以获取所有数据
        PageResult<Map<String, Object>> pageResult = getFloorPage(pageQuery, floorName, floorCode, buildingId, projectId);
        return pageResult.getRecords();
    }

    @Override
    public List<Map<String, Object>> getFloorsByBuildingId(Long buildingId) {
        if (buildingId == null) {
            return null;
        }
        return baseMapper.selectByBuildingId(buildingId);
    }
}