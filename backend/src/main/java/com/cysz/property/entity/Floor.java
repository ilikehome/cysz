package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 楼层实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("floors")
public class Floor {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 楼栋ID
     */
    @TableField("building_id")
    private Long buildingId;

    /**
     * 楼栋名称（关联查询字段，与前端保持一致）
     */
    @TableField(exist = false)
    private String buildingName;

    /**
     * 楼层名称（与前端字段名保持一致）
     */
    @TableField("name")
    private String floorName;

    /**
     * 楼层编号（与前端字段名保持一致）
     */
    @TableField("code")
    private String floorCode;

    /**
     * 楼层号（数字）
     */
    @TableField("floor_number")
    private Integer floorNumber;

    /**
     * 楼层类型：1-地下层，2-地面层，3-标准层，4-顶层
     */
    @TableField("type")
    private Integer type;

    /**
     * 楼层状态：1-启用，0-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 单元数量
     */
    @TableField("unit_count")
    private Integer unitCount;

    /**
     * 备注（与前端字段名保持一致）
     */
    @TableField("remark")
    private String remark;

    /**
     * 楼层描述
     */
    @TableField("description")
    private String description;

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
        return floorName;
    }

    public void setName(String name) {
        this.floorName = name;
    }

    public String getCode() {
        return floorCode;
    }

    public void setCode(String code) {
        this.floorCode = code;
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

    // 楼层类型枚举
    public enum Type {
        BASEMENT(1, "地下层"),
        GROUND(2, "地面层"),
        STANDARD(3, "标准层"),
        TOP(4, "顶层");

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

    // 楼层状态枚举
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