package com.cysz.minimal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cysz.minimal.entity.base.BaseEntity;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 合同实体类
 */
@Data
@TableName("contract")
public class Contract extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    private String contractNo;
    private String contractName;
    private String contractType;
    private Long projectId;
    private String signatory;
    private String signatoryPhone;
    private Long tenantId;
    private String tenantName;
    private String businessBrand;
    private String shopSign;
    private String businessFormat;
    private LocalDate signDate;
    private String buildingIds;
    private String floorIds;
    private String unitIds;
    private BigDecimal buildingArea;
    private BigDecimal rentableArea;
    private BigDecimal depositAmount;
    private String rentMode;
    private String contractStatus;
    

    @TableField(exist = false)
    private BigDecimal rentAmount;
    @TableField(exist = false)
    private String paymentCycle;
    @TableField(exist = false)
    private String paymentMethod;


    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal contractArea;
    private LocalDate depositLatestDate;
    private String feeCompany;
    private String rentPeriodSetting;
    private String lateFeeRule;
    private String paymentAccount;
    private String paymentFrequency;
    private Integer latestPaymentDay;
    private LocalDate firstPaymentLatestDate;
    private BigDecimal firstPeriodRent;
    private BigDecimal periodRent;
    private Integer status;
    private String remark;
    
    /**
     * 合同佣金规则列表（一对多关联）
     * 注意：此字段不映射到数据库表，通过Service层查询获取
     */
    @TableField(exist = false)
    private List<ContractCommissionRules> commissionRulesList;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContractNo() {
        return contractNo;
    }
    
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public String getSignatory() {
        return signatory;
    }
    
    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public LocalDate getSignDate() {
        return signDate;
    }
    
    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }
    
    public String getBuildingIds() {
        return buildingIds;
    }
    
    public void setBuildingIds(String buildingIds) {
        this.buildingIds = buildingIds;
    }
    
    public String getFloorIds() {
        return floorIds;
    }
    
    public void setFloorIds(String floorIds) {
        this.floorIds = floorIds;
    }
    
    public String getUnitIds() {
        return unitIds;
    }
    
    public void setUnitIds(String unitIds) {
        this.unitIds = unitIds;
    }
    
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }
    
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }
    
    public String getRentMode() {
        return rentMode;
    }
    
    public void setRentMode(String rentMode) {
        this.rentMode = rentMode;
    }
    
    public String getContractStatus() {
        return contractStatus;
    }
    
    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }
    

    

    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public List<ContractCommissionRules> getCommissionRulesList() {
        return commissionRulesList;
    }
    
    public void setCommissionRulesList(List<ContractCommissionRules> commissionRulesList) {
        this.commissionRulesList = commissionRulesList;
    }
    
    // 添加缺失的getter/setter方法
    public String getContractName() {
        return contractName;
    }
    
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    
    public String getContractType() {
        return contractType;
    }
    
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
    
    public String getSignatoryPhone() {
        return signatoryPhone;
    }
    
    public void setSignatoryPhone(String signatoryPhone) {
        this.signatoryPhone = signatoryPhone;
    }
    
    public String getTenantName() {
        return tenantName;
    }
    
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    
    public String getBusinessBrand() {
        return businessBrand;
    }
    
    public void setBusinessBrand(String businessBrand) {
        this.businessBrand = businessBrand;
    }
    
    public String getShopSign() {
        return shopSign;
    }
    
    public void setShopSign(String shopSign) {
        this.shopSign = shopSign;
    }
    
    public String getBusinessFormat() {
        return businessFormat;
    }
    
    public void setBusinessFormat(String businessFormat) {
        this.businessFormat = businessFormat;
    }
    
    public BigDecimal getBuildingArea() {
        return buildingArea;
    }
    
    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }
    
    public BigDecimal getRentableArea() {
        return rentableArea;
    }
    
    public void setRentableArea(BigDecimal rentableArea) {
        this.rentableArea = rentableArea;
    }
    
    public BigDecimal getContractArea() {
        return contractArea;
    }
    
    public void setContractArea(BigDecimal contractArea) {
        this.contractArea = contractArea;
    }
    
    public LocalDate getDepositLatestDate() {
        return depositLatestDate;
    }
    
    public void setDepositLatestDate(LocalDate depositLatestDate) {
        this.depositLatestDate = depositLatestDate;
    }
    
    public String getFeeCompany() {
        return feeCompany;
    }
    
    public void setFeeCompany(String feeCompany) {
        this.feeCompany = feeCompany;
    }
    
    public String getRentPeriodSetting() {
        return rentPeriodSetting;
    }
    
    public void setRentPeriodSetting(String rentPeriodSetting) {
        this.rentPeriodSetting = rentPeriodSetting;
    }
    
    public String getLateFeeRule() {
        return lateFeeRule;
    }
    
    public void setLateFeeRule(String lateFeeRule) {
        this.lateFeeRule = lateFeeRule;
    }
    
    public String getPaymentAccount() {
        return paymentAccount;
    }
    
    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }
    
    public String getPaymentFrequency() {
        return paymentFrequency;
    }
    
    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }
    
    public Integer getLatestPaymentDay() {
        return latestPaymentDay;
    }
    
    public void setLatestPaymentDay(Integer latestPaymentDay) {
        this.latestPaymentDay = latestPaymentDay;
    }
    
    public LocalDate getFirstPaymentLatestDate() {
        return firstPaymentLatestDate;
    }
    
    public void setFirstPaymentLatestDate(LocalDate firstPaymentLatestDate) {
        this.firstPaymentLatestDate = firstPaymentLatestDate;
    }
    
    public BigDecimal getFirstPeriodRent() {
        return firstPeriodRent;
    }
    
    public void setFirstPeriodRent(BigDecimal firstPeriodRent) {
        this.firstPeriodRent = firstPeriodRent;
    }
    
    public BigDecimal getPeriodRent() {
        return periodRent;
    }
    
    public void setPeriodRent(BigDecimal periodRent) {
        this.periodRent = periodRent;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    

    
    public BigDecimal getRentAmount() {
        return rentAmount;
    }
    
    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
    }
    
    public String getPaymentCycle() {
        return paymentCycle;
    }
    
    public void setPaymentCycle(String paymentCycle) {
        this.paymentCycle = paymentCycle;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}