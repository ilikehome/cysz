package com.cysz.property.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目数据传输对象
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
public class ProjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String name;

    /**
     * 项目编码
     */
    @NotBlank(message = "项目编码不能为空")
    private String code;

    /**
     * 项目类型
     */
    @NotBlank(message = "项目类型不能为空")
    private String type;

    /**
     * 项目状态
     */
    @NotBlank(message = "项目状态不能为空")
    private String status;

    /**
     * 项目地址
     */
    private String address;

    /**
     * 总建筑面积（平方米）
     */
    @DecimalMin(value = "0.0", message = "总建筑面积不能小于0")
    private BigDecimal totalArea;

    /**
     * 可租赁面积（平方米）
     */
    @DecimalMin(value = "0.0", message = "可租赁面积不能小于0")
    private BigDecimal rentableArea;

    /**
     * 楼栋数量
     */
    @Min(value = 0, message = "楼栋数量不能小于0")
    private Integer buildingCount;

    /**
     * 单元数量
     */
    @Min(value = 0, message = "单元数量不能小于0")
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
}