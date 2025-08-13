package com.cysz.property.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目视图对象
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
public class ProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目类型名称
     */
    private String typeName;

    /**
     * 项目状态
     */
    private String status;

    /**
     * 项目状态名称
     */
    private String statusName;

    /**
     * 项目地址
     */
    private String address;

    /**
     * 总建筑面积（平方米）
     */
    private BigDecimal totalArea;

    /**
     * 可租赁面积（平方米）
     */
    private BigDecimal rentableArea;

    /**
     * 楼栋数量
     */
    private Integer buildingCount;

    /**
     * 单元数量
     */
    private Integer unitCount;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 开工日期
     */
    private LocalDate startDate;

    /**
     * 竣工日期
     */
    private LocalDate completionDate;

    /**
     * 交付日期
     */
    private LocalDate deliveryDate;

    /**
     * 开发商
     */
    private String developer;

    /**
     * 物业公司
     */
    private String propertyCompany;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}