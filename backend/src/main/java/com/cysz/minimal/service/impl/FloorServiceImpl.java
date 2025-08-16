package com.cysz.minimal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.entity.Building;
import com.cysz.minimal.entity.Floor;
import com.cysz.minimal.entity.Unit;
import com.cysz.minimal.mapper.BuildingMapper;
import com.cysz.minimal.mapper.FloorMapper;
import com.cysz.minimal.mapper.UnitMapper;
import com.cysz.minimal.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 楼层服务实现类
 */
@Service
public class FloorServiceImpl implements FloorService {
    
    @Autowired
    private FloorMapper floorMapper;
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    @Autowired
    private UnitMapper unitMapper;
    
    @Override
    public Map<String, Object> getFloorPage(int current, int size, String keyword, Integer buildingId) {
        System.out.println("楼层分页查询 - current: " + current + ", size: " + size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 构建查询条件
            QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("floor_name", keyword)
                    .or()
                    .like("floor_code", keyword)
                );
            }
            
            if (buildingId != null) {
                queryWrapper.eq("building_id", buildingId);
            }
            
            queryWrapper.orderByDesc("id");
            
            // 分页查询
            Page<Floor> page = new Page<>(current, size);
            Page<Floor> floorPage = floorMapper.selectPage(page, queryWrapper);
            
            // 转换为Map格式并添加buildingName
            List<Map<String, Object>> records = new ArrayList<>();
            for (Floor floor : floorPage.getRecords()) {
                Map<String, Object> floorMap = new HashMap<>();
                floorMap.put("id", floor.getId());
                floorMap.put("floorName", floor.getFloorName());
                floorMap.put("floorCode", floor.getFloorCode());
                floorMap.put("buildingId", floor.getBuildingId());
                floorMap.put("remark", floor.getRemark());
                floorMap.put("createTime", floor.getCreateTime());
                floorMap.put("updateTime", floor.getUpdateTime());
                
                // 获取楼栋名称
                if (floor.getBuildingId() != null) {
                    Building building = buildingMapper.selectById(floor.getBuildingId());
                    floorMap.put("buildingName", building != null ? building.getBuildingName() : null);
                } else {
                    floorMap.put("buildingName", null);
                }
                
                records.add(floorMap);
            }
            
            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("records", records);
            pageResult.put("total", floorPage.getTotal());
            pageResult.put("current", current);
            pageResult.put("size", size);
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", pageResult);
            
        } catch (Exception e) {
            System.err.println("查询楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getFloorsByBuilding(Integer buildingId) {
        System.out.println("根据楼栋ID获取楼层列表: " + buildingId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("building_id", buildingId)
                       .orderByAsc("floor_name");
            
            List<Floor> floorList = floorMapper.selectList(queryWrapper);
            
            // 转换为Map格式
            List<Map<String, Object>> floors = new ArrayList<>();
            for (Floor floor : floorList) {
                Map<String, Object> floorMap = new HashMap<>();
                floorMap.put("id", floor.getId());
                floorMap.put("floorName", floor.getFloorName());
                floorMap.put("floorCode", floor.getFloorCode());
                floors.add(floorMap);
            }
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", floors);
            
        } catch (Exception e) {
            System.err.println("获取楼层列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> createFloor(Map<String, Object> floorData) {
        System.out.println("创建楼层: " + floorData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Floor floor = new Floor();
            floor.setFloorName((String) floorData.get("floorName"));
            floor.setFloorCode((String) floorData.get("floorCode"));
            floor.setBuildingId((Integer) floorData.get("buildingId"));
            floor.setRemark((String) floorData.get("remark"));
            floor.setCreateTime(LocalDateTime.now());
            
            floorMapper.insert(floor);
            
            response.put("code", 200);
            response.put("message", "创建成功");
            
            System.out.println("楼层创建成功");
            
        } catch (Exception e) {
            System.err.println("创建楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> updateFloor(Integer id, Map<String, Object> floorData) {
        System.out.println("更新楼层 ID: " + id + ", 数据: " + floorData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            UpdateWrapper<Floor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id)
                        .set("floor_name", floorData.get("floorName"))
                        .set("floor_code", floorData.get("floorCode"))
                        .set("building_id", floorData.get("buildingId"))
                        .set("remark", floorData.get("remark"))
                        .set("update_time", LocalDateTime.now());
            
            floorMapper.update(null, updateWrapper);
            
            response.put("code", 200);
            response.put("message", "更新成功");
            
        } catch (Exception e) {
            System.err.println("更新楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> deleteFloor(Integer id) {
        System.out.println("删除楼层 ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查楼层下是否有单元
            QueryWrapper<Unit> unitQueryWrapper = new QueryWrapper<>();
            unitQueryWrapper.eq("floor_id", id);
            Long unitCount = unitMapper.selectCount(unitQueryWrapper);
            
            if (unitCount != null && unitCount > 0) {
                response.put("code", 400);
                response.put("message", "该楼层下存在单元，无法删除");
                Map<String, Object> data = new HashMap<>();
                data.put("unitCount", unitCount);
                response.put("data", data);
                return response;
            }
            
            floorMapper.deleteById(id);
            
            response.put("code", 200);
            response.put("message", "删除成功");
            response.put("data", null);
            
        } catch (Exception e) {
            System.err.println("删除楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return response;
    }
}