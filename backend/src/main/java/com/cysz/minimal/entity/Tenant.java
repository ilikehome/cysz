package com.cysz.minimal.entity;

import com.cysz.minimal.entity.base.BaseEntity;

/**
 * 租户实体类
 * 对应数据库表：tenant
 */
public class Tenant extends BaseEntity {
    
    private String tenantName;              // 租户名称
    private String tenantNature;            // 租户性质：individual/company/government
    private String enterpriseNature;        // 企业性质
    private String socialCreditCode;        // 社会信用代码
    private String taxpayerId;              // 纳税人识别号
    private String businessRegistrationNumber; // 工商注册号
    private String individualLicenseNumber; // 个体户证件号
    private String brand;                   // 品牌
    private String brandQualification;      // 品牌资质：direct/franchise/joint
    private String businessFormat;          // 业态
    private String businessScope;           // 经营范围
    private String legalPersonName;         // 法人姓名
    private String legalPersonPhone;        // 法人手机号
    private String legalPersonIdCard;       // 法人身份证
    private String financeContact;          // 财务联系人
    private String financePhone;            // 财务电话
    private String payerName;               // 付款人名称
    private String paymentAccount;          // 付款账号
    private String remark;                  // 备注
    
    // 构造函数
    public Tenant() {}
    
    // Getter和Setter方法
    public String getTenantName() {
        return tenantName;
    }
    
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    
    public String getTenantNature() {
        return tenantNature;
    }
    
    public void setTenantNature(String tenantNature) {
        this.tenantNature = tenantNature;
    }
    
    public String getEnterpriseNature() {
        return enterpriseNature;
    }
    
    public void setEnterpriseNature(String enterpriseNature) {
        this.enterpriseNature = enterpriseNature;
    }
    
    public String getSocialCreditCode() {
        return socialCreditCode;
    }
    
    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }
    
    public String getTaxpayerId() {
        return taxpayerId;
    }
    
    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }
    
    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }
    
    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
    
    public String getIndividualLicenseNumber() {
        return individualLicenseNumber;
    }
    
    public void setIndividualLicenseNumber(String individualLicenseNumber) {
        this.individualLicenseNumber = individualLicenseNumber;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getBrandQualification() {
        return brandQualification;
    }
    
    public void setBrandQualification(String brandQualification) {
        this.brandQualification = brandQualification;
    }
    
    public String getBusinessFormat() {
        return businessFormat;
    }
    
    public void setBusinessFormat(String businessFormat) {
        this.businessFormat = businessFormat;
    }
    
    public String getBusinessScope() {
        return businessScope;
    }
    
    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }
    
    public String getLegalPersonName() {
        return legalPersonName;
    }
    
    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }
    
    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }
    
    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }
    
    public String getLegalPersonIdCard() {
        return legalPersonIdCard;
    }
    
    public void setLegalPersonIdCard(String legalPersonIdCard) {
        this.legalPersonIdCard = legalPersonIdCard;
    }
    
    public String getFinanceContact() {
        return financeContact;
    }
    
    public void setFinanceContact(String financeContact) {
        this.financeContact = financeContact;
    }
    
    public String getFinancePhone() {
        return financePhone;
    }
    
    public void setFinancePhone(String financePhone) {
        this.financePhone = financePhone;
    }
    
    public String getPayerName() {
        return payerName;
    }
    
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
    
    public String getPaymentAccount() {
        return paymentAccount;
    }
    
    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    

    

    
    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + getId() +
                ", tenantName='" + tenantName + '\'' +
                ", tenantNature='" + tenantNature + '\'' +
                ", enterpriseNature='" + enterpriseNature + '\'' +
                ", socialCreditCode='" + socialCreditCode + '\'' +
                ", taxpayerId='" + taxpayerId + '\'' +
                ", businessRegistrationNumber='" + businessRegistrationNumber + '\'' +
                ", individualLicenseNumber='" + individualLicenseNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", brandQualification='" + brandQualification + '\'' +
                ", businessFormat='" + businessFormat + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", legalPersonName='" + legalPersonName + '\'' +
                ", legalPersonPhone='" + legalPersonPhone + '\'' +
                ", legalPersonIdCard='" + legalPersonIdCard + '\'' +
                ", financeContact='" + financeContact + '\'' +
                ", financePhone='" + financePhone + '\'' +
                ", payerName='" + payerName + '\'' +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                '}';
    }
}