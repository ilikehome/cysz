package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("projects")
public class Project {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称（与前端字段名保持一致）
     */
    @TableField("name")
    private String projectName;

    /**
     * 项目名称（与前端字段名保持一致）
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目类型（与前端字段名保持一致）
     * APARTMENT-公寓，COMMERCIAL_DISTRICT-商业街，OFFICE-写字楼，COMPLEX-综合体，HOTEL-酒店
     */
    @TableField("project_type")
    private String projectType;

    /**
     * 管理机构（与前端字段名保持一致）
     */
    @TableField("management_org")
    private String managementOrg;

    /**
     * 租金账单公司（与前端字段名保持一致）
     */
    @TableField("rent_bill_company")
    private String rentBillCompany;

    /**
     * 租金账单银行账户（与前端字段名保持一致）
     */
    @TableField("rent_bill_bank_account")
    private String rentBillBankAccount;

    /**
     * 城市（与前端字段名保持一致）
     */
    @TableField("city")
    private String city;

    /**
     * 项目地址（与前端字段名保持一致）
     */
    @TableField("address")
    private String address;

    /**
     * 项目经理（与前端字段名保持一致）
     */
    @TableField("project_manager")
    private String projectManager;

    /**
     * 联系电话（与前端字段名保持一致）
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 项目状态：1-启用，0-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 总建筑面积（平方米）
     */
    @TableField("total_area")
    private BigDecimal totalArea;

    /**
     * 可租赁面积（平方米）
     */
    @TableField("rentable_area")
    private BigDecimal rentableArea;

    /**
     * 楼栋数量
     */
    @TableField("building_count")
    private Integer buildingCount;

    /**
     * 单元数量
     */
    @TableField("unit_count")
    private Integer unitCount;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    /**
     * 开工日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 竣工日期
     */
    @TableField("completion_date")
    private LocalDate completionDate;

    /**
     * 交付日期
     */
    @TableField("delivery_date")
    private LocalDate deliveryDate;

    /**
     * 开发商
     */
    @TableField("developer")
    private String developer;

    /**
     * 物业公司
     */
    @TableField("property_company")
    private String propertyCompany;

    /**
     * 联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 创建时间（与前端字段名保持一致）
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（与前端字段名保持一致）
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // 兼容旧字段名的getter/setter方法
    public String getName() {
        return projectName;
    }

    public void setName(String name) {
        this.projectName = name;
    }

    public Integer getType() {
        if (projectType == null) return null;
        switch (projectType) {
            case "COMPLEX": return 4;
            case "COMMERCIAL_DISTRICT": return 2;
            case "HOTEL": return 3;
            case "APARTMENT": return 1;
            case "OFFICE": return 3;
            default: return 1;
        }
    }

    public void setType(Integer type) {
        if (type == null) {
            this.projectType = null;
            return;
        }
        switch (type) {
            case 1: this.projectType = "APARTMENT"; break;
            case 2: this.projectType = "COMMERCIAL_DISTRICT"; break;
            case 3: this.projectType = "OFFICE"; break;
            case 4: this.projectType = "COMPLEX"; break;
            default: this.projectType = "APARTMENT";
        }
    }

    // 项目类型枚举（与前端保持一致）
    public enum ProjectType {
        COMPLEX("COMPLEX", "综合体"),
        COMMERCIAL_DISTRICT("COMMERCIAL_DISTRICT", "商业街"),
        HOTEL("HOTEL", "酒店"),
        APARTMENT("APARTMENT", "公寓"),
        OFFICE("OFFICE", "写字楼");

        private final String code;
        private final String name;

        ProjectType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 项目状态枚举
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