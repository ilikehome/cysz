package com.cysz.minimal.enums;

/**
 * 租金模式枚举
 * 
 * @author CodeBuddy
 * @since 2025-01-16
 */
public enum RentMode {
    
    /**
     * 固定租金
     */
    FIXED("fixed", "固定租金"),
    
    /**
     * 提成租金
     */
    COMMISSION("commission", "提成租金");
    
    private final String code;
    private final String displayName;
    
    /**
     * 构造函数
     * 
     * @param code 英文代码
     * @param displayName 中文显示名
     */
    RentMode(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    /**
     * 获取英文代码
     * 
     * @return 英文代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取中文显示名
     * 
     * @return 中文显示名
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 根据代码获取枚举实例
     * 
     * @param code 英文代码
     * @return 枚举实例，如果未找到则返回null
     */
    public static RentMode fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (RentMode mode : RentMode.values()) {
            if (mode.code.equals(code)) {
                return mode;
            }
        }
        return null;
    }
    
    /**
     * 根据代码获取中文显示名
     * 
     * @param code 英文代码
     * @return 中文显示名，如果未找到则返回原代码
     */
    public static String getDisplayNameByCode(String code) {
        RentMode mode = fromCode(code);
        return mode != null ? mode.displayName : code;
    }
    
    /**
     * 验证代码是否有效
     * 
     * @param code 英文代码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
    
    /**
     * 获取所有租金模式
     * 
     * @return 所有租金模式数组
     */
    public static RentMode[] getAllModes() {
        return RentMode.values();
    }
}