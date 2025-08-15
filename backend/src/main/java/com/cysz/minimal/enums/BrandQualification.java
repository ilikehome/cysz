package com.cysz.minimal.enums;

/**
 * 品牌资质枚举
 * 定义品牌资质类型，包含英文短字符串和中文显示名称的映射关系
 */
public enum BrandQualification {
    
    DIRECT("direct", "直营"),
    FRANCHISE("franchise", "加盟"),
    JOINT("joint", "联营");
    
    private final String code;
    private final String displayName;
    
    BrandQualification(String code, String displayName) {
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
     * 根据英文代码获取枚举值
     * @param code 英文代码
     * @return 对应的枚举值，如果不存在则返回null
     */
    public static BrandQualification fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (BrandQualification qualification : values()) {
            if (qualification.code.equals(code)) {
                return qualification;
            }
        }
        return null;
    }
    
    /**
     * 根据中文显示名称获取枚举值
     * @param displayName 中文显示名称
     * @return 对应的枚举值，如果不存在则返回null
     */
    public static BrandQualification fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (BrandQualification qualification : values()) {
            if (qualification.displayName.equals(displayName)) {
                return qualification;
            }
        }
        return null;
    }
    
    /**
     * 将前端中文显示名称转换为数据库英文代码
     * @param displayName 中文显示名称
     * @return 英文代码，如果不存在则返回原值
     */
    public static String convertToDbCode(String displayName) {
        BrandQualification qualification = fromDisplayName(displayName);
        return qualification != null ? qualification.getCode() : displayName;
    }
    
    /**
     * 将数据库英文代码转换为前端中文显示名称
     * @param code 英文代码
     * @return 中文显示名称，如果不存在则返回原值
     */
    public static String convertToDisplayName(String code) {
        BrandQualification qualification = fromCode(code);
        return qualification != null ? qualification.getDisplayName() : code;
    }
    
    /**
     * 验证代码是否有效
     * @param code 英文代码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
    
    /**
     * 验证显示名称是否有效
     * @param displayName 中文显示名称
     * @return 是否有效
     */
    public static boolean isValidDisplayName(String displayName) {
        return fromDisplayName(displayName) != null;
    }
}