package com.cysz.minimal.enums;

/**
 * 租金期间设定枚举
 * 统一管理租金期间设定的英文代码和中文显示名称
 * 
 * @author CodeBuddy
 * @since 2025-08-16
 */
public enum RentPeriodSetting {
    
    MONTH_END_CYCLE("MONTH_END_CYCLE", "月末截止计租周期"),
    LEASE_START_ROLLING("LEASE_START_ROLLING", "起租日滚动周期"),
    SPECIFIED_DATE_ROLLING("SPECIFIED_DATE_ROLLING", "指定日期滚动周期");
    
    private final String code;
    private final String displayName;
    
    RentPeriodSetting(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static RentPeriodSetting fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (RentPeriodSetting setting : RentPeriodSetting.values()) {
            if (setting.code.equals(code)) {
                return setting;
            }
        }
        throw new IllegalArgumentException("未知的租金期间设定代码: " + code);
    }
    
    /**
     * 根据中文显示名获取枚举（用于数据迁移）
     */
    public static RentPeriodSetting fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (RentPeriodSetting setting : RentPeriodSetting.values()) {
            if (setting.displayName.equals(displayName)) {
                return setting;
            }
        }
        return null; // 返回null而不是抛异常，便于处理历史数据
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        RentPeriodSetting setting = fromCode(code);
        return setting != null ? setting.displayName : code;
    }
    
    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        for (RentPeriodSetting setting : RentPeriodSetting.values()) {
            if (setting.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取所有租金期间设定选项
     */
    public static RentPeriodSetting[] getAllSettings() {
        return RentPeriodSetting.values();
    }
    
    /**
     * 将中文显示名转换为英文代码（用于数据迁移）
     */
    public static String convertDisplayNameToCode(String displayName) {
        RentPeriodSetting setting = fromDisplayName(displayName);
        return setting != null ? setting.code : displayName;
    }
    
    /**
     * 将英文代码转换为中文显示名
     */
    public static String convertCodeToDisplayName(String code) {
        RentPeriodSetting setting = fromCode(code);
        return setting != null ? setting.displayName : code;
    }
}