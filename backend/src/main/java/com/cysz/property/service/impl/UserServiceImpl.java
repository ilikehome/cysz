package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.User;
import com.cysz.property.mapper.UserMapper;
import com.cysz.property.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 用户Service实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PageResult<Map<String, Object>> getUserPage(PageQuery pageQuery,
                                                       String username,
                                                       String realName,
                                                       String email,
                                                       String phone,
                                                       String department,
                                                       Integer status) {
        // TODO: 实现分页查询用户列表
        return new PageResult<>();
    }

    @Override
    public Map<String, Object> getUserDetail(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", getUserRoles(userId));
        result.put("permissions", getUserPermissions(userId));
        
        return result;
    }

    @Override
    public boolean createUser(User user) {
        if (user == null) {
            return false;
        }
        
        // 检查用户名是否已存在
        if (checkUsernameExists(user.getUsername(), null)) {
            return false;
        }
        
        // 检查邮箱是否已存在
        if (StringUtils.hasText(user.getEmail()) && checkEmailExists(user.getEmail(), null)) {
            return false;
        }
        
        // 检查手机号是否已存在
        if (StringUtils.hasText(user.getPhone()) && checkPhoneExists(user.getPhone(), null)) {
            return false;
        }
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(User.Status.ENABLED.getCode());
        }
        
        return save(user);
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        
        User existUser = getById(user.getId());
        if (existUser == null) {
            return false;
        }
        
        // 检查用户名是否已存在
        if (checkUsernameExists(user.getUsername(), user.getId())) {
            return false;
        }
        
        // 检查邮箱是否已存在
        if (StringUtils.hasText(user.getEmail()) && checkEmailExists(user.getEmail(), user.getId())) {
            return false;
        }
        
        // 检查手机号是否已存在
        if (StringUtils.hasText(user.getPhone()) && checkPhoneExists(user.getPhone(), user.getId())) {
            return false;
        }
        
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userId == null) {
            return false;
        }
        
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        return removeById(userId);
    }

    @Override
    public boolean batchDeleteUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        
        return removeByIds(userIds);
    }

    @Override
    public Map<String, Object> login(String username, String password, String loginIp) {
        // 根据用户名查询用户
         User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码（这里简化处理，实际应该使用BCrypt等加密方式）
        if (!password.equals("123456")) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        
        // 生成token（简化处理）
        String token = "token_" + user.getId() + "_" + System.currentTimeMillis();
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        
        return result;
    }

    @Override
    public boolean logout(String token) {
        // 简化实现，实际应该清除token缓存
        return StringUtils.hasText(token);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return false;
        }
        
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        // 验证旧密码（简化处理）
        if (!oldPassword.equals(user.getPassword())) {
            return false;
        }
        
        // 更新密码
        user.setPassword(newPassword);
        return updateById(user);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        if (userId == null || !StringUtils.hasText(newPassword)) {
            return false;
        }
        
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setPassword(newPassword);
        return updateById(user);
    }

    @Override
    public boolean enableUser(Long userId) {
        return updateUserStatus(userId, User.Status.ENABLED.getCode());
    }

    @Override
    public boolean disableUser(Long userId) {
        return updateUserStatus(userId, User.Status.DISABLED.getCode());
    }

    @Override
    public boolean lockUser(Long userId) {
        return updateUserStatus(userId, User.Status.LOCKED.getCode());
    }

    @Override
    public boolean unlockUser(Long userId) {
        return updateUserStatus(userId, User.Status.ENABLED.getCode());
    }
    
    private boolean updateUserStatus(Long userId, Integer status) {
        if (userId == null || status == null) {
            return false;
        }
        
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setStatus(status);
        return updateById(user);
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        return userMapper.getUserStatistics();
    }

    @Override
    public List<Map<String, Object>> getUserCountByDepartment() {
        return userMapper.getUserCountByDepartment();
    }

    @Override
    public List<Map<String, Object>> getRecentLoginUsers(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return userMapper.getRecentLoginUsers(limit);
    }

    @Override
    public boolean checkUsernameExists(String username, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return userMapper.checkUsernameExists(username, excludeId) > 0;
    }

    @Override
    public boolean checkEmailExists(String email, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return userMapper.checkEmailExists(email, excludeId) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone, Long excludeId) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        return userMapper.checkPhoneExists(phone, excludeId) > 0;
    }

    @Override
    public User getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return userMapper.selectByEmail(email);
    }

    @Override
    public User getUserByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        return userMapper.selectByPhone(phone);
    }

    @Override
    public List<Map<String, Object>> getUserRoles(Long userId) {
        // TODO: 实现获取用户角色列表
        return new ArrayList<>();
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        // TODO: 实现获取用户权限列表
        return new ArrayList<>();
    }

    @Override
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        // TODO: 实现分配用户角色
        return false;
    }

    @Override
    public boolean removeUserRoles(Long userId, List<Long> roleIds) {
        // TODO: 实现移除用户角色
        return false;
    }

    @Override
    public boolean updateLoginInfo(Long userId, String loginIp) {
        // TODO: 实现更新用户登录信息
        return false;
    }

    @Override
    public List<Map<String, Object>> exportUserData(String username,
                                                    String realName,
                                                    String email,
                                                    String phone,
                                                    String department,
                                                    Integer status) {
        // TODO: 实现导出用户数据
        return new ArrayList<>();
    }
}