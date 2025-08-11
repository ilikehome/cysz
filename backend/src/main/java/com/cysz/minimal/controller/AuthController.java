package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String orgCode = loginRequest.get("orgCode");
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        // 模拟用户验证逻辑
        Map<String, Object> response = new HashMap<>();
        
        if (isValidLogin(orgCode, username, password)) {
            Map<String, Object> data = new HashMap<>();
            
            // 生成模拟token
            data.put("token", "mock-jwt-token-" + System.currentTimeMillis());
            
            // 用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", 1);
            userInfo.put("username", username);
            userInfo.put("realName", getRealName(orgCode, username));
            userInfo.put("email", getEmail(orgCode, username));
            userInfo.put("role", getRole(orgCode, username));
            
            // 组织信息
            Map<String, Object> orgInfo = getOrgInfo(orgCode);
            userInfo.put("orgInfo", orgInfo);
            
            data.put("userInfo", userInfo);
            
            // 权限信息
            List<String> permissions = getPermissions(orgCode, username);
            data.put("permissions", permissions);
            
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", data);
        } else {
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("收到获取用户信息请求，Authorization: " + token); // 添加调试日志
        
        if (token != null && token.startsWith("Bearer ") && token.length() > 7) {
            // 从token中提取实际的token值
            String actualToken = token.substring(7);
            System.out.println("实际token: " + actualToken);
            
            // 简单验证token格式（以mock-jwt-token开头）
            if (actualToken.startsWith("mock-jwt-token-")) {
                Map<String, Object> data = new HashMap<>();
                
                // 模拟用户信息
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", 1);
                userInfo.put("username", "admin");
                userInfo.put("realName", "王健林");
                userInfo.put("email", "wjl@wanda.com");
                userInfo.put("role", "TENANT_ADMIN");
                
                // 组织信息
                Map<String, Object> orgInfo = getOrgInfo("ORG001");
                userInfo.put("orgInfo", orgInfo);
                
                data.put("userInfo", userInfo);
                data.put("permissions", getPermissions("ORG001", "admin"));
                
                response.put("code", 200);
                response.put("message", "获取成功");
                response.put("data", data);
                
                System.out.println("返回用户信息: " + response); // 添加调试日志
            } else {
                response.put("code", 401);
                response.put("message", "无效的token格式");
                System.out.println("无效的token格式: " + actualToken);
            }
        } else {
            response.put("code", 401);
            response.put("message", "未授权，token: " + token);
            System.out.println("未授权访问，token: " + token); // 添加调试日志
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/organizations")
    public ResponseEntity<Map<String, Object>> getOrganizations() {
        Map<String, Object> response = new HashMap<>();
        
        List<Map<String, Object>> organizations = Arrays.asList(
            createOrg("ORG001", "万达集团", "COMPANY"),
            createOrg("ORG002", "保利地产", "COMPANY"),
            createOrg("ORG003", "绿地控股", "COMPANY"),
            createOrg("ORG004", "碧桂园集团", "COMPANY"),
            createOrg("ORG005", "华润置地", "COMPANY")
        );
        
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", organizations);
        
        return ResponseEntity.ok(response);
    }
    
    private boolean isValidLogin(String orgCode, String username, String password) {
        // 验证密码
        if (!"123456".equals(password)) {
            return false;
        }
        
        // 超级管理员验证（系统级）
        if ("SYSTEM".equals(orgCode) && "superadmin".equals(username)) {
            return true;
        }
        
        // 租户管理员和普通用户验证
        return (("ORG001".equals(orgCode) && ("admin".equals(username) || "user1".equals(username))) ||
                ("ORG002".equals(orgCode) && ("poly_admin".equals(username) || "poly_user".equals(username))) ||
                ("ORG003".equals(orgCode) && ("greenland_admin".equals(username) || "green_user".equals(username))) ||
                ("ORG004".equals(orgCode) && ("country_admin".equals(username) || "country_user".equals(username))) ||
                ("ORG005".equals(orgCode) && ("crland_admin".equals(username) || "cr_user".equals(username))));
    }
    
    private String getRealName(String orgCode, String username) {
        Map<String, String> nameMap = new HashMap<>();
        
        // 超级管理员
        nameMap.put("SYSTEM-superadmin", "系统管理员");
        
        // 租户管理员
        nameMap.put("ORG001-admin", "王健林");
        nameMap.put("ORG002-poly_admin", "宋广菊");
        nameMap.put("ORG003-greenland_admin", "张玉良");
        nameMap.put("ORG004-country_admin", "杨惠妍");
        nameMap.put("ORG005-crland_admin", "李欣");
        
        // 普通用户
        nameMap.put("ORG001-user1", "张三");
        nameMap.put("ORG002-poly_user", "李四");
        nameMap.put("ORG003-green_user", "王五");
        nameMap.put("ORG004-country_user", "赵六");
        nameMap.put("ORG005-cr_user", "钱七");
        
        return nameMap.getOrDefault(orgCode + "-" + username, username);
    }
    
    private String getEmail(String orgCode, String username) {
        Map<String, String> emailMap = new HashMap<>();
        
        // 超级管理员
        emailMap.put("SYSTEM-superadmin", "superadmin@system.com");
        
        // 租户管理员
        emailMap.put("ORG001-admin", "wjl@wanda.com");
        emailMap.put("ORG002-poly_admin", "sgj@polycn.com");
        emailMap.put("ORG003-greenland_admin", "zyl@greenland.com");
        emailMap.put("ORG004-country_admin", "yhy@countrygarden.com.cn");
        emailMap.put("ORG005-crland_admin", "lixin@crc.com.hk");
        
        // 普通用户
        emailMap.put("ORG001-user1", "zhangsan@wanda.com");
        emailMap.put("ORG002-poly_user", "lisi@polycn.com");
        emailMap.put("ORG003-green_user", "wangwu@greenland.com");
        emailMap.put("ORG004-country_user", "zhaoliu@countrygarden.com.cn");
        emailMap.put("ORG005-cr_user", "qianqi@crc.com.hk");
        
        return emailMap.getOrDefault(orgCode + "-" + username, username + "@example.com");
    }
    
    private String getRole(String orgCode, String username) {
        // 超级管理员（系统级角色）
        if ("SYSTEM".equals(orgCode) && "superadmin".equals(username)) {
            return "SUPER_ADMIN";
        }
        
        // 租户管理员
        if (username.contains("admin")) {
            return "TENANT_ADMIN";
        }
        
        // 普通用户
        return "USER";
    }
    
    private Map<String, Object> getOrgInfo(String orgCode) {
        Map<String, Map<String, Object>> orgMap = new HashMap<>();
        orgMap.put("ORG001", createOrg("ORG001", "万达集团", "COMPANY"));
        orgMap.put("ORG002", createOrg("ORG002", "保利地产", "COMPANY"));
        orgMap.put("ORG003", createOrg("ORG003", "绿地控股", "COMPANY"));
        orgMap.put("ORG004", createOrg("ORG004", "碧桂园集团", "COMPANY"));
        orgMap.put("ORG005", createOrg("ORG005", "华润置地", "COMPANY"));
        
        return orgMap.getOrDefault(orgCode, createOrg("ORG001", "万达集团", "COMPANY"));
    }
    
    private Map<String, Object> createOrg(String code, String name, String type) {
        Map<String, Object> org = new HashMap<>();
        org.put("orgCode", code);
        org.put("orgName", name);
        org.put("orgType", type);
        return org;
    }
    
    private List<String> getPermissions(String orgCode, String username) {
        String role = getRole(orgCode, username);
        
        switch (role) {
            case "SUPER_ADMIN":
                // 超级管理员：系统所有权限
                return Arrays.asList(
                    "system:manage",           // 系统管理
                    "tenant:create",           // 创建租户
                    "tenant:delete",           // 删除租户
                    "tenant:manage",           // 管理所有租户
                    "user:manage:all",         // 管理所有用户
                    "role:manage",             // 角色管理
                    "system:monitor",          // 系统监控
                    "data:export:all",         // 导出所有数据
                    "audit:view:all"           // 查看所有审计日志
                );
                
            case "TENANT_ADMIN":
                // 租户管理员：租户内所有权限
                return Arrays.asList(
                    "tenant:view",             // 查看租户信息
                    "user:create",             // 创建用户
                    "user:update",             // 更新用户
                    "user:delete",             // 删除用户
                    "user:manage",             // 用户管理
                    "project:manage",          // 项目管理
                    "building:manage",         // 楼栋管理
                    "unit:manage",             // 单元管理
                    "tenant:business:manage",  // 租户业务管理
                    "contract:manage",         // 合同管理
                    "receivable:manage",       // 应收账款管理
                    "data:export",             // 数据导出
                    "statistics:view",         // 统计查看
                    "audit:view"               // 审计日志查看
                );
                
            case "USER":
                // 普通用户：基础权限
                return Arrays.asList(
                    "dashboard:view",          // 仪表盘查看
                    "profile:view",            // 个人信息查看
                    "profile:update",          // 个人信息更新
                    "project:view",            // 项目查看
                    "building:view",           // 楼栋查看
                    "unit:view",               // 单元查看
                    "contract:view",           // 合同查看
                    "statistics:view:basic"    // 基础统计查看
                );
                
            default:
                return Arrays.asList("dashboard:view");
        }
    }
}