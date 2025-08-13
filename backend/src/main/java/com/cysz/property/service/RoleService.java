package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 角色管理Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface RoleService extends IService<Role> {

    /**
     * 分页查询角色列表
     */
    PageResult<Map<String, Object>> getRolePage(PageQuery pageQuery, String name, String code, Integer status);

    /**
     * 根据ID查询角色详情
     */
    Map<String, Object> getRoleDetail(Long id);

    /**
     * 创建角色
     */
    boolean createRole(Role role);

    /**
     * 更新角色
     */
    boolean updateRole(Role role);

    /**
     * 删除角色
     */
    boolean deleteRole(Long id);

    /**
     * 批量删除角色
     */
    boolean batchDeleteRoles(List<Long> roleIds);

    /**
     * 查询所有角色列表（用于下拉选择）
     */
    List<Map<String, Object>> getAllRoles();

    /**
     * 验证角色编码是否存在
     */
    boolean checkCodeExists(String code, Long excludeId);

    /**
     * 验证角色名称是否存在
     */
    boolean checkNameExists(String name, Long excludeId);

    /**
     * 获取角色权限列表
     */
    List<Map<String, Object>> getRolePermissions(Long roleId);

    /**
     * 分配角色权限
     */
    boolean assignRolePermissions(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色用户列表
     */
    List<Map<String, Object>> getRoleUsers(Long roleId);

    /**
     * 分配角色用户
     */
    boolean assignRoleUsers(Long roleId, List<Long> userIds);

    /**
     * 移除角色用户
     */
    boolean removeRoleUsers(Long roleId, List<Long> userIds);

    /**
     * 获取角色统计信息
     */
    Map<String, Object> getRoleStatistics();

    /**
     * 根据状态统计角色数量
     */
    List<Map<String, Object>> getRoleCountByStatus();

    /**
     * 启用角色
     */
    boolean enableRole(Long id);

    /**
     * 禁用角色
     */
    boolean disableRole(Long id);

    /**
     * 复制角色
     */
    boolean copyRole(Long sourceRoleId, String newRoleName, String newRoleCode);

    /**
     * 获取用户角色列表
     */
    List<Map<String, Object>> getUserRoles(Long userId);

    /**
     * 根据角色编码查询角色
     */
    Map<String, Object> getRoleByCode(String code);

    /**
     * 获取所有启用的角色
     */
    List<Map<String, Object>> getEnabledRoles();

    /**
     * 根据角色类型统计数量
     */
    List<Map<String, Object>> getRoleCountByType();

    /**
     * 获取角色关联的用户数量
     */
    Integer getRoleUserCount(Long roleId);

    /**
     * 批量分配用户到角色
     */
    boolean assignUsersToRole(Long roleId, List<Long> userIds);

    /**
     * 批量移除角色的用户
     */
    boolean removeUsersFromRole(Long roleId, List<Long> userIds);

    /**
     * 清空角色的所有用户
     */
    boolean clearRoleUsers(Long roleId);

    /**
     * 分配角色权限
     */
    boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    /**
     * 移除角色权限
     */
    boolean removePermissionsFromRole(Long roleId, List<Long> permissionIds);

    /**
     * 导出角色数据
     */
    List<Map<String, Object>> exportRoleData(String code, String name, Integer type, Integer status);
}