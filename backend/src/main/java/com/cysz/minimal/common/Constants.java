package com.cysz.minimal.common;

/**
 * 系统常量定义
 */
public class Constants {
    
    // 响应状态码
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;
    
    // 响应消息
    public static final String SUCCESS_MSG = "操作成功";
    public static final String ERROR_MSG = "操作失败";
    public static final String UNAUTHORIZED_MSG = "未授权访问";
    public static final String FORBIDDEN_MSG = "权限不足";
    
    // 默认分页参数
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUM = 1;
    
    // 系统配置
    public static final String DEFAULT_PASSWORD = "123456";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    
    private Constants() {
        // 工具类，禁止实例化
    }
}