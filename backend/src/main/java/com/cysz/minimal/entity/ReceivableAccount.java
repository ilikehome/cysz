package com.cysz.minimal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cysz.minimal.entity.base.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应收账款实体类
 */
@TableName("receivable_account")
public class ReceivableAccount extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Integer contractId;
    private Integer tenantId;
    private Integer projectId;
    private BigDecimal amount;
    private String status;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String accountType;
    private String remark;
    private LocalDate inputDate;
    private String processStatus;
    private BigDecimal pendingAmount;
    private String accountStatus;
    private BigDecimal receivableAmount;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getContractId() {
        return contractId;
    }
    
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }
    
    public Integer getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public LocalDate getInputDate() {
        return inputDate;
    }
    
    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }
    
    public String getProcessStatus() {
        return processStatus;
    }
    
    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
    
    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }
    
    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
    
    public String getAccountStatus() {
        return accountStatus;
    }
    
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    
    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }
    
    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
}