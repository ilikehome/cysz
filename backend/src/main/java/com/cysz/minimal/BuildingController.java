package com.cysz.minimal;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 楼栋管理控制器
 */
@RestController
@RequestMapping("/building")
@CrossOrigin
public class BuildingController {
    
    // 模拟数据存储
    private static final Map<Integer, Map<String, Object>> buildingStorage = new HashMap<>();
    private static int nextId = 1;
    
    static {
        // 初始化测试数据
        Map<String, Object> building1 = new HashMap<>();
        building1.put("id", 1);
        building1.put("buildingName", "A座");
        building1.put("buildingCode", "A001");
        building1.put("projectId", 1);
        building1.put("projectName", "万达广场");
        building1.put("buildingArea", 25000.0);
        building1.put("rentArea", 22000.0);
        building1.put("propertyArea", 23000.0);
        building1.put("usableArea", 21000.0);
        building1.put("status", 1);
        building1.put("createTime", "2024-01-10 10:30:00");
        buildingStorage.put(1, building1);
        
        Map<String, Object> building2 = new HashMap<>();
        building2.put("id", 2);
        building2.put("buildingName", "B座");
        building2.put("buildingCode", "B001");
        building2.put("projectId", 1);
        building2.put("projectName", "万达广场");
        building2.put("buildingArea", 25000.0);
        building2.put("rentArea", 22500.0);
        building2.put("propertyArea", 23500.0);
        building2.put("usableArea", 21500.0);
        building2.put("status", 1);
        building2.put("createTime", "2024-01-12 14:20:00");
        buildingStorage.put(2, building2);
        
        nextId = 3;
    }
    
    /**
     * 分页查询楼栋
     */
    @GetMapping("/page")
    public Map<String, Object> getBuildingPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer projectId) {
        
        System.out.println("楼栋分页查询 - current: " + current + ", size: " + size);
        
        List<Map<String, Object>> allBuildings = new ArrayList<>(buildingStorage.values());
        
        // 过滤数据
        if (keyword != null && !keyword.trim().isEmpty()) {
            allBuildings = allBuildings.stream()
                    .filter(building -> {
                        String buildingName = (String) building.get("buildingName");
                        String buildingCode = (String) building.get("buildingCode");
                        return (buildingName != null && buildingName.contains(keyword)) ||
                               (buildingCode != null && buildingCode.contains(keyword));
                    })
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        }
        
        if (projectId != null) {
            allBuildings = allBuildings.stream()
                    .filter(building -> projectId.equals(building.get("projectId")))
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        }
        
        // 分页处理
        int total = allBuildings.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = new ArrayList<>();
        if (start < total) {
            records = allBuildings.subList(start, end);
        }
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", records);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", pageResult);
        
        return response;
    }
    
    /**
     * 根据项目ID获取楼栋列表
     */
    @GetMapping("/project/{projectId}")
    public Map<String, Object> getBuildingsByProject(@PathVariable Integer projectId) {
        System.out.println("根据项目ID获取楼栋列表: " + projectId);
        
        List<Map<String, Object>> buildings = buildingStorage.values().stream()
                .filter(building -> projectId.equals(building.get("projectId")))
                .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", buildings);
        
        return response;
    }
    
    /**
     * 创建楼栋
     */
    @PostMapping
    public Map<String, Object> createBuilding(@RequestBody Map<String, Object> buildingData) {
        System.out.println("创建楼栋: " + buildingData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查楼栋编码是否重复
            String buildingCode = (String) buildingData.get("buildingCode");
            boolean codeExists = buildingStorage.values().stream()
                    .anyMatch(building -> buildingCode.equals(building.get("buildingCode")));
            
            if (codeExists) {
                response.put("code", 400);
                response.put("message", "楼栋编码已存在");
                response.put("data", null);
                return response;
            }
            
            // 创建新楼栋
            Map<String, Object> newBuilding = new HashMap<>(buildingData);
            newBuilding.put("id", nextId++);
            newBuilding.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            newBuilding.put("status", 1);
            
            buildingStorage.put((Integer) newBuilding.get("id"), newBuilding);
            
            response.put("code", 200);
            response.put("message", "创建成功");
            response.put("data", newBuilding);
            
            System.out.println("楼栋创建成功，ID: " + newBuilding.get("id"));
            
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
        System.out.println("更新楼栋 ID: " + id + ", 数据: " + buildingData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> existingBuilding = buildingStorage.get(id);
            if (existingBuilding == null) {
                response.put("code", 404);
                response.put("message", "楼栋不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查楼栋编码是否重复（排除自己）
            String buildingCode = (String) buildingData.get("buildingCode");
            boolean codeExists = buildingStorage.values().stream()
                    .anyMatch(building -> buildingCode.equals(building.get("buildingCode")) && 
                             !id.equals(building.get("id")));
            
            if (codeExists) {
                response.put("code", 400);
                response.put("message", "楼栋编码已存在");
                response.put("data", null);
                return response;
            }
            
            // 更新楼栋信息
            existingBuilding.putAll(buildingData);
            existingBuilding.put("id", id); // 确保ID不被覆盖
            existingBuilding.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            response.put("code", 200);
            response.put("message", "更新成功");
            response.put("data", existingBuilding);
            
            System.out.println("楼栋更新成功，ID: " + id);
            
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
            Map<String, Object> building = buildingStorage.remove(id);
            if (building == null) {
                response.put("code", 404);
                response.put("message", "楼栋不存在");
                response.put("data", null);
                return response;
            }
            
            response.put("code", 200);
            response.put("message", "删除成功");
            response.put("data", null);
            
            System.out.println("楼栋删除成功，ID: " + id);
            
        } catch (Exception e) {
            System.err.println("删除楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
}