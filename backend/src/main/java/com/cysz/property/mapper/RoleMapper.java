package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Role;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 角色Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 分页查询角色列表
     *
     * @param page 分页参数
     * @param name 角色名称
     * @param code 角色编码
     * @param type 角色类型
     * @param status 状态
     * @return 角色列表
     */
    Page<Map<String, Object>> selectRolePage(Page<Map<String, Object>> page,
                                             @Param("name") String name,
                                             @Param("code") String code,
                                             @Param("type") Integer type,
                                             @Param("status") Integer status);

    /**
     * 根据角色编码查询角色
     *
     * @param code 角色编码
     * @return 角色信息
     */
    @Select("SELECT * FROM roles WHERE code = #{code} AND deleted = 0")
    Role selectByCode(@Param("code") String code);

    /**
     * 检查角色编码是否存在
     *
     * @param code 角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM roles " +
            "WHERE code = #{code} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkCodeExists(@Param("code") String code,
                       @Param("excludeId") Long excludeId);

    /**
     * 检查角色名称是否存在
     *
     * @param name 角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM roles " +
            "WHERE name = #{name} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkNameExists(@Param("name") String name,
                       @Param("excludeId") Long excludeId);

    /**
     * 获取所有启用的角色
     *
     * @return 角色列表
     */
    @Select("SELECT id, name, code, description FROM roles " +
            "WHERE deleted = 0 AND status = 0 " +
            "ORDER BY sort ASC, id ASC")
    List<Map<String, Object>> selectEnabledRoles();

    /**
     * 获取角色统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT COUNT(*) as totalRoles, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as enabledRoles, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as disabledRoles " +
            "FROM roles WHERE deleted = 0")
    Map<String, Object> getRoleStatistics();

    /**
     * 根据角色类型统计数量
     *
     * @return 类型统计
     */
    @Select("SELECT type, COUNT(*) as count " +
            "FROM roles WHERE deleted = 0 " +
            "GROUP BY type")
    List<Map<String, Object>> getRoleCountByType();

    /**
     * 获取角色关联的用户数量
     *
     * @param roleId 角色ID
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM user_roles ur " +
            "INNER JOIN users u ON ur.user_id = u.id " +
            "WHERE ur.role_id = #{roleId} AND u.deleted = 0")
    int getRoleUserCount(@Param("roleId") Long roleId);

    /**
     * 获取角色关联的用户列表
     *
     * @param roleId 角色ID
     * @return 用户列表
     */
    @Select("SELECT u.id, u.username, u.real_name, u.email, u.phone " +
            "FROM user_roles ur " +
            "INNER JOIN users u ON ur.user_id = u.id " +
            "WHERE ur.role_id = #{roleId} AND u.deleted = 0 " +
            "ORDER BY u.create_time DESC")
    List<Map<String, Object>> getRoleUsers(@Param("roleId") Long roleId);

    /**
     * 批量分配用户角色
     *
     * @param roleId 角色ID
     * @param userIds 用户ID列表
     * @return 分配结果
     */
    int batchAssignUsers(@Param("roleId") Long roleId,
                        @Param("userIds") List<Long> userIds);

    /**
     * 批量移除用户角色
     *
     * @param roleId 角色ID
     * @param userIds 用户ID列表
     * @return 移除结果
     */
    int batchRemoveUsers(@Param("roleId") Long roleId,
                        @Param("userIds") List<Long> userIds);

    /**
     * 清空角色的所有用户
     *
     * @param roleId 角色ID
     * @return 清空结果
     */
    int clearRoleUsers(@Param("roleId") Long roleId);

}