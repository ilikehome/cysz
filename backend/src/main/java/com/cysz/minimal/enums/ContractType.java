package com.cysz.minimal.enums;

/**
 * 合同类型枚举
 * 统一管理合同类型的英文代码和中文显示名称
 * 
 * @author CodeBuddy
 * @since 2025-08-15
 */
public enum ContractType {
    
    RENT("RENT", "租赁"),
    SHOP("SHOP", "商铺"),
    OFFICE("OFFICE", "办公"),
    OFFICE_BUILDING("OFFICE_BUILDING", "写字楼"),
    COWORKING("COWORKING", "联合办公"),
    WAREHOUSE("WAREHOUSE", "仓库"),
    PARKING("PARKING", "停车位");
    
    private final String code;
    private final String displayName;
    
    ContractType(String code, String displayName) {
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
    public static ContractType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ContractType type : ContractType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的合同类型代码: " + code);
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        ContractType type = fromCode(code);
        return type != null ? type.displayName : code;
    }
    
    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        for (ContractType type : ContractType.values()) {
            if (type.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取所有合同类型选项
     */
    public static ContractType[] getAllTypes() {
        return ContractType.values();
    }
}