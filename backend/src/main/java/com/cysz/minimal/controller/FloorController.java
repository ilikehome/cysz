package com.cysz.minimal.controller;

import com.cysz.minimal.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 楼层管理控制器
 */
@RestController
@RequestMapping("/floor")
@CrossOrigin
public class FloorController {
    
    @Autowired
    private FloorService floorService;
    
    /**
     * 楼层分页查询
     */
    @GetMapping("/page")
    public Map<String, Object> getFloorPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer buildingId) {
        
        return floorService.getFloorPage(current, size, keyword, buildingId);
    }
    
    /**
     * 根据楼栋ID获取楼层列表
     */
    @GetMapping("/building/{buildingId}")
    public Map<String, Object> getFloorsByBuilding(@PathVariable Integer buildingId) {
        return floorService.getFloorsByBuilding(buildingId);
    }
    
    /**
     * 创建楼层
     */
    @PostMapping
    public Map<String, Object> createFloor(@RequestBody Map<String, Object> floorData) {
        return floorService.createFloor(floorData);
    }
    
    /**
     * 更新楼层
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateFloor(@PathVariable Integer id, @RequestBody Map<String, Object> floorData) {
        return floorService.updateFloor(id, floorData);
    }
    
    /**
     * 删除楼层
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteFloor(@PathVariable Integer id) {
        return floorService.deleteFloor(id);
    }
}