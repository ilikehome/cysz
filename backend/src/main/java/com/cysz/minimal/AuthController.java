package com.cysz.minimal;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 最小化认证控制器
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        Map<String, Object> response = new HashMap<>();
        
        // 简化验证：只验证admin用户
        if (!"admin".equals(username) || !"123456".equals(password)) {
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
            response.put("data", null);
            return response;
        }
        
        // 生成token（简化版）
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        
        // 用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("username", "admin");
        userInfo.put("realName", "系统管理员");
        userInfo.put("email", "admin@cysz.com");
        userInfo.put("phone", "13800138000");
        data.put("userInfo", userInfo);
        
        // 权限信息
        data.put("permissions", new String[]{"admin"});
        
        response.put("code", 200);
        response.put("message", "登录成功");
        response.put("data", data);
        
        return response;
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/user/info")
    public Map<String, Object> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Map<String, Object> response = new HashMap<>();
        
        // 简单的token验证
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.put("code", 401);
            response.put("message", "未授权，请先登录");
            response.put("data", null);
            return response;
        }
        
        String token = authorization.substring(7); // 移除 "Bearer " 前缀
        if (token.isEmpty()) {
            response.put("code", 401);
            response.put("message", "Token无效");
            response.put("data", null);
            return response;
        }
        
        Map<String, Object> data = new HashMap<>();
        
        // 用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("username", "admin");
        userInfo.put("realName", "系统管理员");
        userInfo.put("email", "admin@cysz.com");
        userInfo.put("phone", "13800138000");
        data.put("userInfo", userInfo);
        
        // 权限信息
        data.put("permissions", new String[]{"admin"});
        
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", data);
        
        return response;
    }
}