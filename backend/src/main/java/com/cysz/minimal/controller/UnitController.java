package com.cysz.minimal.controller;

import com.cysz.minimal.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 单元管理控制器
 */
@RestController
@RequestMapping("/unit")
@CrossOrigin
public class UnitController {
    
    @Autowired
    private UnitService unitService;
    
    /**
     * 分页查询单元
     */
    @GetMapping("/page")
    public Map<String, Object> getUnitPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer buildingId,
            @RequestParam(required = false) Integer floorId,
            @RequestParam(required = false) String unitStatus) {
        
        return unitService.getUnitPage(current, size, projectId, buildingId, floorId, unitStatus);
    }
    
    /**
     * 获取单元列表
     */
    @GetMapping("/list")
    public Map<String, Object> getUnitList(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer buildingId,
            @RequestParam(required = false) Integer floorId,
            @RequestParam(required = false) String unitStatus) {
        
        return unitService.getUnitList(projectId, buildingId, floorId, unitStatus);
    }
    
    /**
     * 根据楼层ID获取单元列表
     */
    @GetMapping("/by-floor/{floorId}")
    public Map<String, Object> getUnitsByFloor(@PathVariable Integer floorId) {
        return unitService.getUnitsByFloor(floorId);
    }
    
    /**
     * 根据项目ID获取可用单元列表
     */
    @GetMapping("/available/{projectId}")
    public Map<String, Object> getAvailableUnits(@PathVariable Integer projectId) {
        return unitService.getAvailableUnits(projectId);
    }
    
    /**
     * 创建单元
     */
    @PostMapping
    public Map<String, Object> createUnit(@RequestBody Map<String, Object> unitData) {
        return unitService.createUnit(unitData);
    }
    
    /**
     * 更新单元
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateUnit(@PathVariable Integer id, @RequestBody Map<String, Object> unitData) {
        return unitService.updateUnit(id, unitData);
    }
    
    /**
     * 删除单元
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUnit(@PathVariable Integer id) {
        return unitService.deleteUnit(id);
    }
    
    /**
     * 单元合并
     */
    @PostMapping("/merge")
    public Map<String, Object> mergeUnits(@RequestBody Map<String, Object> mergeData) {
        return unitService.mergeUnits(mergeData);
    }
    
    /**
     * 单元拆分
     */
    @PostMapping("/split")
    public Map<String, Object> splitUnit(@RequestBody Map<String, Object> splitData) {
        return unitService.splitUnit(splitData);
    }
}