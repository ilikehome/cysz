package com.cysz.minimal.controller;

import com.cysz.minimal.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 楼栋管理控制器
 */
@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;
    
    /**
     * 分页查询楼栋
     */
    @GetMapping("/page")
    public Map<String, Object> getBuildingsPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer projectId) {
        
        System.out.println("分页查询楼栋 - current: " + current + ", size: " + size);
        return buildingService.getBuildingPage(current, size, keyword, projectId);
    }
    
    /**
     * 根据项目ID查询楼栋
     */
    @GetMapping("/project/{projectId}")
    public Map<String, Object> getBuildingsByProjectId(@PathVariable Integer projectId) {
        return buildingService.getBuildingsByProjectId(projectId);
    }
    
    /**
     * 创建楼栋
     */
    @PostMapping
    public Map<String, Object> createBuilding(@RequestBody Map<String, Object> buildingData) {
        return buildingService.createBuilding(buildingData);
    }
    
    /**
     * 更新楼栋
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateBuilding(@PathVariable Integer id, @RequestBody Map<String, Object> buildingData) {
        return buildingService.updateBuilding(id, buildingData);
    }
    
    /**
     * 删除楼栋
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteBuilding(@PathVariable Integer id) {
        System.out.println("删除楼栋 ID: " + id);
        return buildingService.deleteBuilding(id);
    }
}