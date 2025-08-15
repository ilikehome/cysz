package com.cysz.minimal.controller;

import com.cysz.minimal.enums.ProjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 项目管理控制器
 */
@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 项目分页查询
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
            StringBuilder sql = new StringBuilder("SELECT * FROM project WHERE 1=1");
            List<Object> params = new ArrayList<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (project_name LIKE ? OR address LIKE ?)");
                params.add("%" + keyword + "%");
                params.add("%" + keyword + "%");
            }
            
            if (projectType != null && !projectType.trim().isEmpty()) {
                // 验证项目类型是否有效
                if (!ProjectType.isValidCode(projectType)) {
                    response.put("code", 400);
                    response.put("message", "无效的项目类型: " + projectType);
                    response.put("data", null);
                    return response;
                }
                sql.append(" AND project_type = ?");
                params.add(projectType);
            }
            
            // 获取总数
            String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") t";
            int total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
            
            // 分页查询
            sql.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
            params.add(size);
            params.add((current - 1) * size);
            
            List<Map<String, Object>> records = jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> project = new HashMap<>();
                    project.put("id", rs.getInt("id"));
                    project.put("projectName", rs.getString("project_name"));
                    project.put("projectType", rs.getString("project_type"));
                    project.put("managementOrg", rs.getString("management_org"));
                    project.put("rentBillCompany", rs.getString("rent_bill_company"));
                    project.put("rentBillBankAccount", rs.getString("rent_bill_bank_account"));
                    project.put("city", rs.getString("city"));
                    project.put("address", rs.getString("address"));
                    project.put("projectManager", rs.getString("project_manager"));
                    project.put("contactPhone", rs.getString("contact_phone"));
                    project.put("status", rs.getInt("status"));
                    project.put("createTime", rs.getTimestamp("create_time"));
                    project.put("updateTime", rs.getTimestamp("update_time"));
                    return project;
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
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "SELECT id, project_name FROM project WHERE status = 1 ORDER BY project_name";
            List<Map<String, Object>> projects = jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> project = new HashMap<>();
                    project.put("id", rs.getInt("id"));
                    project.put("projectName", rs.getString("project_name"));
                    return project;
                }
            });
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", projects);
            
        } catch (Exception e) {
            System.err.println("获取项目列表失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
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
            // 验证项目类型
            String projectType = (String) projectData.get("projectType");
            if (projectType == null || !ProjectType.isValidCode(projectType)) {
                response.put("code", 400);
                response.put("message", "无效的项目类型: " + projectType);
                return response;
            }
            
            String sql = "INSERT INTO project (project_name, project_type, management_org, rent_bill_company, " +
                        "rent_bill_bank_account, city, address, project_manager, contact_phone, status, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                projectData.get("projectName"),
                projectData.get("projectType"),
                projectData.get("managementOrg"),
                projectData.get("rentBillCompany"),
                projectData.get("rentBillBankAccount"),
                projectData.get("city"),
                projectData.get("address"),
                projectData.get("projectManager"),
                projectData.get("contactPhone"),
                projectData.getOrDefault("status", 1),
                LocalDateTime.now()
            );
            
            response.put("code", 200);
            response.put("message", "创建成功");
            
            System.out.println("项目创建成功");
            
        } catch (Exception e) {
            System.err.println("创建项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
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
            // 验证项目类型
            String projectType = (String) projectData.get("projectType");
            if (projectType == null || !ProjectType.isValidCode(projectType)) {
                response.put("code", 400);
                response.put("message", "无效的项目类型: " + projectType);
                return response;
            }
            
            String sql = "UPDATE project SET project_name = ?, project_type = ?, management_org = ?, " +
                        "rent_bill_company = ?, rent_bill_bank_account = ?, city = ?, address = ?, " +
                        "project_manager = ?, contact_phone = ?, status = ?, update_time = ? WHERE id = ?";
            
            jdbcTemplate.update(sql,
                projectData.get("projectName"),
                projectData.get("projectType"),
                projectData.get("managementOrg"),
                projectData.get("rentBillCompany"),
                projectData.get("rentBillBankAccount"),
                projectData.get("city"),
                projectData.get("address"),
                projectData.get("projectManager"),
                projectData.get("contactPhone"),
                projectData.get("status"),
                LocalDateTime.now(),
                id
            );
            
            response.put("code", 200);
            response.put("message", "更新成功");
            
        } catch (Exception e) {
            System.err.println("更新项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
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
            // 检查项目下是否有楼栋
            String checkSql = "SELECT COUNT(*) FROM building WHERE project_id = ?";
            Integer buildingCount = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);
            
            if (buildingCount != null && buildingCount > 0) {
                response.put("code", 400);
                response.put("message", "该项目下存在楼栋，无法删除");
                return response;
            }
            
            String sql = "DELETE FROM project WHERE id = ?";
            jdbcTemplate.update(sql, id);
            
            response.put("code", 200);
            response.put("message", "删除成功");
            
        } catch (Exception e) {
            System.err.println("删除项目失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return response;
    }
}