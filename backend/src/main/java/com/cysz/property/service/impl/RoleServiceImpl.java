package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Role;
import com.cysz.property.mapper.RoleMapper;
import com.cysz.property.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色服务实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public PageResult<Map<String, Object>> getRolePage(PageQuery pageQuery, String name, String code, Integer status) {
        Page<Map<String, Object>> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        Page<Map<String, Object>> result = baseMapper.selectRolePage(page, name, code, null, status);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Map<String, Object> getRoleDetail(Long id) {
        if (id == null) {
            return null;
        }
        Role role = getById(id);
        if (role == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", role.getId());
        result.put("code", role.getCode());
        result.put("name", role.getName());
        result.put("type", role.getType());
        result.put("status", role.getStatus());
        result.put("description", role.getDescription());
        result.put("createdBy", role.getCreatedBy());
        result.put("createdTime", role.getCreatedTime());
        result.put("updatedBy", role.getUpdatedBy());
        result.put("updatedTime", role.getUpdatedTime());
        return result;
    }

    @Override
    public boolean createRole(Role role) {
        return save(role);
    }

    @Override
    public boolean updateRole(Role role) {
        return updateById(role);
    }

    @Override
    public boolean deleteRole(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDeleteRoles(List<Long> roleIds) {
        return removeByIds(roleIds);
    }

    @Override
    public List<Map<String, Object>> getAllRoles() {
        List<Role> roles = list();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Role role : roles) {
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("id", role.getId());
            roleMap.put("code", role.getCode());
            roleMap.put("name", role.getName());
            roleMap.put("type", role.getType());
            roleMap.put("status", role.getStatus());
            roleMap.put("description", role.getDescription());
            result.add(roleMap);
        }
        return result;
    }

    @Override
    public boolean checkCodeExists(String code, Long excludeId) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return baseMapper.checkCodeExists(code, excludeId) > 0;
    }

    @Override
    public boolean checkNameExists(String name, Long excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return baseMapper.checkNameExists(name, excludeId) > 0;
    }

    @Override
    public List<Map<String, Object>> getRolePermissions(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        // 这里需要查询角色权限关联表，暂时返回空列表
        return new ArrayList<>();
    }

    @Override
    public boolean assignRolePermissions(Long roleId, List<Long> permissionIds) {
        if (roleId == null || permissionIds == null || permissionIds.isEmpty()) {
            return false;
        }
        // 这里需要操作角色权限关联表，暂时返回true
        return true;
    }

    @Override
    public List<Map<String, Object>> getRoleUsers(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        return baseMapper.getRoleUsers(roleId);
    }

    @Override
    public boolean assignRoleUsers(Long roleId, List<Long> userIds) {
        if (roleId == null || userIds == null || userIds.isEmpty()) {
            return false;
        }
        return baseMapper.batchAssignUsers(roleId, userIds) > 0;
    }

    @Override
    public boolean removeRoleUsers(Long roleId, List<Long> userIds) {
        if (roleId == null || userIds == null || userIds.isEmpty()) {
            return false;
        }
        return baseMapper.batchRemoveUsers(roleId, userIds) > 0;
    }

    @Override
    public Map<String, Object> getRoleStatistics() {
        return baseMapper.getRoleStatistics();
    }

    @Override
    public List<Map<String, Object>> getRoleCountByStatus() {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> enabled = new HashMap<>();
        enabled.put("status", 1);
        enabled.put("statusName", "启用");
        enabled.put("count", count(new QueryWrapper<Role>().eq("status", 1)));
        result.add(enabled);
        
        Map<String, Object> disabled = new HashMap<>();
        disabled.put("status", 2);
        disabled.put("statusName", "禁用");
        disabled.put("count", count(new QueryWrapper<Role>().eq("status", 2)));
        result.add(disabled);
        
        return result;
    }

    @Override
    public boolean enableRole(Long id) {
        if (id == null) {
            return false;
        }
        Role role = new Role();
        role.setId(id);
        role.setStatus(1); // 启用状态
        return updateById(role);
    }

    @Override
    public boolean disableRole(Long id) {
        if (id == null) {
            return false;
        }
        Role role = new Role();
        role.setId(id);
        role.setStatus(2); // 禁用状态
        return updateById(role);
    }

    @Override
    public boolean copyRole(Long sourceRoleId, String newRoleName, String newRoleCode) {
        if (sourceRoleId == null || newRoleName == null || newRoleCode == null) {
            return false;
        }
        Role sourceRole = getById(sourceRoleId);
        if (sourceRole == null) {
            return false;
        }
        Role newRole = new Role();
        newRole.setCode(newRoleCode);
        newRole.setName(newRoleName);
        newRole.setType(sourceRole.getType());
        newRole.setStatus(sourceRole.getStatus());
        newRole.setDescription(sourceRole.getDescription());
        return save(newRole);
    }

    @Override
    public List<Map<String, Object>> getUserRoles(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        // 这里需要通过用户ID查询角色，暂时返回空列表
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getRoleByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        Role role = baseMapper.selectByCode(code);
        if (role == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", role.getId());
        result.put("code", role.getCode());
        result.put("name", role.getName());
        result.put("type", role.getType());
        result.put("status", role.getStatus());
        result.put("description", role.getDescription());
        return result;
    }

    @Override
    public List<Map<String, Object>> getEnabledRoles() {
        return baseMapper.selectEnabledRoles();
    }

    @Override
    public List<Map<String, Object>> getRoleCountByType() {
        return baseMapper.getRoleCountByType();
    }

    @Override
    public Integer getRoleUserCount(Long roleId) {
        if (roleId == null) {
            return 0;
        }
        return baseMapper.getRoleUserCount(roleId);
    }

    @Override
    public boolean assignUsersToRole(Long roleId, List<Long> userIds) {
        return assignRoleUsers(roleId, userIds);
    }

    @Override
    public boolean removeUsersFromRole(Long roleId, List<Long> userIds) {
        return removeRoleUsers(roleId, userIds);
    }

    @Override
    public boolean clearRoleUsers(Long roleId) {
        if (roleId == null) {
            return false;
        }
        return baseMapper.clearRoleUsers(roleId) >= 0;
    }

    @Override
    public boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        return assignRolePermissions(roleId, permissionIds);
    }

    @Override
    public boolean removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        if (roleId == null || permissionIds == null || permissionIds.isEmpty()) {
            return false;
        }
        // 这里需要操作角色权限关联表，暂时返回true
        return true;
    }

    @Override
    public List<Map<String, Object>> exportRoleData(String code, String name, Integer type, Integer status) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (code != null && !code.trim().isEmpty()) {
            queryWrapper.like("code", code);
        }
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        List<Role> roles = list(queryWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Role role : roles) {
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("id", role.getId());
            roleMap.put("code", role.getCode());
            roleMap.put("name", role.getName());
            roleMap.put("type", role.getType());
            roleMap.put("status", role.getStatus());
            roleMap.put("description", role.getDescription());
            roleMap.put("createdTime", role.getCreatedTime());
            result.add(roleMap);
        }
        return result;
    }
}