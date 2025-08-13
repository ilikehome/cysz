package com.cysz.property.dto;

import com.cysz.property.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 项目查询数据传输对象
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectQueryDTO extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名称（模糊查询）
     */
    private String name;

    /**
     * 项目编码（模糊查询）
     */
    private String code;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目状态
     */
    private String status;

    /**
     * 项目地址（模糊查询）
     */
    private String address;

    /**
     * 开发商（模糊查询）
     */
    private String developer;

    /**
     * 物业公司（模糊查询）
     */
    private String propertyCompany;

    /**
     * 开工日期开始
     */
    private LocalDate startDateBegin;

    /**
     * 开工日期结束
     */
    private LocalDate startDateEnd;

    /**
     * 竣工日期开始
     */
    private LocalDate completionDateBegin;

    /**
     * 竣工日期结束
     */
    private LocalDate completionDateEnd;

    /**
     * 交付日期开始
     */
    private LocalDate deliveryDateBegin;

    /**
     * 交付日期结束
     */
    private LocalDate deliveryDateEnd;
}