package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 *
 * @author CYSZ
 * @since 2024-01-01
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param username 用户名
     * @param realName 真实姓名
     * @param email 邮箱
     * @param phone 手机号
     * @param department 部门
     * @param status 状态
     * @return 用户列表
     */
    Page<Map<String, Object>> selectUserPage(Page<Map<String, Object>> page,
                                             @Param("username") String username,
                                             @Param("realName") String realName,
                                             @Param("email") String email,
                                             @Param("phone") String phone,
                                             @Param("department") String department,
                                             @Param("status") Integer status);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    User selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User selectByPhone(@Param("phone") String phone);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM users " +
            "WHERE username = #{username} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkUsernameExists(@Param("username") String username,
                           @Param("excludeId") Long excludeId);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM users " +
            "WHERE email = #{email} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkEmailExists(@Param("email") String email,
                        @Param("excludeId") Long excludeId);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM users " +
            "WHERE phone = #{phone} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkPhoneExists(@Param("phone") String phone,
                        @Param("excludeId") Long excludeId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginTime 登录时间
     * @param loginIp 登录IP
     * @return 更新结果
     */
    @Update("UPDATE users SET " +
            "last_login_time = #{loginTime}, " +
            "last_login_ip = #{loginIp}, " +
            "login_count = IFNULL(login_count, 0) + 1 " +
            "WHERE id = #{userId}")
    int updateLoginInfo(@Param("userId") Long userId,
                       @Param("loginTime") LocalDateTime loginTime,
                       @Param("loginIp") String loginIp);

    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("SELECT r.* FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "AND r.deleted = 0 AND r.status = 0")
    List<Map<String, Object>> selectUserRoles(@Param("userId") Long userId);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectUserPermissions(@Param("userId") Long userId);

    /**
     * 获取用户统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT COUNT(*) as totalUsers, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as activeUsers, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as disabledUsers, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as lockedUsers " +
            "FROM users WHERE deleted = 0")
    Map<String, Object> getUserStatistics();

    /**
     * 根据部门统计用户数量
     *
     * @return 部门统计
     */
    @Select("SELECT department, COUNT(*) as count " +
            "FROM users WHERE deleted = 0 AND status = 0 " +
            "GROUP BY department")
    List<Map<String, Object>> getUserCountByDepartment();

    /**
     * 获取最近登录的用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT id, username, real_name, last_login_time, last_login_ip " +
            "FROM users WHERE deleted = 0 AND last_login_time IS NOT NULL " +
            "ORDER BY last_login_time DESC LIMIT #{limit}")
    List<Map<String, Object>> getRecentLoginUsers(@Param("limit") Integer limit);

}