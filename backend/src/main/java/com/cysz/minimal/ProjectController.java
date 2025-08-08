package com.cysz.minimal;

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
 * 项目管理控制器 - 使用数据库
 */
@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final RowMapper<Map<String, Object>> PROJECT_ROW_MAPPER = new RowMapper<Map<String, Object>>() {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> project = new HashMap<>();
            project.put("id", rs.getInt("id"));
            project.put("projectName", rs.getString("project_name"));
            project.put("projectType", rs.getString("project_type"));
            project.put("companyName", rs.getString("company_name"));
            project.put("rentBillCompany", rs.getString("rent_bill_company"));
            project.put("propertyBillCompany", rs.getString("property_bill_company"));
            project.put("propertyRightCompany", rs.getString("property_right_company"));
            project.put("buildingArea", rs.getBigDecimal("building_area"));
            project.put("rentArea", rs.getBigDecimal("rent_area"));
            project.put("propertyArea", rs.getBigDecimal("property_area"));
            project.put("city", rs.getString("city"));
            project.put("address", rs.getString("address"));
            project.put("status", rs.getInt("status"));
            project.put("createTime", rs.getTimestamp("create_time"));
            project.put("updateTime", rs.getTimestamp("update_time"));
            return project;
        }
    };
    
    /**
     * 分页查询项目
     */
    @GetMapping("/page")
    public Map<String, Object> getProjectPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectType) {
        
        System.out.println("项目分页查询 - current: " + current + ", size: " + size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 构建查询条件
            StringBuilder whereClause = new StringBuilder(" WHERE status = 1");
            List<Object> params = new ArrayList<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                whereClause.append(" AND project_name LIKE ?");
                params.add("%" + keyword + "%");
            }
            
            if (projectType != null && !projectType.trim().isEmpty()) {
                whereClause.append(" AND project_type = ?");
                params.add(projectType);
            }
            
            // 查询总数
            String countSql = "SELECT COUNT(*) FROM project" + whereClause.toString();
            int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
            
            // 分页查询数据
            int offset = (current - 1) * size;
            String dataSql = "SELECT * FROM project" + whereClause.toString() + 
                           " ORDER BY create_time DESC LIMIT ? OFFSET ?";
            params.add(size);
            params.add(offset);
            
            List<Map<String, Object>> records = jdbcTemplate.query(dataSql, params.toArray(), PROJECT_ROW_MAPPER);
            
            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("records", records);
            pageResult.put("total", total);
            pageResult.put("current", current);
            pageResult.put("size", size);
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", pageResult);
            
        } catch (Exception e) {
            System.err.println("查询项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 获取所有项目列表
     */
    @GetMapping("/list")
    public Map<String, Object> getProjectList() {
        System.out.println("获取项目列表");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "SELECT * FROM project WHERE status = 1 ORDER BY create_time DESC";
            List<Map<String, Object>> projects = jdbcTemplate.query(sql, PROJECT_ROW_MAPPER);
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", projects);
            
        } catch (Exception e) {
            System.err.println("获取项目列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 创建项目
     */
    @PostMapping
    public Map<String, Object> createProject(@RequestBody Map<String, Object> projectData) {
        System.out.println("创建项目: " + projectData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "INSERT INTO project (project_name, project_type, company_name, rent_bill_company, " +
                        "property_bill_company, property_right_company, building_area, rent_area, property_area, " +
                        "city, address, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
            
            jdbcTemplate.update(sql,
                projectData.get("projectName"),
                projectData.get("projectType"),
                projectData.get("companyName"),
                projectData.get("rentBillCompany"),
                projectData.get("propertyBillCompany"),
                projectData.get("propertyRightCompany"),
                projectData.get("buildingArea"),
                projectData.get("rentArea"),
                projectData.get("propertyArea"),
                projectData.get("city"),
                projectData.get("address")
            );
            
            response.put("code", 200);
            response.put("message", "创建成功");
            response.put("data", projectData);
            
            System.out.println("项目创建成功");
            
        } catch (Exception e) {
            System.err.println("创建项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 更新项目
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateProject(@PathVariable Integer id, @RequestBody Map<String, Object> projectData) {
        System.out.println("更新项目 ID: " + id + ", 数据: " + projectData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "UPDATE project SET project_name = ?, project_type = ?, company_name = ?, " +
                        "rent_bill_company = ?, property_bill_company = ?, property_right_company = ?, " +
                        "building_area = ?, rent_area = ?, property_area = ?, city = ?, address = ? " +
                        "WHERE id = ? AND status = 1";
            
            int updated = jdbcTemplate.update(sql,
                projectData.get("projectName"),
                projectData.get("projectType"),
                projectData.get("companyName"),
                projectData.get("rentBillCompany"),
                projectData.get("propertyBillCompany"),
                projectData.get("propertyRightCompany"),
                projectData.get("buildingArea"),
                projectData.get("rentArea"),
                projectData.get("propertyArea"),
                projectData.get("city"),
                projectData.get("address"),
                id
            );
            
            if (updated > 0) {
                response.put("code", 200);
                response.put("message", "更新成功");
                response.put("data", projectData);
                System.out.println("项目更新成功，ID: " + id);
            } else {
                response.put("code", 404);
                response.put("message", "项目不存在");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("更新项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 删除项目
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProject(@PathVariable Integer id) {
        System.out.println("删除项目 ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "UPDATE project SET status = 0 WHERE id = ?";
            int updated = jdbcTemplate.update(sql, id);
            
            if (updated > 0) {
                response.put("code", 200);
                response.put("message", "删除成功");
                response.put("data", null);
                System.out.println("项目删除成功，ID: " + id);
            } else {
                response.put("code", 404);
                response.put("message", "项目不存在");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            System.err.println("删除项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
}