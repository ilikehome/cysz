package com.cysz.minimal;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/unit")
@CrossOrigin(origins = "*")
public class UnitController {
    
    // 模拟数据存储
    private static final Map<Long, Unit> unitStorage = new HashMap<>();
    private static Long nextId = 1L;
    
    static {
        // 初始化一些测试数据
        Unit unit1 = new Unit();
        unit1.setId(nextId++);
        unit1.setUnitCode("A001-101");
        unit1.setUnitDescription("A栋1层101室");
        unit1.setProjectId(1L);
        unit1.setBuildingId(1L);
        unit1.setUnitStatus("VACANT");
        unit1.setUnitPurpose("办公");
        unit1.setBuildingArea(120.50);
        unit1.setRentArea(115.00);
        unit1.setPropertyArea(118.00);
        unit1.setStatus(1);
        unit1.setCreateTime(LocalDateTime.now().minusDays(5));
        unit1.setUpdateTime(LocalDateTime.now().minusDays(5));
        unitStorage.put(unit1.getId(), unit1);
        
        Unit unit2 = new Unit();
        unit2.setId(nextId++);
        unit2.setUnitCode("A001-102");
        unit2.setUnitDescription("A栋1层102室");
        unit2.setProjectId(1L);
        unit2.setBuildingId(1L);
        unit2.setUnitStatus("OCCUPIED");
        unit2.setUnitPurpose("办公");
        unit2.setBuildingArea(95.80);
        unit2.setRentArea(90.00);
        unit2.setPropertyArea(92.50);
        unit2.setStatus(1);
        unit2.setCreateTime(LocalDateTime.now().minusDays(3));
        unit2.setUpdateTime(LocalDateTime.now().minusDays(3));
        unitStorage.put(unit2.getId(), unit2);
    }
    
    @GetMapping("/page")
    public Map<String, Object> getUnitPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String unitStatus) {
        
        List<Unit> allUnits = new ArrayList<>(unitStorage.values());
        
        // 过滤条件
        List<Unit> filteredUnits = allUnits.stream()
                .filter(unit -> keyword == null || keyword.isEmpty() || 
                        unit.getUnitCode().contains(keyword) || 
                        (unit.getUnitDescription() != null && unit.getUnitDescription().contains(keyword)))
                .filter(unit -> projectId == null || unit.getProjectId().equals(projectId))
                .filter(unit -> unitStatus == null || unitStatus.isEmpty() || unit.getUnitStatus().equals(unitStatus))
                .filter(unit -> unit.getStatus() == 1)
                .toList();
        
        // 分页
        int total = filteredUnits.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        List<Unit> pageData = filteredUnits.subList(start, end);
        
        // 添加关联信息
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> records = pageData.stream().map(unit -> {
            Map<String, Object> record = new HashMap<>();
            record.put("id", unit.getId());
            record.put("unitCode", unit.getUnitCode());
            record.put("unitDescription", unit.getUnitDescription());
            record.put("projectId", unit.getProjectId());
            record.put("projectName", getProjectName(unit.getProjectId()));
            record.put("buildingId", unit.getBuildingId());
            record.put("buildingName", getBuildingName(unit.getBuildingId()));
            record.put("unitStatus", unit.getUnitStatus());
            record.put("unitPurpose", unit.getUnitPurpose());
            record.put("buildingArea", unit.getBuildingArea());
            record.put("rentArea", unit.getRentArea());
            record.put("propertyArea", unit.getPropertyArea());
            record.put("status", unit.getStatus());
            record.put("createTime", unit.getCreateTime().format(formatter));
            record.put("updateTime", unit.getUpdateTime().format(formatter));
            return record;
        }).toList();
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", records);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", pageResult);
        
        return response;
    }
    
    @PostMapping
    public Map<String, Object> createUnit(@RequestBody Unit unit) {
        unit.setId(nextId++);
        unit.setStatus(1);
        unit.setCreateTime(LocalDateTime.now());
        unit.setUpdateTime(LocalDateTime.now());
        
        unitStorage.put(unit.getId(), unit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", unit);
        
        return response;
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateUnit(@PathVariable Long id, @RequestBody Unit unit) {
        Unit existingUnit = unitStorage.get(id);
        if (existingUnit == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "单元不存在");
            return response;
        }
        
        // 更新字段
        existingUnit.setUnitCode(unit.getUnitCode());
        existingUnit.setUnitDescription(unit.getUnitDescription());
        existingUnit.setProjectId(unit.getProjectId());
        existingUnit.setBuildingId(unit.getBuildingId());
        existingUnit.setUnitStatus(unit.getUnitStatus());
        existingUnit.setUnitPurpose(unit.getUnitPurpose());
        existingUnit.setBuildingArea(unit.getBuildingArea());
        existingUnit.setRentArea(unit.getRentArea());
        existingUnit.setPropertyArea(unit.getPropertyArea());
        existingUnit.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", existingUnit);
        
        return response;
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUnit(@PathVariable Long id) {
        Unit unit = unitStorage.get(id);
        if (unit == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "单元不存在");
            return response;
        }
        
        // 软删除
        unit.setStatus(0);
        unit.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        
        return response;
    }
    
    @GetMapping("/list")
    public Map<String, Object> getUnitList(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) String unitStatus) {
        
        List<Unit> allUnits = new ArrayList<>(unitStorage.values());
        
        // 过滤条件
        List<Unit> filteredUnits = allUnits.stream()
                .filter(unit -> projectId == null || unit.getProjectId().equals(projectId))
                .filter(unit -> buildingId == null || unit.getBuildingId().equals(buildingId))
                .filter(unit -> unitStatus == null || unitStatus.isEmpty() || unit.getUnitStatus().equals(unitStatus))
                .filter(unit -> unit.getStatus() == 1)
                .toList();
        
        // 转换为简化格式
        List<Map<String, Object>> unitList = filteredUnits.stream().map(unit -> {
            Map<String, Object> unitInfo = new HashMap<>();
            unitInfo.put("id", unit.getId());
            unitInfo.put("unitCode", unit.getUnitCode());
            unitInfo.put("unitDescription", unit.getUnitDescription());
            unitInfo.put("unitName", unit.getUnitCode() + " " + (unit.getUnitDescription() != null ? unit.getUnitDescription() : ""));
            unitInfo.put("projectId", unit.getProjectId());
            unitInfo.put("buildingId", unit.getBuildingId());
            unitInfo.put("unitStatus", unit.getUnitStatus());
            unitInfo.put("buildingArea", unit.getBuildingArea());
            unitInfo.put("rentArea", unit.getRentArea());
            unitInfo.put("propertyArea", unit.getPropertyArea());
            return unitInfo;
        }).toList();
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", unitList);
        
        return response;
    }
    
    @PostMapping("/merge")
    public Map<String, Object> mergeUnits(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Long> sourceUnitIds = (List<Long>) request.get("sourceUnitIds");
        String targetUnitCode = (String) request.get("targetUnitCode");
        String targetUnitDescription = (String) request.get("targetUnitDescription");
        String operationReason = (String) request.get("operationReason");
        
        // 验证源单元是否存在
        for (Long unitId : sourceUnitIds) {
            Unit unit = unitStorage.get(unitId);
            if (unit == null || unit.getStatus() == 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 400);
                response.put("message", "单元ID " + unitId + " 不存在或已删除");
                return response;
            }
        }
        
        // 获取第一个单元作为基础信息
        Unit firstUnit = unitStorage.get(sourceUnitIds.get(0));
        
        // 创建合并后的新单元
        Unit mergedUnit = new Unit();
        mergedUnit.setId(nextId++);
        mergedUnit.setUnitCode(targetUnitCode);
        mergedUnit.setUnitDescription(targetUnitDescription);
        mergedUnit.setProjectId(firstUnit.getProjectId());
        mergedUnit.setBuildingId(firstUnit.getBuildingId());
        mergedUnit.setUnitStatus("VACANT"); // 合并后默认为空置
        mergedUnit.setUnitPurpose(firstUnit.getUnitPurpose());
        
        // 计算合并后的面积（所有源单元面积之和）
        double totalBuildingArea = 0;
        double totalRentArea = 0;
        double totalPropertyArea = 0;
        
        for (Long unitId : sourceUnitIds) {
            Unit unit = unitStorage.get(unitId);
            totalBuildingArea += unit.getBuildingArea() != null ? unit.getBuildingArea() : 0;
            totalRentArea += unit.getRentArea() != null ? unit.getRentArea() : 0;
            totalPropertyArea += unit.getPropertyArea() != null ? unit.getPropertyArea() : 0;
        }
        
        mergedUnit.setBuildingArea(totalBuildingArea);
        mergedUnit.setRentArea(totalRentArea);
        mergedUnit.setPropertyArea(totalPropertyArea);
        mergedUnit.setStatus(1);
        mergedUnit.setCreateTime(LocalDateTime.now());
        mergedUnit.setUpdateTime(LocalDateTime.now());
        
        // 保存合并后的单元
        unitStorage.put(mergedUnit.getId(), mergedUnit);
        
        // 删除源单元（软删除）
        for (Long unitId : sourceUnitIds) {
            Unit unit = unitStorage.get(unitId);
            unit.setStatus(0);
            unit.setUpdateTime(LocalDateTime.now());
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "单元合并成功");
        response.put("data", mergedUnit);
        
        return response;
    }
    
    @PostMapping("/split")
    public Map<String, Object> splitUnit(@RequestBody Map<String, Object> request) {
        Long sourceUnitId = Long.valueOf(request.get("sourceUnitId").toString());
        @SuppressWarnings("unchecked")
        List<Map<String, String>> targetUnits = (List<Map<String, String>>) request.get("targetUnits");
        String operationReason = (String) request.get("operationReason");
        
        // 验证源单元是否存在
        Unit sourceUnit = unitStorage.get(sourceUnitId);
        if (sourceUnit == null || sourceUnit.getStatus() == 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 400);
            response.put("message", "源单元不存在或已删除");
            return response;
        }
        
        List<Unit> newUnits = new ArrayList<>();
        
        // 创建拆分后的新单元
        for (Map<String, String> targetUnit : targetUnits) {
            Unit newUnit = new Unit();
            newUnit.setId(nextId++);
            newUnit.setUnitCode(targetUnit.get("unitCode"));
            newUnit.setUnitDescription(targetUnit.get("unitDescription"));
            newUnit.setProjectId(sourceUnit.getProjectId());
            newUnit.setBuildingId(sourceUnit.getBuildingId());
            newUnit.setUnitStatus("VACANT"); // 拆分后默认为空置
            newUnit.setUnitPurpose(sourceUnit.getUnitPurpose());
            
            // 面积平均分配（简化处理）
            int splitCount = targetUnits.size();
            newUnit.setBuildingArea(sourceUnit.getBuildingArea() != null ? sourceUnit.getBuildingArea() / splitCount : null);
            newUnit.setRentArea(sourceUnit.getRentArea() != null ? sourceUnit.getRentArea() / splitCount : null);
            newUnit.setPropertyArea(sourceUnit.getPropertyArea() != null ? sourceUnit.getPropertyArea() / splitCount : null);
            
            newUnit.setStatus(1);
            newUnit.setCreateTime(LocalDateTime.now());
            newUnit.setUpdateTime(LocalDateTime.now());
            
            unitStorage.put(newUnit.getId(), newUnit);
            newUnits.add(newUnit);
        }
        
        // 删除源单元（软删除）
        sourceUnit.setStatus(0);
        sourceUnit.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "单元拆分成功");
        response.put("data", newUnits);
        
        return response;
    }
    
    // 辅助方法：获取项目名称
    private String getProjectName(Long projectId) {
        Map<Long, String> projectNames = Map.of(
            1L, "万达广场",
            2L, "中关村写字楼", 
            3L, "阳光公寓"
        );
        return projectNames.getOrDefault(projectId, "未知项目");
    }
    
    // 辅助方法：获取楼栋名称
    private String getBuildingName(Long buildingId) {
        Map<Long, String> buildingNames = Map.of(
            1L, "A栋",
            2L, "B栋",
            3L, "C栋"
        );
        return buildingNames.getOrDefault(buildingId, "未知楼栋");
    }
    
    // 单元实体类
    public static class Unit {
        private Long id;
        private String unitCode;
        private String unitDescription;
        private Long projectId;
        private Long buildingId;
        private String unitStatus; // VACANT, OCCUPIED, MAINTENANCE, RESERVED
        private String unitPurpose;
        private Double buildingArea;
        private Double rentArea;
        private Double propertyArea;
        private Integer status; // 1-正常 0-删除
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getUnitCode() { return unitCode; }
        public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
        
        public String getUnitDescription() { return unitDescription; }
        public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }
        
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        
        public Long getBuildingId() { return buildingId; }
        public void setBuildingId(Long buildingId) { this.buildingId = buildingId; }
        
        public String getUnitStatus() { return unitStatus; }
        public void setUnitStatus(String unitStatus) { this.unitStatus = unitStatus; }
        
        public String getUnitPurpose() { return unitPurpose; }
        public void setUnitPurpose(String unitPurpose) { this.unitPurpose = unitPurpose; }
        
        public Double getBuildingArea() { return buildingArea; }
        public void setBuildingArea(Double buildingArea) { this.buildingArea = buildingArea; }
        
        public Double getRentArea() { return rentArea; }
        public void setRentArea(Double rentArea) { this.rentArea = rentArea; }
        
        public Double getPropertyArea() { return propertyArea; }
        public void setPropertyArea(Double propertyArea) { this.propertyArea = propertyArea; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        
        public LocalDateTime getUpdateTime() { return updateTime; }
        public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    }
}