package com.cysz.minimal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Building;
import com.cysz.minimal.entity.Floor;
import com.cysz.minimal.entity.Project;
import com.cysz.minimal.mapper.BuildingMapper;
import com.cysz.minimal.mapper.FloorMapper;
import com.cysz.minimal.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 楼栋管理控制器
 */
@RestController
@RequestMapping("/building")
@CrossOrigin
public class BuildingController {
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private FloorMapper floorMapper;
    
    /**
     * 楼栋分页查询
     */
    @GetMapping("/page")
    public Map<String, Object> getBuildingPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer projectId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Page<Building> page = new Page<>(current, size);
            QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("building_name", keyword)
                    .or()
                    .like("building_code", keyword)
                );
            }
            
            if (projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            
            queryWrapper.orderByDesc("id");
            
            Page<Building> pageResult = buildingMapper.selectPage(page, queryWrapper);
            
            // 转换为Map格式并添加项目名称
            List<Map<String, Object>> records = new ArrayList<>();
            for (Building building : pageResult.getRecords()) {
                Map<String, Object> buildingMap = new HashMap<>();
                buildingMap.put("id", building.getId());
                buildingMap.put("buildingName", building.getBuildingName());
                buildingMap.put("buildingCode", building.getBuildingCode());
                buildingMap.put("projectId", building.getProjectId());
                
                // 获取项目名称
                if (building.getProjectId() != null) {
                    Project project = projectMapper.selectById(building.getProjectId());
                    buildingMap.put("projectName", project != null ? project.getProjectName() : null);
                } else {
                    buildingMap.put("projectName", null);
                }
                
                buildingMap.put("buildingArea", building.getBuildingArea());
                buildingMap.put("rentArea", building.getRentArea());
                buildingMap.put("propertyArea", building.getPropertyArea());
                buildingMap.put("usableArea", building.getUsableArea());
                buildingMap.put("remark", building.getRemark());
                buildingMap.put("status", building.getStatus());
                buildingMap.put("createTime", building.getCreateTime());
                buildingMap.put("updateTime", building.getUpdateTime());
                records.add(buildingMap);
            }
            
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", records);
            pageData.put("total", pageResult.getTotal());
            pageData.put("current", pageResult.getCurrent());
            pageData.put("size", pageResult.getSize());
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", pageData);
            
        } catch (Exception e) {
            System.err.println("查询楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 根据项目ID查询楼栋
     */
    @GetMapping("/project/{projectId}")
    public Map<String, Object> getBuildingsByProjectId(@PathVariable Integer projectId) {
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
    
    /**
     * 创建楼栋
     */
    @PostMapping
    public Map<String, Object> createBuilding(@RequestBody Map<String, Object> buildingData) {
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
    
    /**
     * 更新楼栋
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateBuilding(@PathVariable Integer id, @RequestBody Map<String, Object> buildingData) {
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
    
    /**
     * 删除楼栋
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteBuilding(@PathVariable Integer id) {
        System.out.println("删除楼栋 ID: " + id);
        
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
}