package com.cysz.minimal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 楼栋管理控制器 - 使用数据库
 */
@RestController
@RequestMapping("/building")
@CrossOrigin
public class BuildingController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final RowMapper<Map<String, Object>> BUILDING_ROW_MAPPER = new RowMapper<Map<String, Object>>() {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> building = new HashMap<>();
            building.put("id", rs.getInt("id"));
            building.put("buildingName", rs.getString("building_name"));
            building.put("buildingCode", rs.getString("building_code"));
            building.put("projectId", rs.getInt("project_id"));
            building.put("buildingType", rs.getString("building_type"));
            building.put("floorCount", rs.getInt("floor_count"));
            building.put("buildingArea", rs.getBigDecimal("building_area"));
            building.put("rentArea", rs.getBigDecimal("rent_area"));
            building.put("propertyArea", rs.getBigDecimal("property_area"));
            building.put("buildingStatus", rs.getString("building_status"));
            building.put("description", rs.getString("description"));
            building.put("status", rs.getInt("status"));
            building.put("createTime", rs.getTimestamp("create_time"));
            building.put("updateTime", rs.getTimestamp("update_time"));
            return building;
        }
    };
    
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
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 构建查询条件
            StringBuilder whereClause = new StringBuilder(" WHERE b.status = 1");
            List<Object> params = new ArrayList<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                whereClause.append(" AND (b.building_name LIKE ? OR b.building_code LIKE ?)");
                params.add("%" + keyword + "%");
                params.add("%" + keyword + "%");
            }
            
            if (projectId != null) {
                whereClause.append(" AND b.project_id = ?");
                params.add(projectId);
            }
            
            // 查询总数
            String countSql = "SELECT COUNT(*) FROM building b" + whereClause.toString();
            int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
            
            // 分页查询数据，关联项目表获取项目名称
            int offset = (current - 1) * size;
            String dataSql = "SELECT b.*, p.project_name FROM building b " +
                           "LEFT JOIN project p ON b.project_id = p.id" + 
                           whereClause.toString() + 
                           " ORDER BY b.create_time DESC LIMIT ? OFFSET ?";
            params.add(size);
            params.add(offset);
            
            List<Map<String, Object>> records = jdbcTemplate.query(dataSql, params.toArray(), 
                new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> building = new HashMap<>();
                        building.put("id", rs.getInt("id"));
                        building.put("buildingName", rs.getString("building_name"));
                        building.put("buildingCode", rs.getString("building_code"));
                        building.put("projectId", rs.getInt("project_id"));
                        building.put("projectName", rs.getString("project_name"));
                        building.put("buildingType", rs.getString("building_type"));
                        building.put("floorCount", rs.getInt("floor_count"));
                        building.put("buildingArea", rs.getBigDecimal("building_area"));
                        building.put("rentArea", rs.getBigDecimal("rent_area"));
                        building.put("propertyArea", rs.getBigDecimal("property_area"));
                        building.put("buildingStatus", rs.getString("building_status"));
                        building.put("description", rs.getString("description"));
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
            String sql = "SELECT b.*, p.project_name FROM building b " +
                        "LEFT JOIN project p ON b.project_id = p.id " +
                        "WHERE b.project_id = ? AND b.status = 1 " +
                        "ORDER BY b.create_time DESC";
            
            List<Map<String, Object>> buildings = jdbcTemplate.query(sql, new Object[]{projectId}, 
                new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> building = new HashMap<>();
                        building.put("id", rs.getInt("id"));
                        building.put("buildingName", rs.getString("building_name"));
                        building.put("buildingCode", rs.getString("building_code"));
                        building.put("projectId", rs.getInt("project_id"));
                        building.put("projectName", rs.getString("project_name"));
                        building.put("buildingType", rs.getString("building_type"));
                        building.put("floorCount", rs.getInt("floor_count"));
                        building.put("buildingArea", rs.getBigDecimal("building_area"));
                        building.put("rentArea", rs.getBigDecimal("rent_area"));
                        building.put("propertyArea", rs.getBigDecimal("property_area"));
                        building.put("buildingStatus", rs.getString("building_status"));
                        building.put("description", rs.getString("description"));
                        building.put("status", rs.getInt("status"));
                        building.put("createTime", rs.getTimestamp("create_time"));
                        building.put("updateTime", rs.getTimestamp("update_time"));
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
            response.put("data", null);
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
            // 检查楼栋编码是否重复
            String buildingCode = (String) buildingData.get("buildingCode");
            String checkSql = "SELECT COUNT(*) FROM building WHERE building_code = ?";
            int count = jdbcTemplate.queryForObject(checkSql, new Object[]{buildingCode}, Integer.class);
            
            if (count > 0) {
                response.put("code", 400);
                response.put("message", "楼栋编码已存在");
                response.put("data", null);
                return response;
            }
            
            // 创建新楼栋
            String sql = "INSERT INTO building (building_code, building_name, project_id, building_type, " +
                        "floor_count, building_area, rent_area, property_area, building_status, description, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                buildingData.get("buildingCode"),
                buildingData.get("buildingName"),
                buildingData.get("projectId"),
                buildingData.get("buildingType"),
                buildingData.get("floorCount"),
                buildingData.get("buildingArea"),
                buildingData.get("rentArea"),
                buildingData.get("propertyArea"),
                buildingData.get("buildingStatus") != null ? buildingData.get("buildingStatus") : "NORMAL",
                buildingData.get("description"),
                1
            );
            
            response.put("code", 200);
            response.put("message", "创建成功");
            response.put("data", buildingData);
            
            System.out.println("楼栋创建成功");
            
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
            // 检查楼栋是否存在
            String checkSql = "SELECT COUNT(*) FROM building WHERE id = ?";
            int count = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);
            if (count == 0) {
                response.put("code", 404);
                response.put("message", "楼栋不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查楼栋编码是否重复（排除自己）
            String buildingCode = (String) buildingData.get("buildingCode");
            String codeCheckSql = "SELECT COUNT(*) FROM building WHERE building_code = ? AND id != ?";
            int codeCount = jdbcTemplate.queryForObject(codeCheckSql, new Object[]{buildingCode, id}, Integer.class);
            
            if (codeCount > 0) {
                response.put("code", 400);
                response.put("message", "楼栋编码已存在");
                response.put("data", null);
                return response;
            }
            
            // 更新楼栋信息
            String sql = "UPDATE building SET building_code = ?, building_name = ?, project_id = ?, " +
                        "building_type = ?, floor_count = ?, building_area = ?, rent_area = ?, " +
                        "property_area = ?, building_status = ?, description = ? WHERE id = ?";
            
            int updated = jdbcTemplate.update(sql,
                buildingData.get("buildingCode"),
                buildingData.get("buildingName"),
                buildingData.get("projectId"),
                buildingData.get("buildingType"),
                buildingData.get("floorCount"),
                buildingData.get("buildingArea"),
                buildingData.get("rentArea"),
                buildingData.get("propertyArea"),
                buildingData.get("buildingStatus"),
                buildingData.get("description"),
                id
            );
            
            if (updated > 0) {
                response.put("code", 200);
                response.put("message", "更新成功");
                response.put("data", buildingData);
                System.out.println("楼栋更新成功，ID: " + id);
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
            String sql = "UPDATE building SET status = 0 WHERE id = ?";
            int updated = jdbcTemplate.update(sql, id);
            
            if (updated > 0) {
                response.put("code", 200);
                response.put("message", "删除成功");
                response.put("data", null);
                System.out.println("楼栋删除成功，ID: " + id);
            } else {
                response.put("code", 404);
                response.put("message", "楼栋不存在");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("删除楼栋失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
}