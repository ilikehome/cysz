package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 楼栋实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("building")
public class Building {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 楼栋编号（与数据库字段名保持一致）
     */
    @TableField("building_code")
    private String buildingCode;

    /**
     * 楼栋名称（与数据库字段名保持一致）
     */
    @TableField("building_name")
    private String buildingName;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 项目名称（关联查询字段，与前端保持一致）
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 楼栋类型（与数据库字段名保持一致）
     * RESIDENTIAL-住宅，COMMERCIAL-商业，OFFICE-办公，MIXED-混合
     */
    @TableField("building_type")
    private String buildingType;

    /**
     * 楼层数
     */
    @TableField("floor_count")
    private Integer floorCount;

    /**
     * 建筑面积
     */
    @TableField("building_area")
    private BigDecimal buildingArea;

    /**
     * 租赁面积
     */
    @TableField("rent_area")
    private BigDecimal rentArea;

    /**
     * 物业面积
     */
    @TableField("property_area")
    private BigDecimal propertyArea;

    /**
     * 可用面积
     */
    @TableField("usable_area")
    private BigDecimal usableArea;

    /**
     * 楼栋状态
     */
    @TableField("building_status")
    private String buildingStatus;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

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
        return buildingName;
    }

    public void setName(String name) {
        this.buildingName = name;
    }

    public String getCode() {
        return buildingCode;
    }

    public void setCode(String code) {
        this.buildingCode = code;
    }

    public String getType() {
        return buildingType;
    }

    public void setType(String type) {
        this.buildingType = type;
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

    // 楼栋类型枚举
    public enum BuildingType {
        RESIDENTIAL("RESIDENTIAL", "住宅"),
        COMMERCIAL("COMMERCIAL", "商业"),
        OFFICE("OFFICE", "办公"),
        MIXED("MIXED", "混合");

        private final String code;
        private final String name;

        BuildingType(String code, String name) {
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

    // 楼栋状态枚举
    public enum BuildingStatus {
        NORMAL("NORMAL", "正常"),
        MAINTENANCE("MAINTENANCE", "维护中"),
        CONSTRUCTION("CONSTRUCTION", "建设中"),
        DISABLED("DISABLED", "禁用");

        private final String code;
        private final String name;

        BuildingStatus(String code, String name) {
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