package com.cysz.minimal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cysz.minimal.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 单元实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("unit")
public class Unit extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 单元名称
     */
    private String unitName;

    /**
     * 单元编码
     */
    private String unitCode;

    /**
     * 楼层ID
     */
    private Long floorId;

    /**
     * 建筑面积
     */
    private BigDecimal buildingArea;

    /**
     * 租赁面积
     */
    private BigDecimal rentArea;

    /**
     * 单元状态
     */
    private String unitStatus;

    /**
     * 单元用途
     */
    private String unitPurpose;



    /**
     * 是否多租户
     */
    private Boolean isMultiTenant;

    /**
     * 备注
     */
    private String remark;


}