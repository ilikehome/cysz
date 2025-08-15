package com.cysz.minimal.enums;

/**
 * 项目类型枚举
 * 
 * @author CodeBuddy
 * @since 2025-08-15
 */
public enum ProjectType {
    
    COMPLEX("COMPLEX", "综合体"),
    COMMERCIAL_DISTRICT("COMMERCIAL_DISTRICT", "商业街区"),
    HOTEL("HOTEL", "酒店"),
    APARTMENT("APARTMENT", "公寓"),
    OFFICE("OFFICE", "写字楼");
    
    private final String code;
    private final String displayName;
    
    ProjectType(String code, String displayName) {
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
    public static ProjectType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ProjectType type : ProjectType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的项目类型代码: " + code);
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        ProjectType type = fromCode(code);
        return type != null ? type.displayName : code;
    }
    
    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        for (ProjectType type : ProjectType.values()) {
            if (type.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}