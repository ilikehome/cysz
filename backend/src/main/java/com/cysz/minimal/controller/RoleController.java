package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "*")
public class RoleController {
    
    /**
     * 获取所有角色列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getRoleList(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("code", 401);
            response.put("message", "未授权");
            return ResponseEntity.ok(response);
        }
        
        List<Map<String, Object>> roles = Arrays.asList(
            createRole("SUPER_ADMIN", "超级管理员", "SYSTEM", "系统最高权限，管理所有租户和系统配置"),
            createRole("TENANT_ADMIN", "租户管理员", "TENANT", "租户内最高权限，管理租户内所有用户和数据"),
            createRole("USER", "普通用户", "TENANT", "基础操作权限，只能访问授权的数据")
        );
        
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", roles);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取角色权限详情
     */
    @GetMapping("/{roleCode}/permissions")
    public ResponseEntity<Map<String, Object>> getRolePermissions(@PathVariable String roleCode, 
                                                                  @RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("code", 401);
            response.put("message", "未授权");
            return ResponseEntity.ok(response);
        }
        
        List<String> permissions = getPermissionsByRole(roleCode);
        
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", permissions);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取用户角色统计
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getRoleStatistics(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("code", 401);
            response.put("message", "未授权");
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("superAdminCount", 1);
        statistics.put("tenantAdminCount", 5);
        statistics.put("userCount", 15);
        statistics.put("totalUsers", 21);
        
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", statistics);
        
        return ResponseEntity.ok(response);
    }
    
    private Map<String, Object> createRole(String code, String name, String level, String description) {
        Map<String, Object> role = new HashMap<>();
        role.put("roleCode", code);
        role.put("roleName", name);
        role.put("roleLevel", level);
        role.put("description", description);
        role.put("userCount", getRoleUserCount(code));
        role.put("createdAt", "2025-01-01 00:00:00");
        return role;
    }
    
    private int getRoleUserCount(String roleCode) {
        switch (roleCode) {
            case "SUPER_ADMIN": return 1;
            case "TENANT_ADMIN": return 5;
            case "USER": return 15;
            default: return 0;
        }
    }
    
    private List<String> getPermissionsByRole(String roleCode) {
        switch (roleCode) {
            case "SUPER_ADMIN":
                return Arrays.asList(
                    "system:manage", "tenant:create", "tenant:delete", "tenant:manage",
                    "user:manage:all", "role:manage", "system:monitor", 
                    "data:export:all", "audit:view:all"
                );
                
            case "TENANT_ADMIN":
                return Arrays.asList(
                    "tenant:view", "user:create", "user:update", "user:delete", "user:manage",
                    "project:manage", "building:manage", "unit:manage", "tenant:business:manage",
                    "contract:manage", "receivable:manage", "data:export", 
                    "statistics:view", "audit:view"
                );
                
            case "USER":
                return Arrays.asList(
                    "dashboard:view", "profile:view", "profile:update", "project:view",
                    "building:view", "unit:view", "contract:view", "statistics:view:basic"
                );
                
            default:
                return Arrays.asList();
        }
    }
}