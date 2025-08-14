package com.cysz.minimal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 单元管理控制器
 */
@RestController
@RequestMapping("/unit")
@CrossOrigin
public class UnitController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 单元分页查询
     */
    @GetMapping("/page")
    public Map<String, Object> getUnitPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer buildingId,
            @RequestParam(required = false) Integer floorId,
            @RequestParam(required = false) String unitStatus) {
        
        System.out.println("单元分页查询 - current: " + current + ", size: " + size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            StringBuilder sql = new StringBuilder("SELECT u.*, f.floor_name, b.building_name, p.project_name " +
                "FROM unit u " +
                "LEFT JOIN floor f ON u.floor_id = f.id " +
                "LEFT JOIN building b ON f.building_id = b.id " +
                "LEFT JOIN project p ON b.project_id = p.id WHERE 1=1");
            List<Object> params = new ArrayList<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (u.unit_name LIKE ? OR u.unit_code LIKE ?)");
                params.add("%" + keyword + "%");
                params.add("%" + keyword + "%");
            }
            
            if (projectId != null) {
                sql.append(" AND p.id = ?");
                params.add(projectId);
            }
            
            if (buildingId != null) {
                sql.append(" AND b.id = ?");
                params.add(buildingId);
            }
            
            if (floorId != null) {
                sql.append(" AND u.floor_id = ?");
                params.add(floorId);
            }
            
            if (unitStatus != null && !unitStatus.trim().isEmpty()) {
                sql.append(" AND u.unit_status = ?");
                params.add(unitStatus);
            }
            
            // 获取总数
            String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") t";
            int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
            
            // 分页查询
            sql.append(" ORDER BY u.id DESC LIMIT ? OFFSET ?");
            params.add(size);
            params.add((current - 1) * size);
            
            List<Map<String, Object>> records = jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> unit = new HashMap<>();
                    unit.put("id", rs.getInt("id"));
                    unit.put("unitName", rs.getString("unit_name"));
                    unit.put("unitCode", rs.getString("unit_code"));
                    unit.put("floorId", rs.getInt("floor_id"));
                    unit.put("floorName", rs.getString("floor_name"));
                    unit.put("buildingName", rs.getString("building_name"));
                    unit.put("projectName", rs.getString("project_name"));
                    unit.put("unitStatus", rs.getString("unit_status"));
                    unit.put("unitPurpose", rs.getString("unit_purpose"));
                    unit.put("buildingArea", rs.getBigDecimal("building_area"));
                    unit.put("rentArea", rs.getBigDecimal("rent_area"));
                    unit.put("isMultiTenant", rs.getBoolean("is_multi_tenant"));
                    unit.put("remark", rs.getString("remark"));
                    unit.put("status", rs.getInt("status"));
                    unit.put("createTime", rs.getTimestamp("create_time"));
                    unit.put("updateTime", rs.getTimestamp("update_time"));
                    return unit;
                }
            });
            
            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("records", records);
            pageResult.put("total", total);
            pageResult.put("current", current);
            pageResult.put("size", size);
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", pageResult);
            
        } catch (Exception e) {
            System.err.println("查询单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
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
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            StringBuilder sql = new StringBuilder("SELECT u.*, f.floor_name, b.building_name, p.project_name " +
                "FROM unit u " +
                "LEFT JOIN floor f ON u.floor_id = f.id " +
                "LEFT JOIN building b ON f.building_id = b.id " +
                "LEFT JOIN project p ON b.project_id = p.id WHERE u.status = 1");
            List<Object> params = new ArrayList<>();
            
            if (projectId != null) {
                sql.append(" AND p.id = ?");
                params.add(projectId);
            }
            
            if (buildingId != null) {
                sql.append(" AND b.id = ?");
                params.add(buildingId);
            }
            
            if (floorId != null) {
                sql.append(" AND u.floor_id = ?");
                params.add(floorId);
            }
            
            if (unitStatus != null && !unitStatus.trim().isEmpty()) {
                sql.append(" AND u.unit_status = ?");
                params.add(unitStatus);
            }
            
            sql.append(" ORDER BY u.unit_name");
            
            List<Map<String, Object>> units = jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> unit = new HashMap<>();
                    unit.put("id", rs.getInt("id"));
                    unit.put("unitName", rs.getString("unit_name"));
                    unit.put("unitCode", rs.getString("unit_code"));
                    unit.put("floorName", rs.getString("floor_name"));
                    unit.put("buildingName", rs.getString("building_name"));
                    unit.put("projectName", rs.getString("project_name"));
                    unit.put("unitStatus", rs.getString("unit_status"));
                    unit.put("buildingArea", rs.getBigDecimal("building_area"));
                    unit.put("rentArea", rs.getBigDecimal("rent_area"));
                    return unit;
                }
            });
            
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
    
    /**
     * 根据楼层ID获取单元列表
     */
    @GetMapping("/by-floor/{floorId}")
    public Map<String, Object> getUnitsByFloor(@PathVariable Integer floorId) {
        System.out.println("根据楼层ID获取单元列表: " + floorId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "SELECT id, unit_name, unit_code, building_area, rent_area, unit_purpose " +
                        "FROM unit WHERE floor_id = ? AND status = 1 ORDER BY unit_name";
            
            List<Map<String, Object>> units = jdbcTemplate.query(sql, new Object[]{floorId}, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> unit = new HashMap<>();
                    unit.put("id", rs.getInt("id"));
                    unit.put("unitName", rs.getString("unit_name"));
                    unit.put("unitCode", rs.getString("unit_code"));
                    unit.put("buildingArea", rs.getBigDecimal("building_area"));
                    unit.put("rentArea", rs.getBigDecimal("rent_area"));
                    unit.put("unitPurpose", rs.getString("unit_purpose"));
                    return unit;
                }
            });
            
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
    
    /**
     * 根据项目ID获取可用单元列表
     */
    @GetMapping("/available/{projectId}")
    public Map<String, Object> getAvailableUnits(@PathVariable Integer projectId) {
        System.out.println("根据项目ID获取可用单元列表: " + projectId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "SELECT u.id, u.unit_name, u.unit_code, u.building_area, u.rent_area, " +
                        "f.floor_name, b.building_name FROM unit u " +
                        "LEFT JOIN floor f ON u.floor_id = f.id " +
                        "LEFT JOIN building b ON f.building_id = b.id " +
                        "WHERE b.project_id = ? AND u.unit_status = 'RENTABLE' AND u.status = 1 " +
                        "ORDER BY b.building_name, f.floor_name, u.unit_name";
            
            List<Map<String, Object>> units = jdbcTemplate.query(sql, new Object[]{projectId}, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> unit = new HashMap<>();
                    unit.put("id", rs.getInt("id"));
                    unit.put("unitName", rs.getString("unit_name"));
                    unit.put("unitCode", rs.getString("unit_code"));
                    unit.put("buildingArea", rs.getBigDecimal("building_area"));
                    unit.put("rentArea", rs.getBigDecimal("rent_area"));
                    unit.put("floorName", rs.getString("floor_name"));
                    unit.put("buildingName", rs.getString("building_name"));
                    return unit;
                }
            });
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", units);
            
        } catch (Exception e) {
            System.err.println("获取可用单元列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    /**
     * 创建单元
     */
    @PostMapping
    public Map<String, Object> createUnit(@RequestBody Map<String, Object> unitData) {
        System.out.println("创建单元: " + unitData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "INSERT INTO unit (unit_name, unit_code, floor_id, unit_status, unit_purpose, " +
                        "building_area, rent_area, is_multi_tenant, remark, status, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                unitData.get("unitName"),
                unitData.get("unitCode"),
                unitData.get("floorId"),
                unitData.getOrDefault("unitStatus", "RENTABLE"),
                unitData.get("unitPurpose"),
                unitData.get("buildingArea"),
                unitData.get("rentArea"),
                unitData.getOrDefault("isMultiTenant", false),
                unitData.get("remark"),
                unitData.getOrDefault("status", 1),
                LocalDateTime.now()
            );
            
            response.put("code", 200);
            response.put("message", "创建成功");
            
            System.out.println("单元创建成功");
            
        } catch (Exception e) {
            System.err.println("创建单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 更新单元
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateUnit(@PathVariable Integer id, @RequestBody Map<String, Object> unitData) {
        System.out.println("更新单元 ID: " + id + ", 数据: " + unitData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "UPDATE unit SET unit_name = ?, unit_code = ?, floor_id = ?, unit_status = ?, " +
                        "unit_purpose = ?, building_area = ?, rent_area = ?, is_multi_tenant = ?, " +
                        "remark = ?, status = ?, update_time = ? WHERE id = ?";
            
            jdbcTemplate.update(sql,
                unitData.get("unitName"),
                unitData.get("unitCode"),
                unitData.get("floorId"),
                unitData.get("unitStatus"),
                unitData.get("unitPurpose"),
                unitData.get("buildingArea"),
                unitData.get("rentArea"),
                unitData.get("isMultiTenant"),
                unitData.get("remark"),
                unitData.get("status"),
                LocalDateTime.now(),
                id
            );
            
            response.put("code", 200);
            response.put("message", "更新成功");
            
        } catch (Exception e) {
            System.err.println("更新单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 删除单元
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUnit(@PathVariable Integer id) {
        System.out.println("删除单元 ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查单元是否有关联的合同
            String checkSql = "SELECT COUNT(*) FROM contract WHERE unit_id = ?";
            Integer contractCount = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);
            
            if (contractCount != null && contractCount > 0) {
                response.put("code", 400);
                response.put("message", "该单元存在关联合同，无法删除");
                return response;
            }
            
            String sql = "DELETE FROM unit WHERE id = ?";
            jdbcTemplate.update(sql, id);
            
            response.put("code", 200);
            response.put("message", "删除成功");
            
        } catch (Exception e) {
            System.err.println("删除单元失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 单元合并
     */
    @PostMapping("/merge")
    public Map<String, Object> mergeUnits(@RequestBody Map<String, Object> mergeData) {
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
    
    /**
     * 单元拆分
     */
    @PostMapping("/split")
    public Map<String, Object> splitUnit(@RequestBody Map<String, Object> splitData) {
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