package com.cysz.minimal.vo.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 实收记录VO
 */
public class ReceivedRecordVO {
    private Long id;
    private String receivedId;
    private String contractId;
    private String contractNo;
    private String contractName;
    private String tenantName;
    private String unitName;
    private String unitDescription;
    private String feeType;
    private String billType;
    private BigDecimal amount;
    private LocalDate receivedDate;
    private String billStatus;
    private String bankTransactionNo;
    private String paymentMethod;
    private String status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getReceivedId() { return receivedId; }
    public void setReceivedId(String receivedId) { this.receivedId = receivedId; }
    
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    
    public String getUnitDescription() { return unitDescription; }
    public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }
    
    public String getFeeType() { return feeType; }
    public void setFeeType(String feeType) { this.feeType = feeType; }
    
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public LocalDate getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDate receivedDate) { this.receivedDate = receivedDate; }
    
    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
    
    public String getBankTransactionNo() { return bankTransactionNo; }
    public void setBankTransactionNo(String bankTransactionNo) { this.bankTransactionNo = bankTransactionNo; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}