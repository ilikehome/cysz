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
@TableName("units")
public class Unit {

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
     * 单元编号
     */
    @TableField("number")
    private String number;

    /**
     * 单元名称
     */
    @TableField("name")
    private String name;

    /**
     * 单元类型：1-一居室，2-两居室，3-三居室，4-四居室，5-商铺，6-办公室，7-仓库
     */
    @TableField("type")
    private Integer type;

    /**
     * 单元状态：1-空置，2-已租，3-维修中，4-预定
     */
    @TableField("status")
    private Integer status;

    /**
     * 建筑面积（平方米）
     */
    @TableField("area")
    private BigDecimal area;

    /**
     * 可租赁面积（平方米）
     */
    @TableField("rentable_area")
    private BigDecimal rentableArea;

    /**
     * 租金（元/月）
     */
    @TableField("rent")
    private BigDecimal rent;

    /**
     * 朝向：1-东，2-南，3-西，4-北，5-东南，6-西南，7-东北，8-西北
     */
    @TableField("orientation")
    private Integer orientation;

    /**
     * 装修状态：1-毛坯，2-简装，3-精装，4-豪装
     */
    @TableField("decoration")
    private Integer decoration;

    /**
     * 房间数
     */
    @TableField("room_count")
    private Integer roomCount;

    /**
     * 卫生间数
     */
    @TableField("bathroom_count")
    private Integer bathroomCount;

    /**
     * 阳台数
     */
    @TableField("balcony_count")
    private Integer balconyCount;

    /**
     * 单元描述
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

    // 单元类型枚举
    public enum Type {
        ONE_BEDROOM(1, "一居室"),
        TWO_BEDROOM(2, "两居室"),
        THREE_BEDROOM(3, "三居室"),
        FOUR_BEDROOM(4, "四居室"),
        SHOP(5, "商铺"),
        OFFICE(6, "办公室"),
        WAREHOUSE(7, "仓库");

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

    // 单元状态枚举
    public enum Status {
        VACANT(1, "空置"),
        RENTED(2, "已租"),
        MAINTENANCE(3, "维修中"),
        RESERVED(4, "预定");

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

    // 朝向枚举
    public enum Orientation {
        EAST(1, "东"),
        SOUTH(2, "南"),
        WEST(3, "西"),
        NORTH(4, "北"),
        SOUTHEAST(5, "东南"),
        SOUTHWEST(6, "西南"),
        NORTHEAST(7, "东北"),
        NORTHWEST(8, "西北");

        private final Integer code;
        private final String name;

        Orientation(Integer code, String name) {
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

    // 装修状态枚举
    public enum Decoration {
        ROUGH(1, "毛坯"),
        SIMPLE(2, "简装"),
        FINE(3, "精装"),
        LUXURY(4, "豪装");

        private final Integer code;
        private final String name;

        Decoration(Integer code, String name) {
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