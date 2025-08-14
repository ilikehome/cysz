package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Building;
import com.cysz.property.mapper.BuildingMapper;
import com.cysz.property.service.BuildingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 楼栋服务实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Override
    public PageResult<Map<String, Object>> getBuildingPage(PageQuery pageQuery, String number, String name, Integer status, Integer type, Long projectId) {
        Page<Map<String, Object>> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        baseMapper.selectBuildingPage(page, projectId, name, number, status, type);
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public Map<String, Object> getBuildingDetail(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getBuildingDetail(id);
    }

    @Override
    public boolean createBuilding(Building building) {
        return save(building);
    }

    @Override
    public boolean updateBuilding(Building building) {
        return updateById(building);
    }

    @Override
    public boolean deleteBuilding(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDeleteBuildings(List<Long> buildingIds) {
        return removeByIds(buildingIds);
    }

    @Override
    public List<Map<String, Object>> getBuildingsByProjectId(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return baseMapper.selectByProjectId(projectId);
    }

    @Override
    public boolean checkNumberExists(String number, Long projectId, Long excludeId) {
        if (number == null || number.trim().isEmpty() || projectId == null) {
            return false;
        }
        return baseMapper.checkNumberExists(projectId, number.trim(), excludeId) > 0;
    }

    @Override
    public boolean checkSequenceExists(Integer sequence, Long projectId, Long excludeId) {
        // 注意：当前数据库设计中没有sequence字段，此方法暂时返回false
        // 如需要可以在Building实体和数据库中添加sequence字段
        return false;
    }

    @Override
    public Map<String, Object> getBuildingStatistics(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return baseMapper.getBuildingStatistics(projectId);
    }

    @Override
    public List<Map<String, Object>> getBuildingCountByStatus(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return baseMapper.getBuildingCountByStatus(projectId);
    }

    @Override
    public List<Map<String, Object>> getBuildingCountByType(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return baseMapper.getBuildingCountByType(projectId);
    }

    @Override
    public Map<String, Object> getBuildingDetailWithStats(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getBuildingDetail(id);
    }

    @Override
    public List<Map<String, Object>> getBuildingFloors(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getBuildingFloors(id);
    }

    @Override
    public boolean updateBuildingStats(Long id) {
        if (id == null) {
            return false;
        }
        // 更新楼栋单元数量统计
        baseMapper.updateBuildingUnitCount(id);
        // 更新楼栋面积统计
        baseMapper.updateBuildingAreaStatistics(id);
        return true;
    }

    @Override
    public Map<String, Object> getBuildingRentalStats(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getBuildingRentalStatistics(id);
    }

    @Override
    public Map<String, Object> getBuildingRevenueStats(Long id, Integer year) {
        if (id == null) {
            return null;
        }
        return baseMapper.getBuildingRevenueStatistics(id, year);
    }

    @Override
    public Map<String, Object> batchCreateBuildings(Long projectId, Integer startBuilding, Integer endBuilding, Building buildingTemplate) {
        if (projectId == null || startBuilding == null || endBuilding == null || buildingTemplate == null) {
            return Map.of("success", false, "message", "参数不能为空");
        }
        
        if (startBuilding > endBuilding) {
            return Map.of("success", false, "message", "起始楼栋号不能大于结束楼栋号");
        }
        
        int successCount = 0;
        int failCount = 0;
        
        for (int i = startBuilding; i <= endBuilding; i++) {
            Building building = new Building();
            building.setProjectId(projectId);
            building.setBuildingCode(buildingTemplate.getBuildingCode() + i);
            building.setBuildingName(buildingTemplate.getBuildingName() + i + "号楼");
            building.setBuildingType(buildingTemplate.getBuildingType());
            building.setFloorCount(buildingTemplate.getFloorCount());
            building.setBuildingArea(buildingTemplate.getBuildingArea());
            building.setRentArea(buildingTemplate.getRentArea());
            building.setPropertyArea(buildingTemplate.getPropertyArea());
            building.setUsableArea(buildingTemplate.getUsableArea());
            building.setBuildingStatus(buildingTemplate.getBuildingStatus());
            building.setDescription(buildingTemplate.getDescription());
            building.setStatus(1);
            building.setDeleted(0);
            
            if (save(building)) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        return Map.of(
            "success", true,
            "successCount", successCount,
            "failCount", failCount,
            "message", String.format("批量创建完成，成功%d个，失败%d个", successCount, failCount)
        );
    }

    @Override
    public Integer getMaxSequence(Long projectId) {
        // 注意：当前数据库设计中没有sequence字段，返回0
        // 如需要可以在Building实体和数据库中添加sequence字段
        return 0;
    }

    @Override
    public List<Map<String, Object>> getBuildingUnits(Long buildingId) {
        if (buildingId == null) {
            return null;
        }
        return baseMapper.getBuildingUnits(buildingId);
    }

    @Override
    public String exportBuildingData(String number, String name, Integer status, Integer type, Long projectId) {
        // 导出功能需要根据具体需求实现，这里返回提示信息
        log.info("导出楼栋数据请求：projectId={}, number={}, name={}, status={}, type={}", 
                projectId, number, name, status, type);
        return "导出功能待实现，请联系管理员";
    }
}