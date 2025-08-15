package com.cysz.minimal.enums;

/**
 * 单元资产状态枚举
 */
public enum UnitStatus {
    
    RENTABLE("RENTABLE", "可租"),
    SELF_USE("SELF_USE", "自用"),
    PUBLIC_USE("PUBLIC_USE", "公用"),
    LEASE_BACK("LEASE_BACK", "返租"),
    DISABLED("DISABLED", "停用"),
    SELF_RENTAL("SELF_RENTAL", "自持出租");
    
    private final String code;
    private final String displayName;
    
    UnitStatus(String code, String displayName) {
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
    public static UnitStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (UnitStatus status : UnitStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的单元状态代码: " + code);
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        UnitStatus status = fromCode(code);
        return status != null ? status.getDisplayName() : code;
    }
    
    /**
     * 检查代码是否有效
     */
    public static boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        for (UnitStatus status : UnitStatus.values()) {
            if (status.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}