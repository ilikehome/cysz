package com.cysz.minimal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Building;
import com.cysz.minimal.entity.Floor;
import com.cysz.minimal.entity.Project;
import com.cysz.minimal.mapper.BuildingMapper;
import com.cysz.minimal.mapper.FloorMapper;
import com.cysz.minimal.mapper.ProjectMapper;
import com.cysz.minimal.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private FloorMapper floorMapper;

    @Override
    public Map<String, Object> getBuildingPage(int current, int size, String keyword, Integer projectId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Page<Building> page = new Page<>(current, size);
            QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
            
            // 关键词搜索
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("building_name", keyword)
                    .or()
                    .like("building_code", keyword));
            }
            
            // 项目ID过滤
            if (projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            
            queryWrapper.orderByDesc("id");
            
            IPage<Building> buildingPage = buildingMapper.selectPage(page, queryWrapper);
            
            // 转换为Map格式
            List<Map<String, Object>> buildings = new ArrayList<>();
            for (Building building : buildingPage.getRecords()) {
                Map<String, Object> buildingMap = new HashMap<>();
                buildingMap.put("id", building.getId());
                buildingMap.put("buildingName", building.getBuildingName());
                buildingMap.put("buildingCode", building.getBuildingCode());
                buildingMap.put("projectId", building.getProjectId());
                buildingMap.put("buildingArea", building.getBuildingArea());
                buildingMap.put("rentArea", building.getRentArea());
                buildingMap.put("propertyArea", building.getPropertyArea());
                buildingMap.put("usableArea", building.getUsableArea());
                buildingMap.put("remark", building.getRemark());
                buildingMap.put("status", building.getStatus());
                buildingMap.put("createTime", building.getCreateTime());
                buildingMap.put("updateTime", building.getUpdateTime());
                
                // 获取项目名称
                if (building.getProjectId() != null) {
                    Project project = projectMapper.selectById(building.getProjectId());
                    if (project != null) {
                        buildingMap.put("projectName", project.getProjectName());
                    }
                }
                
                buildings.add(buildingMap);
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("records", buildings);
            data.put("total", buildingPage.getTotal());
            data.put("size", buildingPage.getSize());
            data.put("current", buildingPage.getCurrent());
            data.put("pages", buildingPage.getPages());
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", data);
            
        } catch (Exception e) {
            System.err.println("查询楼栋分页失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }

    @Override
    public Map<String, Object> getBuildingsByProjectId(Integer projectId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("project_id", projectId)
                       .orderByDesc("id");
            
            List<Building> buildingList = buildingMapper.selectList(queryWrapper);
            
            // 转换为Map格式
            List<Map<String, Object>> buildings = new ArrayList<>();
            for (Building building : buildingList) {
                Map<String, Object> buildingMap = new HashMap<>();
                buildingMap.put("id", building.getId());
                buildingMap.put("buildingName", building.getBuildingName());
                buildingMap.put("buildingCode", building.getBuildingCode());
                buildingMap.put("projectId", building.getProjectId());
                buildingMap.put("buildingArea", building.getBuildingArea());
                buildingMap.put("rentArea", building.getRentArea());
                buildingMap.put("propertyArea", building.getPropertyArea());
                buildingMap.put("usableArea", building.getUsableArea());
                buildingMap.put("remark", building.getRemark());
                buildingMap.put("status", building.getStatus());
                buildingMap.put("createTime", building.getCreateTime());
                buildingMap.put("updateTime", building.getUpdateTime());
                buildings.add(buildingMap);
            }
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", buildings);
            
        } catch (Exception e) {
            System.err.println("查询楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }

    @Override
    public Map<String, Object> createBuilding(Map<String, Object> buildingData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Building building = new Building();
            building.setBuildingName((String) buildingData.get("buildingName"));
            building.setBuildingCode((String) buildingData.get("buildingCode"));
            building.setProjectId((Integer) buildingData.get("projectId"));
            
            if (buildingData.get("buildingArea") != null) {
                building.setBuildingArea(new BigDecimal(buildingData.get("buildingArea").toString()));
            }
            if (buildingData.get("rentArea") != null) {
                building.setRentArea(new BigDecimal(buildingData.get("rentArea").toString()));
            }
            if (buildingData.get("propertyArea") != null) {
                building.setPropertyArea(new BigDecimal(buildingData.get("propertyArea").toString()));
            }
            if (buildingData.get("usableArea") != null) {
                building.setUsableArea(new BigDecimal(buildingData.get("usableArea").toString()));
            }
            
            building.setRemark((String) buildingData.get("remark"));
            building.setStatus(1);
            building.setCreateTime(LocalDateTime.now());
            building.setUpdateTime(LocalDateTime.now());
            
            int result = buildingMapper.insert(building);
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "创建成功");
                response.put("data", null);
            } else {
                response.put("code", 500);
                response.put("message", "创建失败");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("创建楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }

    @Override
    public Map<String, Object> updateBuilding(Integer id, Map<String, Object> buildingData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UpdateWrapper<Building> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            
            if (buildingData.containsKey("buildingName")) {
                updateWrapper.set("building_name", buildingData.get("buildingName"));
            }
            if (buildingData.containsKey("buildingCode")) {
                updateWrapper.set("building_code", buildingData.get("buildingCode"));
            }
            if (buildingData.containsKey("projectId")) {
                updateWrapper.set("project_id", buildingData.get("projectId"));
            }
            if (buildingData.containsKey("buildingArea")) {
                updateWrapper.set("building_area", buildingData.get("buildingArea"));
            }
            if (buildingData.containsKey("rentArea")) {
                updateWrapper.set("rent_area", buildingData.get("rentArea"));
            }
            if (buildingData.containsKey("propertyArea")) {
                updateWrapper.set("property_area", buildingData.get("propertyArea"));
            }
            if (buildingData.containsKey("usableArea")) {
                updateWrapper.set("usable_area", buildingData.get("usableArea"));
            }
            if (buildingData.containsKey("remark")) {
                updateWrapper.set("remark", buildingData.get("remark"));
            }
            if (buildingData.containsKey("status")) {
                updateWrapper.set("status", buildingData.get("status"));
            }
            
            updateWrapper.set("update_time", LocalDateTime.now());
            
            int result = buildingMapper.update(null, updateWrapper);
            
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "更新成功");
                response.put("data", null);
            } else {
                response.put("code", 404);
                response.put("message", "楼栋不存在");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("更新楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }

    @Override
    public Map<String, Object> deleteBuilding(Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查楼栋下是否有楼层
            QueryWrapper<Floor> floorQuery = new QueryWrapper<>();
            floorQuery.eq("building_id", id);
            Long floorCount = floorMapper.selectCount(floorQuery);
            
            if (floorCount != null && floorCount > 0) {
                response.put("code", 400);
                response.put("message", "该楼栋下存在楼层，无法删除");
                return response;
            }
            
            int result = buildingMapper.deleteById(id);
            
            response.put("code", 200);
            response.put("message", "删除成功");
            
        } catch (Exception e) {
            System.err.println("删除楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> getBuildingById(Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Building building = buildingMapper.selectById(id);
            
            if (building != null) {
                Map<String, Object> buildingMap = new HashMap<>();
                buildingMap.put("id", building.getId());
                buildingMap.put("buildingName", building.getBuildingName());
                buildingMap.put("buildingCode", building.getBuildingCode());
                buildingMap.put("projectId", building.getProjectId());
                buildingMap.put("buildingArea", building.getBuildingArea());
                buildingMap.put("rentArea", building.getRentArea());
                buildingMap.put("propertyArea", building.getPropertyArea());
                buildingMap.put("usableArea", building.getUsableArea());
                buildingMap.put("remark", building.getRemark());
                buildingMap.put("status", building.getStatus());
                buildingMap.put("createTime", building.getCreateTime());
                buildingMap.put("updateTime", building.getUpdateTime());
                
                // 获取项目名称
                if (building.getProjectId() != null) {
                    Project project = projectMapper.selectById(building.getProjectId());
                    if (project != null) {
                        buildingMap.put("projectName", project.getProjectName());
                    }
                }
                
                response.put("code", 200);
                response.put("message", "查询成功");
                response.put("data", buildingMap);
            } else {
                response.put("code", 404);
                response.put("message", "楼栋不存在");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("查询楼栋详情失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }


}