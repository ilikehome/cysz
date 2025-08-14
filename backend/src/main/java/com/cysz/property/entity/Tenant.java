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
@TableName("tenant")
public class Tenant {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户编号（与数据库字段名保持一致）
     */
    @TableField("tenant_code")
    private String tenantCode;

    /**
     * 租户名称（与数据库字段名保持一致）
     */
    @TableField("tenant_name")
    private String tenantName;

    /**
     * 租户类别（与数据库字段名保持一致）
     */
    @TableField("tenant_category")
    private String tenantCategory;

    /**
     * 法人代表（与数据库字段名保持一致）
     */
    @TableField("legal_person")
    private String legalPerson;

    /**
     * 联系电话（与数据库字段名保持一致）
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 统一社会信用代码（与数据库字段名保持一致）
     */
    @TableField("social_credit_code")
    private String socialCreditCode;

    /**
     * 联系地址（与数据库字段名保持一致）
     */
    @TableField("contact_address")
    private String contactAddress;

    /**
     * 联系邮箱（与数据库字段名保持一致）
     */
    @TableField("email")
    private String email;

    /**
     * 证件类型（与数据库字段名保持一致）
     */
    @TableField("certificate_type")
    private String certificateType;

    /**
     * 纳税人识别号（与数据库字段名保持一致）
     */
    @TableField("taxpayer_id")
    private String taxpayerId;

    /**
     * 项目ID（与数据库字段名保持一致）
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间（与前端字段名保持一致）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（与前端字段名保持一致）
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // 兼容前端字段名的getter/setter方法
    public String getName() {
        return tenantName;
    }

    public void setName(String name) {
        this.tenantName = name;
    }

    public String getCode() {
        return tenantCode;
    }

    public void setCode(String code) {
        this.tenantCode = code;
    }

    public String getBusinessType() {
        return tenantCategory;
    }

    public void setBusinessType(String businessType) {
        this.tenantCategory = businessType;
    }

    public String getContactPerson() {
        return legalPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.legalPerson = contactPerson;
    }

    public String getAddress() {
        return contactAddress;
    }

    public void setAddress(String address) {
        this.contactAddress = address;
    }

    public String getContactEmail() {
        return email;
    }

    public void setContactEmail(String contactEmail) {
        this.email = contactEmail;
    }

    public String getRegisteredAddress() {
        return contactAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.contactAddress = registeredAddress;
    }

    public LocalDateTime getCreatedTime() {
        return createTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updateTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updateTime = updatedTime;
    }

    // 状态枚举
    public enum Status {
        DISABLED(0, "禁用"),
        ENABLED(1, "启用");

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