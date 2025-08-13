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
     * 项目名称
     */
    @TableField("name")
    private String name;

    /**
     * 项目编码
     */
    @TableField("code")
    private String code;

    /**
     * 项目类型：1-住宅，2-商业，3-办公，4-综合
     */
    @TableField("type")
    private Integer type;

    /**
     * 项目状态：1-规划中，2-建设中，3-已完成，4-已交付
     */
    @TableField("status")
    private Integer status;

    /**
     * 项目地址
     */
    @TableField("address")
    private String address;

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
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

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

    // 项目类型枚举
    public enum Type {
        RESIDENTIAL(1, "住宅"),
        COMMERCIAL(2, "商业"),
        OFFICE(3, "办公"),
        MIXED(4, "综合");

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

    // 项目状态枚举
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