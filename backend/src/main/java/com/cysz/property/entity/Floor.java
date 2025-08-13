package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
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
     * 楼层编号
     */
    @TableField("number")
    private String number;

    /**
     * 楼层名称
     */
    @TableField("name")
    private String name;

    /**
     * 楼层序号
     */
    @TableField("sequence")
    private Integer sequence;

    /**
     * 楼层类型：1-标准层，2-设备层，3-架空层，4-地下层
     */
    @TableField("type")
    private Integer type;

    /**
     * 楼层状态：1-规划中，2-建设中，3-已完成，4-已交付
     */
    @TableField("status")
    private Integer status;

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
     * 层高（米）
     */
    @TableField("height")
    private BigDecimal height;

    /**
     * 楼层描述
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

    // 楼层类型枚举
    public enum Type {
        STANDARD(1, "标准层"),
        EQUIPMENT(2, "设备层"),
        OVERHEAD(3, "架空层"),
        BASEMENT(4, "地下层");

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