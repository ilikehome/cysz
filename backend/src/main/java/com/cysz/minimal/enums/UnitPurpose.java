package com.cysz.minimal.enums;

/**
 * 单元用途枚举
 */
public enum UnitPurpose {
    
    OFFICE("OFFICE", "办公"),
    WAREHOUSE("WAREHOUSE", "仓库"),
    FREIGHT("FREIGHT", "货运"),
    COMMERCIAL("COMMERCIAL", "商业"),
    MEETING_ROOM("MEETING_ROOM", "会议室"),
    BUSINESS_ROOM("BUSINESS_ROOM", "营业房"),
    PARKING("PARKING", "停车位"),
    ADVERTISING("ADVERTISING", "广告位"),
    APARTMENT("APARTMENT", "公寓"),
    MULTI_BUSINESS("MULTI_BUSINESS", "多经点位"),
    PROMOTION_VENUE("PROMOTION_VENUE", "推广场地");
    
    private final String code;
    private final String displayName;
    
    UnitPurpose(String code, String displayName) {
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
    public static UnitPurpose fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (UnitPurpose purpose : UnitPurpose.values()) {
            if (purpose.getCode().equals(code)) {
                return purpose;
            }
        }
        throw new IllegalArgumentException("未知的单元用途代码: " + code);
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        UnitPurpose purpose = fromCode(code);
        return purpose != null ? purpose.getDisplayName() : code;
    }
    
    /**
     * 检查代码是否有效
     */
    public static boolean isValidCode(String code) {
        if (code == null) {
            return false;
        }
        for (UnitPurpose purpose : UnitPurpose.values()) {
            if (purpose.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}