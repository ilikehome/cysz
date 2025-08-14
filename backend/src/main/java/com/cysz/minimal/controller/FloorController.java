package com.cysz.minimal;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 楼层管理控制器
 */
@RestController
@RequestMapping("/floor")
@CrossOrigin
public class FloorController {
    
    // 模拟数据存储
    private static final Map<Integer, Map<String, Object>> floorStorage = new HashMap<>();
    private static int nextId = 1;
    
    static {
        // 初始化一些测试数据
        Map<String, Object> floor1 = new HashMap<>();
        floor1.put("id", nextId++);
        floor1.put("floorName", "1层");
        floor1.put("floorCode", "F001");
        floor1.put("buildingId", 1);
        floor1.put("buildingName", "A栋");
        floor1.put("remark", "一层大厅及办公区");
        floor1.put("status", 1);
        floor1.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        floorStorage.put((Integer) floor1.get("id"), floor1);
        
        Map<String, Object> floor2 = new HashMap<>();
        floor2.put("id", nextId++);
        floor2.put("floorName", "2层");
        floor2.put("floorCode", "F002");
        floor2.put("buildingId", 1);
        floor2.put("buildingName", "A栋");
        floor2.put("remark", "二层办公区");
        floor2.put("status", 1);
        floor2.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        floorStorage.put((Integer) floor2.get("id"), floor2);
    }
    
    /**
     * 分页查询楼层
     */
    @GetMapping("/page")
    public Map<String, Object> getFloorPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer buildingId) {
        
        System.out.println("楼层分页查询 - current: " + current + ", size: " + size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> allFloors = new ArrayList<>(floorStorage.values());
            
            // 过滤条件
            if (keyword != null && !keyword.trim().isEmpty()) {
                allFloors = allFloors.stream()
                        .filter(floor -> {
                            String floorName = (String) floor.get("floorName");
                            String floorCode = (String) floor.get("floorCode");
                            return (floorName != null && floorName.contains(keyword)) ||
                                   (floorCode != null && floorCode.contains(keyword));
                        })
                        .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            }
            
            if (buildingId != null) {
                allFloors = allFloors.stream()
                        .filter(floor -> buildingId.equals(floor.get("buildingId")))
                        .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            }
            
            // 分页处理
            int total = allFloors.size();
            int start = (current - 1) * size;
            int end = Math.min(start + size, total);
            
            List<Map<String, Object>> records = new ArrayList<>();
            if (start < total) {
                records = allFloors.subList(start, end);
            }
            
            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("records", records);
            pageResult.put("total", total);
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
    
    /**
     * 根据楼栋ID获取楼层列表
     */
    @GetMapping("/building/{buildingId}")
    public Map<String, Object> getFloorsByBuilding(@PathVariable Integer buildingId) {
        System.out.println("根据楼栋ID获取楼层列表: " + buildingId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> floors = floorStorage.values().stream()
                    .filter(floor -> buildingId.equals(floor.get("buildingId")))
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            
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
    
    /**
     * 创建楼层
     */
    @PostMapping
    public Map<String, Object> createFloor(@RequestBody Map<String, Object> floorData) {
        System.out.println("创建楼层: " + floorData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 创建新楼层
            Map<String, Object> newFloor = new HashMap<>(floorData);
            newFloor.put("id", nextId++);
            newFloor.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            newFloor.put("status", 1);
            
            // 获取楼栋名称
            Integer buildingId = (Integer) floorData.get("buildingId");
            newFloor.put("buildingName", getBuildingName(buildingId));
            
            floorStorage.put((Integer) newFloor.get("id"), newFloor);
            
            response.put("code", 200);
            response.put("message", "创建成功");
            response.put("data", newFloor);
            
            System.out.println("楼层创建成功，ID: " + newFloor.get("id"));
            
        } catch (Exception e) {
            System.err.println("创建楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 更新楼层
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateFloor(@PathVariable Integer id, @RequestBody Map<String, Object> floorData) {
        System.out.println("更新楼层 ID: " + id + ", 数据: " + floorData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> existingFloor = floorStorage.get(id);
            if (existingFloor == null) {
                response.put("code", 404);
                response.put("message", "楼层不存在");
                response.put("data", null);
                return response;
            }
            
            // 更新楼层信息
            existingFloor.putAll(floorData);
            existingFloor.put("id", id); // 确保ID不被覆盖
            existingFloor.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // 获取楼栋名称
            Integer buildingId = (Integer) floorData.get("buildingId");
            existingFloor.put("buildingName", getBuildingName(buildingId));
            
            response.put("code", 200);
            response.put("message", "更新成功");
            response.put("data", existingFloor);
            
        } catch (Exception e) {
            System.err.println("更新楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 删除楼层
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteFloor(@PathVariable Integer id) {
        System.out.println("删除楼层 ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查楼层是否存在单元
            if (hasUnitsInFloor(id)) {
                response.put("code", 400);
                response.put("message", "该楼层下存在单元，无法删除");
                response.put("data", null);
                return response;
            }
            
            Map<String, Object> existingFloor = floorStorage.get(id);
            if (existingFloor == null) {
                response.put("code", 404);
                response.put("message", "楼层不存在");
                response.put("data", null);
                return response;
            }
            
            floorStorage.remove(id);
            
            response.put("code", 200);
            response.put("message", "删除成功");
            response.put("data", null);
            
        } catch (Exception e) {
            System.err.println("删除楼层失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 检查楼层下是否有单元
     */
    private boolean hasUnitsInFloor(Integer floorId) {
        // 这里应该查询单元表，暂时返回false
        // TODO: 实现真实的单元检查逻辑
        return false;
    }
    
    /**
     * 根据楼栋ID获取楼栋名称
     */
    private String getBuildingName(Integer buildingId) {
        if (buildingId == null) return "";
        
        // 模拟楼栋名称映射
        Map<Integer, String> buildingNames = new HashMap<>();
        buildingNames.put(1, "A栋");
        buildingNames.put(2, "B栋");
        buildingNames.put(3, "C栋");
        
        return buildingNames.getOrDefault(buildingId, "未知楼栋");
    }
}