package com.cysz.minimal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目实体类
 */
@TableName("project")
public class Project {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String projectName;
    
    private String projectType;
    
    private String managementOrg;
    
    private String rentBillCompany;
    
    private String rentBillBankAccount;
    
    private String city;
    
    private String address;
    
    private String projectManager;
    
    private String contactPhone;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    // Getter and Setter methods
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectType() {
        return projectType;
    }
    
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    
    public String getManagementOrg() {
        return managementOrg;
    }
    
    public void setManagementOrg(String managementOrg) {
        this.managementOrg = managementOrg;
    }
    
    public String getRentBillCompany() {
        return rentBillCompany;
    }
    
    public void setRentBillCompany(String rentBillCompany) {
        this.rentBillCompany = rentBillCompany;
    }
    
    public String getRentBillBankAccount() {
        return rentBillBankAccount;
    }
    
    public void setRentBillBankAccount(String rentBillBankAccount) {
        this.rentBillBankAccount = rentBillBankAccount;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getProjectManager() {
        return projectManager;
    }
    
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    

}