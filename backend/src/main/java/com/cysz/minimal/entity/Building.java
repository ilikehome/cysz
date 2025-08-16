package com.cysz.minimal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cysz.minimal.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 楼栋实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("building")
public class Building extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 楼栋编码
     */
    private String buildingCode;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 建筑面积
     */
    private java.math.BigDecimal buildingArea;

    /**
     * 出租面积
     */
    private java.math.BigDecimal rentArea;

    /**
     * 物业面积
     */
    private java.math.BigDecimal propertyArea;

    /**
     * 可用面积
     */
    private java.math.BigDecimal usableArea;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（1：启用，0：禁用）
     */
    private Integer status;
}