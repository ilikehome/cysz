package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 单元实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("unit")
public class Unit {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单元编号（与数据库字段名保持一致）
     */
    @TableField("unit_code")
    private String unitCode;

    /**
     * 单元描述（与数据库字段名保持一致）
     */
    @TableField("unit_description")
    private String unitDescription;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 楼栋ID
     */
    @TableField("building_id")
    private Long buildingId;

    /**
     * 楼层ID
     */
    @TableField("floor_id")
    private Long floorId;

    /**
     * 楼层名称（关联查询字段，与前端保持一致）
     */
    @TableField(exist = false)
    private String floorName;

    /**
     * 楼栋名称（关联查询字段，与前端保持一致）
     */
    @TableField(exist = false)
    private String buildingName;

    /**
     * 项目名称（关联查询字段，与前端保持一致）
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 单元状态（与数据库字段名保持一致）
     * VACANT-空置，OCCUPIED-已租，MAINTENANCE-维护中，DISABLED-禁用
     */
    @TableField("unit_status")
    private String unitStatus;

    /**
     * 单元用途（与数据库字段名保持一致）
     */
    @TableField("unit_purpose")
    private String unitPurpose;

    /**
     * 建筑面积（与数据库字段名保持一致）
     */
    @TableField("building_area")
    private BigDecimal buildingArea;

    /**
     * 租赁面积（与数据库字段名保持一致）
     */
    @TableField("rent_area")
    private BigDecimal rentArea;

    /**
     * 物业面积（与数据库字段名保持一致）
     */
    @TableField("property_area")
    private BigDecimal propertyArea;

    /**
     * 是否多租户（与数据库字段名保持一致）
     */
    @TableField("is_multi_tenant")
    private Boolean isMultiTenant;

    /**
     * 用途（与数据库字段名保持一致）
     */
    @TableField("purpose")
    private String purpose;

    /**
     * 备注（与数据库字段名保持一致）
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态标志（与数据库字段名保持一致）
     */
    @TableField("status_flag")
    private Integer statusFlag;

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
    public String getUnitName() {
        return unitDescription; // 单元名称使用描述字段
    }

    public void setUnitName(String unitName) {
        this.unitDescription = unitName;
    }

    public String getName() {
        return unitDescription;
    }

    public void setName(String name) {
        this.unitDescription = name;
    }

    public String getNumber() {
        return unitCode;
    }

    public void setNumber(String number) {
        this.unitCode = number;
    }

    public String getCode() {
        return unitCode;
    }

    public void setCode(String code) {
        this.unitCode = code;
    }

    public BigDecimal getArea() {
        return buildingArea;
    }

    public void setArea(BigDecimal area) {
        this.buildingArea = area;
    }

    public BigDecimal getRentableArea() {
        return rentArea;
    }

    public void setRentableArea(BigDecimal rentableArea) {
        this.rentArea = rentableArea;
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

    // 单元状态枚举（与数据库实际值保持一致）
    public enum UnitStatus {
        VACANT("VACANT", "空置"),
        OCCUPIED("OCCUPIED", "已租"),
        MAINTENANCE("MAINTENANCE", "维护中"),
        DISABLED("DISABLED", "禁用"),
        // 兼容前端可能使用的状态
        RENTABLE("RENTABLE", "可租赁"),
        SELF_USE("SELF_USE", "自用"),
        PUBLIC_USE("PUBLIC_USE", "公用"),
        LEASE_BACK("LEASE_BACK", "回租"),
        SELF_RENTAL("SELF_RENTAL", "自营");

        private final String code;
        private final String name;

        UnitStatus(String code, String name) {
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