package com.cysz.property.controller;

import com.cysz.property.common.Result;
import com.cysz.property.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证相关Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "认证管理", description = "认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录接口")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        String orgCode = loginRequest.get("orgCode");
        
        // 参数验证
        if (!StringUtils.hasText(username)) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            return Result.error("密码不能为空");
        }
        if (!StringUtils.hasText(orgCode)) {
            return Result.error("组织代码不能为空");
        }
        
        try {
            // 调用UserService进行登录验证
            Map<String, Object> loginResult = userService.login(username, password, "");
            
            // 添加组织代码到结果中
            loginResult.put("orgCode", orgCode);
            
            return Result.success(loginResult);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "用户注册接口")
    @PostMapping("/register")
    public Result<String> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String email = registerRequest.get("email");
        String phone = registerRequest.get("phone");
        
        // 参数验证
        if (!StringUtils.hasText(username)) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            return Result.error("密码不能为空");
        }
        
        // 简单的注册逻辑（实际项目中应该保存到数据库）
        return Result.success("注册成功");
    }

    /**
     * 获取组织列表
     */
    @Operation(summary = "获取组织列表", description = "获取系统中的组织列表")
    @GetMapping("/organizations")
    public Result<List<Map<String, Object>>> getOrganizations() {
        // 返回模拟的组织数据
        List<Map<String, Object>> organizations = new ArrayList<>();
        
        Map<String, Object> org1 = new HashMap<>();
        org1.put("orgCode", "CYSZ001");
        org1.put("orgName", "创业时代物业管理有限公司");
        org1.put("orgType", "PROPERTY_MANAGEMENT");
        organizations.add(org1);
        
        Map<String, Object> org2 = new HashMap<>();
        org2.put("orgCode", "CYSZ002");
        org2.put("orgName", "创业时代商业管理有限公司");
        org2.put("orgType", "COMMERCIAL_MANAGEMENT");
        organizations.add(org2);
        
        return Result.success(organizations);
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/user-info")
    public Result<Map<String, Object>> getUserInfo() {
        // 返回模拟的用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1L);
        userInfo.put("username", "admin");
        userInfo.put("realName", "系统管理员");
        userInfo.put("email", "admin@cysz.com");
        userInfo.put("phone", "13800138000");
        userInfo.put("role", "ADMIN");
        userInfo.put("status", 1);
        
        // 组织信息
        Map<String, Object> orgInfo = new HashMap<>();
        orgInfo.put("orgCode", "CYSZ001");
        orgInfo.put("orgName", "创业时代物业管理有限公司");
        orgInfo.put("orgType", "PROPERTY_MANAGEMENT");
        userInfo.put("orgInfo", orgInfo);
        
        // 权限列表
        List<String> permissions = new ArrayList<>();
        permissions.add("user:view");
        permissions.add("user:create");
        permissions.add("user:update");
        permissions.add("user:delete");
        permissions.add("project:view");
        permissions.add("project:create");
        permissions.add("project:update");
        permissions.add("project:delete");
        permissions.add("building:view");
        permissions.add("building:create");
        permissions.add("building:update");
        permissions.add("building:delete");
        permissions.add("contract:view");
        permissions.add("contract:create");
        permissions.add("contract:update");
        permissions.add("contract:delete");
        permissions.add("tenant:view");
        permissions.add("tenant:create");
        permissions.add("tenant:update");
        permissions.add("tenant:delete");
        permissions.add("account:view");
        permissions.add("account:create");
        permissions.add("account:update");
        permissions.add("account:delete");
        
        Map<String, Object> result = new HashMap<>();
        result.put("userInfo", userInfo);
        result.put("permissions", permissions);
        
        return Result.success(result);
    }
}