package com.cysz.minimal.enums;

/**
 * 付款频率枚举
 * 统一管理付款频率的英文代码和中文显示名称
 * 
 * @author CodeBuddy
 * @since 2025-08-16
 */
public enum PaymentFrequency {
    
    MONTHLY("MONTHLY", "月"),
    BI_MONTHLY("BI_MONTHLY", "双月"),
    QUARTERLY("QUARTERLY", "季度"),
    SEMI_ANNUALLY("SEMI_ANNUALLY", "半年"),
    ANNUALLY("ANNUALLY", "年");
    
    private final String code;
    private final String displayName;
    
    PaymentFrequency(String code, String displayName) {
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
    public static PaymentFrequency fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (PaymentFrequency frequency : PaymentFrequency.values()) {
            if (frequency.code.equals(code)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("未知的付款频率代码: " + code);
    }
    
    /**
     * 根据中文显示名获取枚举（用于数据迁移）
     */
    public static PaymentFrequency fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (PaymentFrequency frequency : PaymentFrequency.values()) {
            if (frequency.displayName.equals(displayName)) {
                return frequency;
            }
        }
        return null; // 返回null而不是抛异常，便于处理历史数据
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        PaymentFrequency frequency = fromCode(code);
        return frequency != null ? frequency.displayName : code;
    }
    
    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        for (PaymentFrequency frequency : PaymentFrequency.values()) {
            if (frequency.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取所有付款频率选项
     */
    public static PaymentFrequency[] getAllFrequencies() {
        return PaymentFrequency.values();
    }
    
    /**
     * 将中文显示名转换为英文代码（用于数据迁移）
     */
    public static String convertDisplayNameToCode(String displayName) {
        PaymentFrequency frequency = fromDisplayName(displayName);
        return frequency != null ? frequency.code : displayName;
    }
    
    /**
     * 将英文代码转换为中文显示名
     */
    public static String convertCodeToDisplayName(String code) {
        PaymentFrequency frequency = fromCode(code);
        return frequency != null ? frequency.displayName : code;
    }
}