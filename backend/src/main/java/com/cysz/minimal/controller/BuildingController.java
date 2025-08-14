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
 * 楼栋管理控制器
 */
@RestController
@RequestMapping("/building")
@CrossOrigin
public class BuildingController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 楼栋分页查询
     */
    @GetMapping("/page")
    public Map<String, Object> getBuildingPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer projectId) {
        
        System.out.println("楼栋分页查询 - current: " + current + ", size: " + size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            StringBuilder sql = new StringBuilder("SELECT b.*, p.project_name FROM building b LEFT JOIN project p ON b.project_id = p.id WHERE 1=1");
            List<Object> params = new ArrayList<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (b.building_name LIKE ? OR b.building_code LIKE ?)");
                params.add("%" + keyword + "%");
                params.add("%" + keyword + "%");
            }
            
            if (projectId != null) {
                sql.append(" AND b.project_id = ?");
                params.add(projectId);
            }
            
            // 获取总数
            String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") t";
            int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
            
            // 分页查询
            sql.append(" ORDER BY b.id DESC LIMIT ? OFFSET ?");
            params.add(size);
            params.add((current - 1) * size);
            
            List<Map<String, Object>> records = jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> building = new HashMap<>();
                    building.put("id", rs.getInt("id"));
                    building.put("buildingName", rs.getString("building_name"));
                    building.put("buildingCode", rs.getString("building_code"));
                    building.put("projectId", rs.getInt("project_id"));
                    building.put("projectName", rs.getString("project_name"));
                    building.put("buildingArea", rs.getBigDecimal("building_area"));
                    building.put("rentArea", rs.getBigDecimal("rent_area"));
                    building.put("propertyArea", rs.getBigDecimal("property_area"));
                    building.put("usableArea", rs.getBigDecimal("usable_area"));
                    building.put("remark", rs.getString("remark"));
                    building.put("status", rs.getInt("status"));
                    building.put("createTime", rs.getTimestamp("create_time"));
                    building.put("updateTime", rs.getTimestamp("update_time"));
                    return building;
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
            System.err.println("查询楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 根据项目ID获取楼栋列表
     */
    @GetMapping("/project/{projectId}")
    public Map<String, Object> getBuildingsByProject(@PathVariable Integer projectId) {
        System.out.println("根据项目ID获取楼栋列表: " + projectId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "SELECT id, building_name, building_code FROM building WHERE project_id = ? AND status = 1 ORDER BY building_name";
            List<Map<String, Object>> buildings = jdbcTemplate.query(sql, new Object[]{projectId}, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> building = new HashMap<>();
                    building.put("id", rs.getInt("id"));
                    building.put("buildingName", rs.getString("building_name"));
                    building.put("buildingCode", rs.getString("building_code"));
                    return building;
                }
            });
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", buildings);
            
        } catch (Exception e) {
            System.err.println("获取楼栋列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
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
            String sql = "INSERT INTO building (building_name, building_code, project_id, building_area, " +
                        "rent_area, property_area, usable_area, remark, status, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                buildingData.get("buildingName"),
                buildingData.get("buildingCode"),
                buildingData.get("projectId"),
                buildingData.get("buildingArea"),
                buildingData.get("rentArea"),
                buildingData.get("propertyArea"),
                buildingData.get("usableArea"),
                buildingData.get("remark"),
                buildingData.getOrDefault("status", 1),
                LocalDateTime.now()
            );
            
            response.put("code", 200);
            response.put("message", "创建成功");
            
            System.out.println("楼栋创建成功");
            
        } catch (Exception e) {
            System.err.println("创建楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
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
            String sql = "UPDATE building SET building_name = ?, building_code = ?, project_id = ?, " +
                        "building_area = ?, rent_area = ?, property_area = ?, usable_area = ?, " +
                        "remark = ?, status = ?, update_time = ? WHERE id = ?";
            
            jdbcTemplate.update(sql,
                buildingData.get("buildingName"),
                buildingData.get("buildingCode"),
                buildingData.get("projectId"),
                buildingData.get("buildingArea"),
                buildingData.get("rentArea"),
                buildingData.get("propertyArea"),
                buildingData.get("usableArea"),
                buildingData.get("remark"),
                buildingData.get("status"),
                LocalDateTime.now(),
                id
            );
            
            response.put("code", 200);
            response.put("message", "更新成功");
            
        } catch (Exception e) {
            System.err.println("更新楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
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
            String checkSql = "SELECT COUNT(*) FROM floor WHERE building_id = ?";
            Integer floorCount = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);
            
            if (floorCount != null && floorCount > 0) {
                response.put("code", 400);
                response.put("message", "该楼栋下存在楼层，无法删除");
                return response;
            }
            
            String sql = "DELETE FROM building WHERE id = ?";
            jdbcTemplate.update(sql, id);
            
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