package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户列表
     *
     * @param pageQuery 分页查询参数
     * @param username 用户名
     * @param realName 真实姓名
     * @param email 邮箱
     * @param phone 手机号
     * @param department 部门
     * @param status 状态
     * @return 用户列表
     */
    PageResult<Map<String, Object>> getUserPage(PageQuery pageQuery,
                                               String username,
                                               String realName,
                                               String email,
                                               String phone,
                                               String department,
                                               Integer status);

    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     * @return 用户详细信息
     */
    Map<String, Object> getUserDetail(Long userId);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 创建结果
     */
    boolean createUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    boolean batchDeleteUsers(List<Long> userIds);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param loginIp 登录IP
     * @return 登录结果
     */
    Map<String, Object> login(String username, String password, String loginIp);

    /**
     * 用户登出
     *
     * @param token 令牌
     * @return 登出结果
     */
    boolean logout(String token);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 启用用户
     *
     * @param userId 用户ID
     * @return 启用结果
     */
    boolean enableUser(Long userId);

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     * @return 禁用结果
     */
    boolean disableUser(Long userId);

    /**
     * 锁定用户
     *
     * @param userId 用户ID
     * @return 锁定结果
     */
    boolean lockUser(Long userId);

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 解锁结果
     */
    boolean unlockUser(Long userId);

    /**
     * 获取用户统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getUserStatistics();

    /**
     * 根据部门统计用户数量
     *
     * @return 部门统计
     */
    List<Map<String, Object>> getUserCountByDepartment();

    /**
     * 获取最近登录的用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<Map<String, Object>> getRecentLoginUsers(Integer limit);

    /**
     * 验证用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkUsernameExists(String username, Long excludeId);

    /**
     * 验证邮箱是否存在
     *
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkEmailExists(String email, Long excludeId);

    /**
     * 验证手机号是否存在
     *
     * @param phone 手机号
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone, Long excludeId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getUserByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User getUserByPhone(String phone);

    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Map<String, Object>> getUserRoles(Long userId);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 分配用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果
     */
    boolean assignUserRoles(Long userId, List<Long> roleIds);

    /**
     * 移除用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 移除结果
     */
    boolean removeUserRoles(Long userId, List<Long> roleIds);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新结果
     */
    boolean updateLoginInfo(Long userId, String loginIp);

    /**
     * 导出用户数据
     *
     * @param username 用户名
     * @param realName 真实姓名
     * @param email 邮箱
     * @param phone 手机号
     * @param department 部门
     * @param status 状态
     * @return 导出数据
     */
    List<Map<String, Object>> exportUserData(String username,
                                             String realName,
                                             String email,
                                             String phone,
                                             String department,
                                             Integer status);

}