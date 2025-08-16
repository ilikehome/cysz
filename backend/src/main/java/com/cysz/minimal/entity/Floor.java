package com.cysz.minimal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cysz.minimal.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 楼层实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("floor")
public class Floor extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 楼层名称
     */
    private String floorName;

    /**
     * 楼层编码
     */
    private String floorCode;

    /**
     * 楼栋ID
     */
    private Integer buildingId;



    /**
     * 备注
     */
    private String remark;
}