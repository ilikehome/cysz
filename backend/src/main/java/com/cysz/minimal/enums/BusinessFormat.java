package com.cysz.minimal.enums;

/**
 * 业态枚举
 * 定义租户和合同中使用的业态类型
 */
public enum BusinessFormat {
    
    // 零售业态
    DEPARTMENT_STORE("department_store", "百货商场"),
    SHOPPING_MALL("shopping_mall", "购物中心"),
    SUPERMARKET("supermarket", "超市"),
    CONVENIENCE_STORE("convenience_store", "便利店"),
    SPECIALTY_STORE("specialty_store", "专卖店"),
    BRAND_STORE("brand_store", "品牌店"),
    DISCOUNT_STORE("discount_store", "折扣店"),
    DUTY_FREE_SHOP("duty_free_shop", "免税店"),
    
    // 餐饮业态
    FINE_DINING("fine_dining", "正餐"),
    FAST_FOOD("fast_food", "快餐"),
    CASUAL_DINING("casual_dining", "休闲餐饮"),
    COFFEE_SHOP("coffee_shop", "咖啡厅"),
    TEA_SHOP("tea_shop", "茶饮店"),
    BAR("bar", "酒吧"),
    BAKERY("bakery", "烘焙店"),
    DESSERT_SHOP("dessert_shop", "甜品店"),
    
    // 娱乐业态
    CINEMA("cinema", "电影院"),
    KTV("ktv", "KTV"),
    GAME_CENTER("game_center", "游戏厅"),
    GYM("gym", "健身房"),
    BEAUTY_SALON("beauty_salon", "美容美发"),
    SPA("spa", "SPA"),
    KIDS_PLAYGROUND("kids_playground", "儿童乐园"),
    ESCAPE_ROOM("escape_room", "密室逃脱"),
    
    // 服务业态
    BANK("bank", "银行"),
    INSURANCE("insurance", "保险"),
    TELECOM_OFFICE("telecom_office", "通讯营业厅"),
    COURIER_SERVICE("courier_service", "快递服务"),
    LAUNDRY("laundry", "洗衣店"),
    REPAIR_SERVICE("repair_service", "维修服务"),
    EDUCATION_TRAINING("education_training", "教育培训"),
    MEDICAL_CLINIC("medical_clinic", "医疗诊所"),
    
    // 办公业态
    OFFICE_BUILDING("office_building", "写字楼"),
    CO_WORKING("co_working", "联合办公"),
    INCUBATOR("incubator", "创业孵化器"),
    CONFERENCE_CENTER("conference_center", "会议中心"),
    SHOWROOM("showroom", "展示厅"),
    
    // 住宿业态
    HOTEL("hotel", "酒店"),
    HOMESTAY("homestay", "民宿"),
    HOSTEL("hostel", "青年旅社"),
    SERVICED_APARTMENT("serviced_apartment", "公寓式酒店"),
    
    // 其他业态
    WAREHOUSE_LOGISTICS("warehouse_logistics", "仓储物流"),
    AUTO_SERVICE("auto_service", "汽车服务"),
    PET_SERVICE("pet_service", "宠物服务"),
    CULTURE_ARTS("culture_arts", "文化艺术"),
    OTHER("other", "其他");
    
    private final String code;
    private final String displayName;
    
    BusinessFormat(String code, String displayName) {
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
    public static BusinessFormat fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        
        for (BusinessFormat format : BusinessFormat.values()) {
            if (format.getCode().equals(code.trim())) {
                return format;
            }
        }
        return null;
    }
    
    /**
     * 根据代码获取显示名称
     */
    public static String getDisplayNameByCode(String code) {
        BusinessFormat format = fromCode(code);
        return format != null ? format.getDisplayName() : code;
    }
    
    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
    
    /**
     * 根据中文显示名获取代码（用于数据迁移）
     */
    public static String getCodeByDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            return null;
        }
        
        for (BusinessFormat format : BusinessFormat.values()) {
            if (format.getDisplayName().equals(displayName.trim())) {
                return format.getCode();
            }
        }
        return null;
    }
    
    /**
     * 获取业态分类
     */
    public String getCategory() {
        switch (this) {
            case DEPARTMENT_STORE:
            case SHOPPING_MALL:
            case SUPERMARKET:
            case CONVENIENCE_STORE:
            case SPECIALTY_STORE:
            case BRAND_STORE:
            case DISCOUNT_STORE:
            case DUTY_FREE_SHOP:
                return "零售业态";
                
            case FINE_DINING:
            case FAST_FOOD:
            case CASUAL_DINING:
            case COFFEE_SHOP:
            case TEA_SHOP:
            case BAR:
            case BAKERY:
            case DESSERT_SHOP:
                return "餐饮业态";
                
            case CINEMA:
            case KTV:
            case GAME_CENTER:
            case GYM:
            case BEAUTY_SALON:
            case SPA:
            case KIDS_PLAYGROUND:
            case ESCAPE_ROOM:
                return "娱乐业态";
                
            case BANK:
            case INSURANCE:
            case TELECOM_OFFICE:
            case COURIER_SERVICE:
            case LAUNDRY:
            case REPAIR_SERVICE:
            case EDUCATION_TRAINING:
            case MEDICAL_CLINIC:
                return "服务业态";
                
            case OFFICE_BUILDING:
            case CO_WORKING:
            case INCUBATOR:
            case CONFERENCE_CENTER:
            case SHOWROOM:
                return "办公业态";
                
            case HOTEL:
            case HOMESTAY:
            case HOSTEL:
            case SERVICED_APARTMENT:
                return "住宿业态";
                
            case WAREHOUSE_LOGISTICS:
            case AUTO_SERVICE:
            case PET_SERVICE:
            case CULTURE_ARTS:
            case OTHER:
            default:
                return "其他业态";
        }
    }
    
    @Override
    public String toString() {
        return code;
    }
}