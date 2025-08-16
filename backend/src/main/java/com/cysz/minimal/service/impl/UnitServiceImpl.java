package com.cysz.minimal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.*;
import com.cysz.minimal.enums.UnitPurpose;
import com.cysz.minimal.mapper.*;
import com.cysz.minimal.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单元管理服务实现类
 */
@Service
public class UnitServiceImpl implements UnitService {
    
    @Autowired
    private UnitMapper unitMapper;
    
    @Autowired
    private FloorMapper floorMapper;
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private ContractMapper contractMapper;
    
    @Override
    public Map<String, Object> getUnitPage(int current, int size, Integer projectId, Integer buildingId, Integer floorId, String unitStatus) {
        System.out.println("分页查询单元 - 当前页: " + current + ", 页大小: " + size + ", 项目ID: " + projectId + ", 楼栋ID: " + buildingId + ", 楼层ID: " + floorId + ", 单元状态: " + unitStatus);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
            
            // 根据项目ID过滤
            if (projectId != null) {
                // 需要通过building和floor关联查询
                List<Building> buildings = buildingMapper.selectList(new QueryWrapper<Building>().eq("project_id", projectId));
                if (!buildings.isEmpty()) {
                    List<Long> buildingIds = buildings.stream().map(Building::getId).collect(Collectors.toList());
                    List<Floor> floors = floorMapper.selectList(new QueryWrapper<Floor>().in("building_id", buildingIds));
                    if (!floors.isEmpty()) {
                        List<Long> floorIds = floors.stream().map(Floor::getId).collect(Collectors.toList());
                        queryWrapper.in("floor_id", floorIds);
                    } else {
                        // 没有楼层，返回空结果
                        Page<Unit> emptyPage = new Page<>(current, size);
                        response.put("code", 200);
                        response.put("message", "查询成功");
                        response.put("data", Map.of(
                            "records", new ArrayList<>(),
                            "total", 0L,
                            "size", (long) size,
                            "current", (long) current,
                            "pages", 0L
                        ));
                        return response;
                    }
                } else {
                    // 没有楼栋，返回空结果
                    Page<Unit> emptyPage = new Page<>(current, size);
                    response.put("code", 200);
                    response.put("message", "查询成功");
                    response.put("data", Map.of(
                        "records", new ArrayList<>(),
                        "total", 0L,
                        "size", (long) size,
                        "current", (long) current,
                        "pages", 0L
                    ));
                    return response;
                }
            }
            
            if (buildingId != null) {
                // 需要通过floor关联查询
                List<Floor> floors = floorMapper.selectList(new QueryWrapper<Floor>().eq("building_id", buildingId));
                if (!floors.isEmpty()) {
                    List<Long> floorIds = floors.stream().map(Floor::getId).collect(Collectors.toList());
                    queryWrapper.in("floor_id", floorIds);
                } else {
                    // 没有楼层，返回空结果
                    Page<Unit> emptyPage = new Page<>(current, size);
                    response.put("code", 200);
                    response.put("message", "查询成功");
                    response.put("data", Map.of(
                        "records", new ArrayList<>(),
                        "total", 0L,
                        "size", (long) size,
                        "current", (long) current,
                        "pages", 0L
                    ));
                    return response;
                }
            }
            
            if (floorId != null) {
                queryWrapper.eq("floor_id", floorId);
            }
            
            if (unitStatus != null && !unitStatus.trim().isEmpty()) {
                // 验证单元状态是否有效
                if (Arrays.asList("RENTABLE", "RENTED", "DISABLED").contains(unitStatus)) {
                    queryWrapper.eq("unit_status", unitStatus.trim());
                } else {
                    System.err.println("无效的单元状态代码: " + unitStatus);
                }
            }
            
            queryWrapper.orderByDesc("create_time");
            
            Page<Unit> page = new Page<>(current, size);
            Page<Unit> unitPage = unitMapper.selectPage(page, queryWrapper);
            
            // 转换为Map格式并添加关联信息
            List<Map<String, Object>> units = new ArrayList<>();
            for (Unit unit : unitPage.getRecords()) {
                Map<String, Object> unitMap = new HashMap<>();
                unitMap.put("id", unit.getId());
                unitMap.put("unitName", unit.getUnitName());
                unitMap.put("unitCode", unit.getUnitCode());
                unitMap.put("floorId", unit.getFloorId());
                unitMap.put("unitStatus", unit.getUnitStatus());
                unitMap.put("unitPurpose", unit.getUnitPurpose());
                unitMap.put("buildingArea", unit.getBuildingArea());
                unitMap.put("rentArea", unit.getRentArea());
                unitMap.put("isMultiTenant", unit.getIsMultiTenant());
                unitMap.put("remark", unit.getRemark());
                unitMap.put("createTime", unit.getCreateTime());
                unitMap.put("updateTime", unit.getUpdateTime());
                
                // 获取关联信息
                if (unit.getFloorId() != null) {
                    Floor floor = floorMapper.selectById(unit.getFloorId());
                    if (floor != null) {
                        unitMap.put("floorName", floor.getFloorName());
                        if (floor.getBuildingId() != null) {
                            Building building = buildingMapper.selectById(floor.getBuildingId());
                            if (building != null) {
                                unitMap.put("buildingName", building.getBuildingName());
                                if (building.getProjectId() != null) {
                                    Project project = projectMapper.selectById(building.getProjectId());
                                    if (project != null) {
                                        unitMap.put("projectName", project.getProjectName());
                                    }
                                }
                            }
                        }
                    }
                }
                
                units.add(unitMap);
            }
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", Map.of(
                "records", units,
                "total", unitPage.getTotal(),
                "size", unitPage.getSize(),
                "current", unitPage.getCurrent(),
                "pages", unitPage.getPages()
            ));
            
        } catch (Exception e) {
            System.err.println("分页查询单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", Map.of(
                "records", new ArrayList<>(),
                "total", 0L,
                "size", (long) size,
                "current", (long) current,
                "pages", 0L
            ));
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getUnitList(Integer projectId, Integer buildingId, Integer floorId, String unitStatus) {
        System.out.println("获取单元列表 - 项目ID: " + projectId + ", 楼栋ID: " + buildingId + ", 楼层ID: " + floorId + ", 单元状态: " + unitStatus);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
            
            // 根据项目ID过滤
            if (projectId != null) {
                // 需要通过building和floor关联查询
                List<Building> buildings = buildingMapper.selectList(new QueryWrapper<Building>().eq("project_id", projectId));
                if (!buildings.isEmpty()) {
                    List<Long> buildingIds = buildings.stream().map(Building::getId).toList();
                    List<Floor> floors = floorMapper.selectList(new QueryWrapper<Floor>().in("building_id", buildingIds));
                    if (!floors.isEmpty()) {
                        List<Long> floorIds = floors.stream().map(Floor::getId).toList();
                        queryWrapper.in("floor_id", floorIds);
                    } else {
                        // 没有楼层，返回空结果
                        response.put("code", 200);
                        response.put("message", "查询成功");
                        response.put("data", new ArrayList<>());
                        return response;
                    }
                } else {
                    // 没有楼栋，返回空结果
                    response.put("code", 200);
                    response.put("message", "查询成功");
                    response.put("data", new ArrayList<>());
                    return response;
                }
            }
            
            if (buildingId != null) {
                // 需要通过floor关联查询
                List<Floor> floors = floorMapper.selectList(new QueryWrapper<Floor>().eq("building_id", buildingId));
                if (!floors.isEmpty()) {
                    List<Long> floorIds = floors.stream().map(Floor::getId).toList();
                    queryWrapper.in("floor_id", floorIds);
                } else {
                    // 没有楼层，返回空结果
                    response.put("code", 200);
                    response.put("message", "查询成功");
                    response.put("data", new ArrayList<>());
                    return response;
                }
            }
            
            if (floorId != null) {
                queryWrapper.eq("floor_id", floorId);
            }
            
            if (unitStatus != null && !unitStatus.trim().isEmpty()) {
                // 验证单元状态是否有效
                if (Arrays.asList("RENTABLE", "RENTED", "DISABLED").contains(unitStatus)) {
                    queryWrapper.eq("unit_status", unitStatus.trim());
                } else {
                    System.err.println("无效的单元状态代码: " + unitStatus);
                }
            }
            
            queryWrapper.orderByDesc("create_time");
            
            List<Unit> unitList = unitMapper.selectList(queryWrapper);
            
            // 转换为Map格式并添加关联信息
            List<Map<String, Object>> units = new ArrayList<>();
            for (Unit unit : unitList) {
                Map<String, Object> unitMap = new HashMap<>();
                unitMap.put("id", unit.getId());
                unitMap.put("unitName", unit.getUnitName());
                unitMap.put("unitCode", unit.getUnitCode());
                unitMap.put("floorId", unit.getFloorId());
                unitMap.put("unitStatus", unit.getUnitStatus());
                unitMap.put("unitPurpose", unit.getUnitPurpose());
                unitMap.put("buildingArea", unit.getBuildingArea());
                unitMap.put("rentArea", unit.getRentArea());
                unitMap.put("isMultiTenant", unit.getIsMultiTenant());
                unitMap.put("remark", unit.getRemark());
                unitMap.put("createTime", unit.getCreateTime());
                unitMap.put("updateTime", unit.getUpdateTime());
                
                // 获取关联信息
                if (unit.getFloorId() != null) {
                    Floor floor = floorMapper.selectById(unit.getFloorId());
                    if (floor != null) {
                        unitMap.put("floorName", floor.getFloorName());
                        if (floor.getBuildingId() != null) {
                            Building building = buildingMapper.selectById(floor.getBuildingId());
                            if (building != null) {
                                unitMap.put("buildingName", building.getBuildingName());
                                if (building.getProjectId() != null) {
                                    Project project = projectMapper.selectById(building.getProjectId());
                                    if (project != null) {
                                        unitMap.put("projectName", project.getProjectName());
                                    }
                                }
                            }
                        }
                    }
                }
                
                units.add(unitMap);
            }
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", units);
            
        } catch (Exception e) {
            System.err.println("获取单元列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getUnitsByFloor(Integer floorId) {
        System.out.println("根据楼层ID获取单元列表 - 楼层ID: " + floorId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("floor_id", floorId)
                       .eq("status", 1)
                       .orderByAsc("unit_name");
            
            List<Unit> unitList = unitMapper.selectList(queryWrapper);
            
            // 转换为Map格式
            List<Map<String, Object>> units = new ArrayList<>();
            for (Unit unit : unitList) {
                Map<String, Object> unitMap = new HashMap<>();
                unitMap.put("id", unit.getId());
                unitMap.put("unitName", unit.getUnitName());
                unitMap.put("unitCode", unit.getUnitCode());
                unitMap.put("floorId", unit.getFloorId());
                unitMap.put("unitStatus", unit.getUnitStatus());
                unitMap.put("unitPurpose", unit.getUnitPurpose());
                unitMap.put("buildingArea", unit.getBuildingArea());
                unitMap.put("rentArea", unit.getRentArea());
                unitMap.put("isMultiTenant", unit.getIsMultiTenant());
                unitMap.put("remark", unit.getRemark());
                unitMap.put("createTime", unit.getCreateTime());
                unitMap.put("updateTime", unit.getUpdateTime());
                units.add(unitMap);
            }
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", units);
            
        } catch (Exception e) {
            System.err.println("根据楼层ID获取单元列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getAvailableUnits(Integer projectId) {
        System.out.println("根据项目ID获取可用单元列表 - 项目ID: " + projectId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 先获取项目下的所有楼栋
            List<Building> buildings = buildingMapper.selectList(new QueryWrapper<Building>().eq("project_id", projectId));
            if (buildings.isEmpty()) {
                response.put("code", 200);
                response.put("message", "查询成功");
                response.put("data", new ArrayList<>());
                return response;
            }
            
            // 获取楼栋下的所有楼层
            List<Long> buildingIds = buildings.stream().map(Building::getId).toList();
            List<Floor> floors = floorMapper.selectList(new QueryWrapper<Floor>().in("building_id", buildingIds));
            if (floors.isEmpty()) {
                response.put("code", 200);
                response.put("message", "查询成功");
                response.put("data", new ArrayList<>());
                return response;
            }
            
            // 获取楼层下的可用单元
            List<Long> floorIds = floors.stream().map(Floor::getId).toList();
            QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("floor_id", floorIds)
                       .eq("unit_status", "RENTABLE");
            
            List<Unit> unitList = unitMapper.selectList(queryWrapper);
            
            // 转换为Map格式并添加关联信息
            List<Map<String, Object>> units = new ArrayList<>();
            for (Unit unit : unitList) {
                Map<String, Object> unitMap = new HashMap<>();
                unitMap.put("id", unit.getId());
                unitMap.put("unitName", unit.getUnitName());
                unitMap.put("unitCode", unit.getUnitCode());
                unitMap.put("floorId", unit.getFloorId());
                unitMap.put("unitStatus", unit.getUnitStatus());
                unitMap.put("unitPurpose", unit.getUnitPurpose());
                unitMap.put("buildingArea", unit.getBuildingArea());
                unitMap.put("rentArea", unit.getRentArea());
                unitMap.put("isMultiTenant", unit.getIsMultiTenant());
                unitMap.put("remark", unit.getRemark());
                
                // 获取关联信息
                if (unit.getFloorId() != null) {
                    Floor floor = floors.stream().filter(f -> f.getId().equals(unit.getFloorId())).findFirst().orElse(null);
                    if (floor != null) {
                        unitMap.put("floorName", floor.getFloorName());
                        if (floor.getBuildingId() != null) {
                            Building building = buildings.stream().filter(b -> b.getId().equals(floor.getBuildingId())).findFirst().orElse(null);
                            if (building != null) {
                                unitMap.put("buildingName", building.getBuildingName());
                            }
                        }
                    }
                }
                
                units.add(unitMap);
            }
            
            // 按楼栋名称、楼层名称、单元名称排序
            units.sort((u1, u2) -> {
                String building1 = (String) u1.get("buildingName");
                String building2 = (String) u2.get("buildingName");
                if (building1 != null && building2 != null && !building1.equals(building2)) {
                    return building1.compareTo(building2);
                }
                
                String floor1 = (String) u1.get("floorName");
                String floor2 = (String) u2.get("floorName");
                if (floor1 != null && floor2 != null && !floor1.equals(floor2)) {
                    return floor1.compareTo(floor2);
                }
                
                String unit1 = (String) u1.get("unitName");
                String unit2 = (String) u2.get("unitName");
                if (unit1 != null && unit2 != null) {
                    return unit1.compareTo(unit2);
                }
                
                return 0;
            });
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", units);
            
        } catch (Exception e) {
            System.err.println("根据项目ID获取可用单元列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> createUnit(Map<String, Object> unitData) {
        System.out.println("创建单元: " + unitData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证必填字段
            if (unitData.get("unitName") == null || unitData.get("unitName").toString().trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "单元名称不能为空");
                return response;
            }
            
            if (unitData.get("floorId") == null) {
                response.put("code", 400);
                response.put("message", "楼层ID不能为空");
                return response;
            }
            
            // 验证单元状态
            String unitStatus = (String) unitData.get("unitStatus");
            if (unitStatus != null && !Arrays.asList("RENTABLE", "RENTED", "DISABLED").contains(unitStatus)) {
                response.put("code", 400);
                response.put("message", "无效的单元状态代码: " + unitStatus);
                return response;
            }
            
            // 验证单元用途
            String unitPurpose = (String) unitData.get("unitPurpose");
            if (unitPurpose != null && !UnitPurpose.isValidCode(unitPurpose)) {
                response.put("code", 400);
                response.put("message", "无效的单元用途代码: " + unitPurpose);
                return response;
            }
            
            // 创建Unit实体
            Unit unit = new Unit();
            unit.setUnitName(unitData.get("unitName").toString().trim());
            unit.setUnitCode((String) unitData.get("unitCode"));
            unit.setFloorId(Long.valueOf(unitData.get("floorId").toString()));
            unit.setUnitStatus(unitStatus != null ? unitStatus : "RENTABLE");
            unit.setUnitPurpose(unitPurpose != null ? unitPurpose : "OFFICE");
            
            // 处理面积字段
            if (unitData.get("buildingArea") != null) {
                unit.setBuildingArea(new java.math.BigDecimal(unitData.get("buildingArea").toString()));
            }
            if (unitData.get("rentArea") != null) {
                unit.setRentArea(new java.math.BigDecimal(unitData.get("rentArea").toString()));
            }
            
            unit.setIsMultiTenant(unitData.get("isMultiTenant") != null ? 
                Boolean.valueOf(unitData.get("isMultiTenant").toString()) : false);
            unit.setRemark((String) unitData.get("remark"));
            unit.setCreateTime(LocalDateTime.now());
            unit.setUpdateTime(LocalDateTime.now());
            
            int result = unitMapper.insert(unit);
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "单元创建成功");
                response.put("data", Map.of("id", unit.getId()));
            } else {
                response.put("code", 500);
                response.put("message", "单元创建失败");
            }
            
        } catch (Exception e) {
            System.err.println("创建单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> updateUnit(Integer id, Map<String, Object> unitData) {
        System.out.println("更新单元 - ID: " + id + ", 数据: " + unitData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证单元状态
            String unitStatus = (String) unitData.get("unitStatus");
            if (unitStatus != null && !unitStatus.trim().isEmpty() && !Arrays.asList("RENTABLE", "RENTED", "DISABLED").contains(unitStatus)) {
                response.put("code", 400);
                response.put("message", "无效的单元状态: " + unitStatus);
                return response;
            }
            
            // 验证单元用途
            String unitPurpose = (String) unitData.get("unitPurpose");
            if (unitPurpose != null && !unitPurpose.trim().isEmpty() && !UnitPurpose.isValidCode(unitPurpose)) {
                response.put("code", 400);
                response.put("message", "无效的单元用途: " + unitPurpose);
                return response;
            }
            
            UpdateWrapper<Unit> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("update_time", LocalDateTime.now());
            
            if (unitData.containsKey("unitName")) {
                updateWrapper.set("unit_name", unitData.get("unitName"));
            }
            if (unitData.containsKey("unitCode")) {
                updateWrapper.set("unit_code", unitData.get("unitCode"));
            }
            if (unitData.containsKey("floorId")) {
                updateWrapper.set("floor_id", unitData.get("floorId"));
            }
            if (unitData.containsKey("unitStatus")) {
                updateWrapper.set("unit_status", unitData.get("unitStatus"));
            }
            if (unitData.containsKey("unitPurpose")) {
                updateWrapper.set("unit_purpose", unitData.get("unitPurpose"));
            }
            if (unitData.containsKey("buildingArea")) {
                updateWrapper.set("building_area", unitData.get("buildingArea"));
            }
            if (unitData.containsKey("rentArea")) {
                updateWrapper.set("rent_area", unitData.get("rentArea"));
            }
            if (unitData.containsKey("isMultiTenant")) {
                updateWrapper.set("is_multi_tenant", unitData.get("isMultiTenant"));
            }
            if (unitData.containsKey("remark")) {
                updateWrapper.set("remark", unitData.get("remark"));
            }
            
            int result = unitMapper.update(null, updateWrapper);
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "更新成功");
            } else {
                response.put("code", 404);
                response.put("message", "单元不存在");
            }
            
        } catch (Exception e) {
            System.err.println("更新单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> deleteUnit(Integer id) {
        System.out.println("删除单元 - ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查单元是否有关联的合同
            QueryWrapper<com.cysz.minimal.entity.Contract> contractQuery = new QueryWrapper<>();
            contractQuery.eq("unit_id", id);
            Long contractCount = contractMapper.selectCount(contractQuery);
            
            if (contractCount != null && contractCount > 0) {
                response.put("code", 400);
                response.put("message", "该单元存在关联合同，无法删除");
                return response;
            }
            
            int result = unitMapper.deleteById(id);
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "删除成功");
            } else {
                response.put("code", 404);
                response.put("message", "单元不存在");
            }
            
        } catch (Exception e) {
            System.err.println("删除单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> mergeUnits(Map<String, Object> mergeData) {
        System.out.println("单元合并: " + mergeData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 这里应该实现单元合并的业务逻辑
            // 暂时返回成功响应
            response.put("code", 200);
            response.put("message", "合并成功");
            
        } catch (Exception e) {
            System.err.println("单元合并失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "合并失败: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> splitUnit(Map<String, Object> splitData) {
        System.out.println("单元拆分: " + splitData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 这里应该实现单元拆分的业务逻辑
            // 暂时返回成功响应
            response.put("code", 200);
            response.put("message", "拆分成功");
            
        } catch (Exception e) {
            System.err.println("单元拆分失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "拆分失败: " + e.getMessage());
        }
        
        return response;
    }
}