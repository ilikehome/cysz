package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tenants")
public class Tenant {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户名称
     */
    @TableField("name")
    private String name;

    /**
     * 租户编号
     */
    @TableField("code")
    private String code;

    /**
     * 租户性质：1-个人，2-企业
     */
    @TableField("type")
    private Integer type;

    /**
     * 业务形态：1-零售，2-餐饮，3-服务，4-办公，5-居住
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 营业执照号
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 统一社会信用代码
     */
    @TableField("social_credit_code")
    private String socialCreditCode;

    /**
     * 信用等级：1-优秀，2-良好，3-一般，4-较差，5-很差
     */
    @TableField("credit_level")
    private Integer creditLevel;

    /**
     * 风险等级：1-低风险，2-中风险，3-高风险
     */
    @TableField("risk_level")
    private Integer riskLevel;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注
     */
    @TableField("description")
    private String description;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // 租户性质枚举
    public enum Type {
        INDIVIDUAL(1, "个人"),
        ENTERPRISE(2, "企业");

        private final Integer code;
        private final String name;

        Type(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 业务形态枚举
    public enum BusinessType {
        RETAIL(1, "零售"),
        CATERING(2, "餐饮"),
        SERVICE(3, "服务"),
        OFFICE(4, "办公"),
        RESIDENTIAL(5, "居住");

        private final Integer code;
        private final String name;

        BusinessType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 信用等级枚举
    public enum CreditLevel {
        EXCELLENT(1, "优秀"),
        GOOD(2, "良好"),
        AVERAGE(3, "一般"),
        POOR(4, "较差"),
        VERY_POOR(5, "很差");

        private final Integer code;
        private final String name;

        CreditLevel(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 风险等级枚举
    public enum RiskLevel {
        LOW(1, "低风险"),
        MEDIUM(2, "中风险"),
        HIGH(3, "高风险");

        private final Integer code;
        private final String name;

        RiskLevel(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 状态枚举
    public enum Status {
        ENABLED(1, "启用"),
        DISABLED(2, "禁用");

        private final Integer code;
        private final String name;

        Status(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}