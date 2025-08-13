package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 楼栋实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buildings")
public class Building {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 楼栋编号
     */
    @TableField("number")
    private String number;

    /**
     * 楼栋名称
     */
    @TableField("name")
    private String name;

    /**
     * 楼栋类型：1-住宅楼，2-商业楼，3-办公楼，4-综合楼
     */
    @TableField("type")
    private Integer type;

    /**
     * 楼栋状态：1-规划中，2-建设中，3-已完成，4-已交付
     */
    @TableField("status")
    private Integer status;

    /**
     * 楼层数量
     */
    @TableField("floor_count")
    private Integer floorCount;

    /**
     * 单元数量
     */
    @TableField("unit_count")
    private Integer unitCount;

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
     * 楼栋描述
     */
    @TableField("description")
    private String description;

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

    // 楼栋类型枚举
    public enum Type {
        RESIDENTIAL(1, "住宅楼"),
        COMMERCIAL(2, "商业楼"),
        OFFICE(3, "办公楼"),
        MIXED(4, "综合楼");

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

    // 楼栋状态枚举
    public enum Status {
        PLANNING(1, "规划中"),
        CONSTRUCTION(2, "建设中"),
        COMPLETED(3, "已完成"),
        DELIVERED(4, "已交付");

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