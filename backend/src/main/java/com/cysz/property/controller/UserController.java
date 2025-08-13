package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.User;
import com.cysz.property.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getUserPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        PageResult<Map<String, Object>> result = userService.getUserPage(
                pageQuery, username, realName, email, phone, 
                departmentId != null ? departmentId.toString() : null, status);
        return Result.success(result);
    }

    @Operation(summary = "获取用户详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUserDetail(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = userService.getUserDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public Result<Boolean> createUser(
            @Parameter(description = "用户信息") @RequestBody @Valid User user) {
        boolean result = userService.createUser(user);
        return Result.success(result);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public Result<Boolean> updateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "用户信息") @RequestBody @Valid User user) {
        user.setId(id);
        boolean result = userService.updateUser(user);
        return Result.success(result);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        boolean result = userService.deleteUser(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping
    public Result<Boolean> batchDeleteUsers(
            @Parameter(description = "用户ID列表") @RequestBody List<Long> userIds) {
        boolean result = userService.batchDeleteUsers(userIds);
        return Result.success(result);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @Parameter(description = "组织代码") @RequestParam(required = false) String orgCode,
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "密码") @RequestParam String password,
            @Parameter(description = "登录IP") @RequestParam(required = false) String loginIp) {
        // 模拟登录逻辑，返回前端期望的数据格式
        Map<String, Object> result = new HashMap<>();
        
        // 模拟token
        String token = "mock-jwt-token-" + System.currentTimeMillis();
        result.put("token", token);
        
        // 模拟用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1L);
        userInfo.put("username", username);
        userInfo.put("realName", "系统管理员");
        userInfo.put("email", "admin@cysz.com");
        userInfo.put("phone", "13800138000");
        userInfo.put("role", "ADMIN");
        userInfo.put("status", 1);
        
        // 组织信息
        Map<String, Object> orgInfo = new HashMap<>();
        orgInfo.put("orgCode", orgCode != null ? orgCode : "CYSZ001");
        orgInfo.put("orgName", "创业时代物业管理有限公司");
        orgInfo.put("orgType", "PROPERTY_MANAGEMENT");
        userInfo.put("orgInfo", orgInfo);
        
        result.put("userInfo", userInfo);
        
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
        
        result.put("permissions", permissions);
        
        return Result.success(result);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Boolean> logout(
            @Parameter(description = "令牌") @RequestParam @NotNull String token) {
        boolean result = userService.logout(token);
        return Result.success(result);
    }

    @Operation(summary = "修改密码")
    @PostMapping("/{id}/change-password")
    public Result<Boolean> changePassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "旧密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        boolean result = userService.changePassword(id, oldPassword, newPassword);
        return Result.success(result);
    }

    @Operation(summary = "重置密码")
    @PostMapping("/{id}/reset-password")
    public Result<Boolean> resetPassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        boolean result = userService.resetPassword(id, newPassword);
        return Result.success(result);
    }

    @Operation(summary = "启用用户")
    @PostMapping("/{id}/enable")
    public Result<Boolean> enableUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        boolean result = userService.enableUser(id);
        return Result.success(result);
    }

    @Operation(summary = "禁用用户")
    @PostMapping("/{id}/disable")
    public Result<Boolean> disableUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        boolean result = userService.disableUser(id);
        return Result.success(result);
    }

    @Operation(summary = "锁定用户")
    @PostMapping("/{id}/lock")
    public Result<Boolean> lockUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        boolean result = userService.lockUser(id);
        return Result.success(result);
    }

    @Operation(summary = "解锁用户")
    @PostMapping("/{id}/unlock")
    public Result<Boolean> unlockUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        boolean result = userService.unlockUser(id);
        return Result.success(result);
    }

    @Operation(summary = "获取用户统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getUserStatistics() {
        Map<String, Object> result = userService.getUserStatistics();
        return Result.success(result);
    }

    @Operation(summary = "根据部门统计用户数量")
    @GetMapping("/statistics/department")
    public Result<List<Map<String, Object>>> getUserCountByDepartment() {
        List<Map<String, Object>> result = userService.getUserCountByDepartment();
        return Result.success(result);
    }

    @Operation(summary = "获取最近登录的用户")
    @GetMapping("/recent-login")
    public Result<List<Map<String, Object>>> getRecentLoginUsers(
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        List<Map<String, Object>> result = userService.getRecentLoginUsers(limit);
        return Result.success(result);
    }

    @Operation(summary = "验证用户名是否存在")
    @GetMapping("/check-username")
    public Result<Boolean> checkUsernameExists(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "排除的用户ID") @RequestParam(required = false) Long excludeId) {
        boolean result = userService.checkUsernameExists(username, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "验证邮箱是否存在")
    @GetMapping("/check-email")
    public Result<Boolean> checkEmailExists(
            @Parameter(description = "邮箱") @RequestParam String email,
            @Parameter(description = "排除的用户ID") @RequestParam(required = false) Long excludeId) {
        boolean result = userService.checkEmailExists(email, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "验证手机号是否存在")
    @GetMapping("/check-phone")
    public Result<Boolean> checkPhoneExists(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "排除的用户ID") @RequestParam(required = false) Long excludeId) {
        boolean result = userService.checkPhoneExists(phone, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "根据用户名查询用户")
    @GetMapping("/by-username")
    public Result<Map<String, Object>> getUserByUsername(
            @Parameter(description = "用户名") @RequestParam String username) {
        User user = userService.getUserByUsername(username);
        // TODO: 转换User对象为Map
        Map<String, Object> result = new HashMap<>();
        return Result.success(result);
    }

    @Operation(summary = "根据邮箱查询用户")
    @GetMapping("/by-email")
    public Result<Map<String, Object>> getUserByEmail(
            @Parameter(description = "邮箱") @RequestParam String email) {
        User user = userService.getUserByEmail(email);
        // TODO: 转换User对象为Map
        Map<String, Object> result = new HashMap<>();
        return Result.success(result);
    }

    @Operation(summary = "根据手机号查询用户")
    @GetMapping("/by-phone")
    public Result<Map<String, Object>> getUserByPhone(
            @Parameter(description = "手机号") @RequestParam String phone) {
        User user = userService.getUserByPhone(phone);
        // TODO: 转换User对象为Map
        Map<String, Object> result = new HashMap<>();
        return Result.success(result);
    }

    @Operation(summary = "获取用户角色")
    @GetMapping("/{id}/roles")
    public Result<List<Map<String, Object>>> getUserRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = userService.getUserRoles(id);
        return Result.success(result);
    }

    @Operation(summary = "获取用户权限")
    @GetMapping("/{id}/permissions")
    public Result<List<String>> getUserPermissions(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        List<String> result = userService.getUserPermissions(id);
        return Result.success(result);
    }

    @Operation(summary = "分配用户角色")
    @PostMapping("/{id}/assign-roles")
    public Result<Boolean> assignUserRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        boolean result = userService.assignUserRoles(id, roleIds);
        return Result.success(result);
    }

    @Operation(summary = "移除用户角色")
    @PostMapping("/{id}/remove-roles")
    public Result<Boolean> removeUserRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        boolean result = userService.removeUserRoles(id, roleIds);
        return Result.success(result);
    }

    @Operation(summary = "更新用户登录信息")
    @PostMapping("/{id}/update-login-info")
    public Result<Boolean> updateUserLoginInfo(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "登录IP") @RequestParam String loginIp) {
        boolean result = userService.updateLoginInfo(id, loginIp);
        return Result.success(result);
    }

    @Operation(summary = "导出用户数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportUserData(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<Map<String, Object>> result = userService.exportUserData(
                username, realName, email, phone, departmentId != null ? departmentId.toString() : null, status);
        return Result.success(result);
    }

}