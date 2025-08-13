package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Role;
import com.cysz.property.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 角色管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getRolePage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "角色编码") @RequestParam(required = false) String code,
            @Parameter(description = "角色名称") @RequestParam(required = false) String name,
            @Parameter(description = "角色类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        PageResult<Map<String, Object>> result = roleService.getRolePage(
                pageQuery, name, code, status);
        return Result.success(result);
    }

    @Operation(summary = "获取角色详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getRoleDetail(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = roleService.getRoleDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建角色")
    @PostMapping
    public Result<Boolean> createRole(
            @Parameter(description = "角色信息") @RequestBody @Valid Role role) {
        boolean result = roleService.createRole(role);
        return Result.success(result);
    }

    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public Result<Boolean> updateRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Parameter(description = "角色信息") @RequestBody @Valid Role role) {
        role.setId(id);
        boolean result = roleService.updateRole(role);
        return Result.success(result);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        boolean result = roleService.deleteRole(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除角色")
    @DeleteMapping
    public Result<Boolean> batchDeleteRoles(
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        boolean result = roleService.batchDeleteRoles(roleIds);
        return Result.success(result);
    }

    @Operation(summary = "根据角色编码查询角色")
    @GetMapping("/by-code/{code}")
    public Result<Map<String, Object>> getRoleByCode(
            @Parameter(description = "角色编码") @PathVariable String code) {
        Map<String, Object> result = roleService.getRoleByCode(code);
        return Result.success(result);
    }

    @Operation(summary = "验证角色编码是否存在")
    @GetMapping("/check-code")
    public Result<Boolean> checkCodeExists(
            @Parameter(description = "角色编码") @RequestParam String code,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) Long excludeId) {
        boolean result = roleService.checkCodeExists(code, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "验证角色名称是否存在")
    @GetMapping("/check-name")
    public Result<Boolean> checkNameExists(
            @Parameter(description = "角色名称") @RequestParam String name,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) Long excludeId) {
        boolean result = roleService.checkNameExists(name, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取所有启用的角色")
    @GetMapping("/enabled")
    public Result<List<Map<String, Object>>> getEnabledRoles() {
        List<Map<String, Object>> result = roleService.getEnabledRoles();
        return Result.success(result);
    }

    @Operation(summary = "获取角色统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getRoleStatistics() {
        Map<String, Object> result = roleService.getRoleStatistics();
        return Result.success(result);
    }

    @Operation(summary = "根据角色类型统计数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> getRoleCountByType() {
        List<Map<String, Object>> result = roleService.getRoleCountByType();
        return Result.success(result);
    }

    @Operation(summary = "获取角色关联的用户数量")
    @GetMapping("/{id}/user-count")
    public Result<Integer> getRoleUserCount(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        Integer result = roleService.getRoleUserCount(id);
        return Result.success(result);
    }

    @Operation(summary = "获取角色关联的用户列表")
    @GetMapping("/{id}/users")
    public Result<List<Map<String, Object>>> getRoleUsers(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = roleService.getRoleUsers(id);
        return Result.success(result);
    }

    @Operation(summary = "批量分配用户到角色")
    @PostMapping("/{id}/assign-users")
    public Result<Boolean> assignUsersToRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Parameter(description = "用户ID列表") @RequestBody List<Long> userIds) {
        boolean result = roleService.assignUsersToRole(id, userIds);
        return Result.success(result);
    }

    @Operation(summary = "批量移除角色的用户")
    @PostMapping("/{id}/remove-users")
    public Result<Boolean> removeUsersFromRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Parameter(description = "用户ID列表") @RequestBody List<Long> userIds) {
        boolean result = roleService.removeUsersFromRole(id, userIds);
        return Result.success(result);
    }

    @Operation(summary = "清空角色的所有用户")
    @PostMapping("/{id}/clear-users")
    public Result<Boolean> clearRoleUsers(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        boolean result = roleService.clearRoleUsers(id);
        return Result.success(result);
    }

    @Operation(summary = "获取角色权限")
    @GetMapping("/{id}/permissions")
    public Result<List<Map<String, Object>>> getRolePermissions(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = roleService.getRolePermissions(id);
        return Result.success(result);
    }

    @Operation(summary = "分配角色权限")
    @PostMapping("/{id}/assign-permissions")
    public Result<Boolean> assignPermissionsToRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        boolean result = roleService.assignPermissionsToRole(id, permissionIds);
        return Result.success(result);
    }

    @Operation(summary = "移除角色权限")
    @PostMapping("/{id}/remove-permissions")
    public Result<Boolean> removePermissionsFromRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        boolean result = roleService.removePermissionsFromRole(id, permissionIds);
        return Result.success(result);
    }

    @Operation(summary = "导出角色数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportRoleData(
            @Parameter(description = "角色编码") @RequestParam(required = false) String code,
            @Parameter(description = "角色名称") @RequestParam(required = false) String name,
            @Parameter(description = "角色类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<Map<String, Object>> result = roleService.exportRoleData(code, name, type, status);
        return Result.success(result);
    }

}