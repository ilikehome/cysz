package com.cysz.minimal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(@RequestBody Map<String, Object> request) {
        try {
            // 这里应该实现真正的用户信息更新逻辑
            // 目前返回成功响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户信息更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户信息更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");
            
            // 这里应该实现真正的密码修改逻辑
            // 1. 验证当前密码是否正确
            // 2. 更新密码
            // 目前返回成功响应
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "密码修改成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "密码修改失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/login-history")
    public ResponseEntity<?> getUserLoginHistory() {
        try {
            // 模拟登录记录数据
            List<Map<String, Object>> loginHistory = new ArrayList<>();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            Map<String, Object> record1 = new HashMap<>();
            record1.put("loginTime", LocalDateTime.now().minusHours(2).format(formatter));
            record1.put("ipAddress", "192.168.1.100");
            record1.put("userAgent", "Chrome 120.0.0.0 Windows");
            record1.put("status", "success");
            loginHistory.add(record1);
            
            Map<String, Object> record2 = new HashMap<>();
            record2.put("loginTime", LocalDateTime.now().minusDays(1).format(formatter));
            record2.put("ipAddress", "192.168.1.100");
            record2.put("userAgent", "Chrome 120.0.0.0 Windows");
            record2.put("status", "success");
            loginHistory.add(record2);
            
            Map<String, Object> record3 = new HashMap<>();
            record3.put("loginTime", LocalDateTime.now().minusDays(2).format(formatter));
            record3.put("ipAddress", "192.168.1.100");
            record3.put("userAgent", "Chrome 120.0.0.0 Windows");
            record3.put("status", "success");
            loginHistory.add(record3);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", loginHistory);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取登录记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}