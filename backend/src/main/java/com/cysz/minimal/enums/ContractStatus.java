package com.cysz.minimal.enums;

/**
 * 合同状态枚举
 * 定义合同的各种状态及其对应的中文显示名
 */
public enum ContractStatus {
    
    UNSIGNED_EFFECTIVE("unsigned_effective", "未盖章生效"),
    SIGNED_EFFECTIVE("signed_effective", "已盖章生效"),
    TERMINATED("terminated", "提前终止"),
    EXPIRED("expired", "自然到期");
    
    private final String code;
    private final String displayName;
    
    ContractStatus(String code, String displayName) {
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
    public static ContractStatus fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        
        for (ContractStatus status : ContractStatus.values()) {
            if (status.getCode().equals(code.trim())) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        ContractStatus status = fromCode(code);
        return status != null ? status.getDisplayName() : code;
    }
    
    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
    
    /**
     * 获取所有代码数组
     */
    public static String[] getAllCodes() {
        ContractStatus[] values = ContractStatus.values();
        String[] codes = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            codes[i] = values[i].getCode();
        }
        return codes;
    }
    
    /**
     * 获取所有显示名称数组
     */
    public static String[] getAllDisplayNames() {
        ContractStatus[] values = ContractStatus.values();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].getDisplayName();
        }
        return names;
    }
}