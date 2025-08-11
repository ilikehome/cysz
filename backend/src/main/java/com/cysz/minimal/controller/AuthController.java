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
        
        if (token != null && token.startsWith("Bearer ")) {
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
            data.put("permissions", Arrays.asList("ALL"));
            
            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", data);
            
            System.out.println("返回用户信息: " + response); // 添加调试日志
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
        // 简单的验证逻辑
        return "123456".equals(password) && 
               (("ORG001".equals(orgCode) && "admin".equals(username)) ||
                ("ORG002".equals(orgCode) && "poly_admin".equals(username)) ||
                ("ORG003".equals(orgCode) && "greenland_admin".equals(username)) ||
                ("ORG004".equals(orgCode) && "country_admin".equals(username)) ||
                ("ORG005".equals(orgCode) && "crland_admin".equals(username)));
    }
    
    private String getRealName(String orgCode, String username) {
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("ORG001-admin", "王健林");
        nameMap.put("ORG002-poly_admin", "宋广菊");
        nameMap.put("ORG003-greenland_admin", "张玉良");
        nameMap.put("ORG004-country_admin", "杨惠妍");
        nameMap.put("ORG005-crland_admin", "李欣");
        return nameMap.getOrDefault(orgCode + "-" + username, username);
    }
    
    private String getEmail(String orgCode, String username) {
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("ORG001-admin", "wjl@wanda.com");
        emailMap.put("ORG002-poly_admin", "sgj@polycn.com");
        emailMap.put("ORG003-greenland_admin", "zyl@greenland.com");
        emailMap.put("ORG004-country_admin", "yhy@countrygarden.com.cn");
        emailMap.put("ORG005-crland_admin", "lixin@crc.com.hk");
        return emailMap.getOrDefault(orgCode + "-" + username, username + "@example.com");
    }
    
    private String getRole(String orgCode, String username) {
        if (username.contains("admin")) {
            return "TENANT_ADMIN";
        }
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
        if (username.contains("admin")) {
            return Arrays.asList("ALL");
        }
        return Arrays.asList("dashboard:view", "asset:view");
    }
}