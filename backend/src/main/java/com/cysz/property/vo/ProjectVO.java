package com.cysz.property.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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
     * 项目类型
     */
    private String type;

    /**
     * 项目类型名称
     */
    private String typeName;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 租金账单公司
     */
    private String rentBillCompany;

    /**
     * 物业账单公司
     */
    private String propertyBillCompany;

    /**
     * 产权公司
     */
    private String propertyRightCompany;

    /**
     * 建筑面积
     */
    private BigDecimal buildingArea;

    /**
     * 租赁面积
     */
    private BigDecimal rentArea;

    /**
     * 物业面积
     */
    private BigDecimal propertyArea;

    /**
     * 城市
     */
    private String city;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 管理机构
     */
    private String managementOrg;

    /**
     * 租金账单银行账户
     */
    private String rentBillBankAccount;

    /**
     * 项目经理
     */
    private String projectManager;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 兼容前端字段名的getter/setter方法
    public String getProjectName() {
        return name;
    }

    public void setProjectName(String projectName) {
        this.name = projectName;
    }

    public String getProjectType() {
        return type;
    }

    public void setProjectType(String projectType) {
        this.type = projectType;
    }

    public LocalDateTime getCreatedTime() {
        return createTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updateTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updateTime = updatedTime;
    }
}