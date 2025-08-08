package com.cysz.minimal;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/receivable")
@CrossOrigin(origins = "*")
public class ReceivableController {
    
    // 模拟数据存储
    private static final Map<Long, ReceivableAccount> receivableStorage = new HashMap<>();
    private static Long nextId = 1L;
    
    static {
        // 初始化一些测试数据
        ReceivableAccount receivable1 = new ReceivableAccount();
        receivable1.setId(nextId++);
        receivable1.setBatchNo("B202401001");
        receivable1.setLineNo("001");
        receivable1.setProcessStatus("PENDING");
        receivable1.setCompany("万达集团");
        receivable1.setProjectId(1L);
        receivable1.setPaymentMethod("银行转账");
        receivable1.setPayeeName("万达物业");
        receivable1.setPayeeAccount("6222021234567890");
        receivable1.setPayeeBank("中国银行");
        receivable1.setPayee("万达物业");
        receivable1.setPayerName("北京科技有限公司");
        receivable1.setTenantName("北京科技有限公司");
        receivable1.setContractNo("HT001");
        receivable1.setPayerAccount("6222021234567891");
        receivable1.setPayer("北京科技有限公司");
        receivable1.setPayerBankCode("104100000004");
        receivable1.setTransactionTime(LocalDateTime.now().minusDays(2));
        receivable1.setAmount(50000.00);
        receivable1.setPendingAmount(50000.00);
        receivable1.setInputDate(LocalDate.now().minusDays(2));
        receivable1.setDebitCreditFlag("CREDIT");
        receivable1.setSummary("租金收入");
        receivable1.setRemark("2024年1月租金");
        receivable1.setStatus(1);
        receivable1.setCreateTime(LocalDateTime.now().minusDays(2));
        receivable1.setUpdateTime(LocalDateTime.now().minusDays(2));
        receivableStorage.put(receivable1.getId(), receivable1);
        
        ReceivableAccount receivable2 = new ReceivableAccount();
        receivable2.setId(nextId++);
        receivable2.setBatchNo("B202401002");
        receivable2.setLineNo("002");
        receivable2.setProcessStatus("COMPLETED");
        receivable2.setCompany("中关村科技");
        receivable2.setProjectId(2L);
        receivable2.setPaymentMethod("网银");
        receivable2.setPayeeName("中关村物业");
        receivable2.setPayeeAccount("6222021234567892");
        receivable2.setPayeeBank("工商银行");
        receivable2.setPayee("中关村物业");
        receivable2.setPayerName("上海贸易公司");
        receivable2.setTenantName("上海贸易公司");
        receivable2.setContractNo("HT002");
        receivable2.setPayerAccount("6222021234567893");
        receivable2.setPayer("上海贸易公司");
        receivable2.setPayerBankCode("102100000001");
        receivable2.setTransactionTime(LocalDateTime.now().minusDays(1));
        receivable2.setAmount(35000.00);
        receivable2.setPendingAmount(0.00);
        receivable2.setInputDate(LocalDate.now().minusDays(1));
        receivable2.setClaimer("李经理");
        receivable2.setClaimDate(LocalDate.now().minusDays(1));
        receivable2.setDebitCreditFlag("CREDIT");
        receivable2.setSummary("租金收入");
        receivable2.setRemark("2024年2月租金");
        receivable2.setStatus(1);
        receivable2.setCreateTime(LocalDateTime.now().minusDays(1));
        receivable2.setUpdateTime(LocalDateTime.now().minusDays(1));
        receivableStorage.put(receivable2.getId(), receivable2);
    }
    
    @GetMapping("/page")
    public Map<String, Object> getReceivablePage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String processStatus,
            @RequestParam(required = false) String tenantName) {
        
        List<ReceivableAccount> allReceivables = new ArrayList<>(receivableStorage.values());
        
        // 过滤条件
        List<ReceivableAccount> filteredReceivables = allReceivables.stream()
                .filter(receivable -> batchNo == null || batchNo.isEmpty() || 
                        receivable.getBatchNo().contains(batchNo))
                .filter(receivable -> processStatus == null || processStatus.isEmpty() || 
                        receivable.getProcessStatus().equals(processStatus))
                .filter(receivable -> tenantName == null || tenantName.isEmpty() || 
                        (receivable.getTenantName() != null && receivable.getTenantName().contains(tenantName)))
                .filter(receivable -> receivable.getStatus() == 1)
                .toList();
        
        // 分页
        int total = filteredReceivables.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        List<ReceivableAccount> pageData = filteredReceivables.subList(start, end);
        
        // 转换为响应格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> records = pageData.stream().map(receivable -> {
            Map<String, Object> record = new HashMap<>();
            record.put("id", receivable.getId());
            record.put("batchNo", receivable.getBatchNo());
            record.put("lineNo", receivable.getLineNo());
            record.put("processStatus", receivable.getProcessStatus());
            record.put("company", receivable.getCompany());
            record.put("projectId", receivable.getProjectId());
            record.put("paymentMethod", receivable.getPaymentMethod());
            record.put("payeeName", receivable.getPayeeName());
            record.put("payeeAccount", receivable.getPayeeAccount());
            record.put("payeeBank", receivable.getPayeeBank());
            record.put("payee", receivable.getPayee());
            record.put("payerName", receivable.getPayerName());
            record.put("tenantName", receivable.getTenantName());
            record.put("contractNo", receivable.getContractNo());
            record.put("payerAccount", receivable.getPayerAccount());
            record.put("payer", receivable.getPayer());
            record.put("payerBankCode", receivable.getPayerBankCode());
            record.put("transactionTime", receivable.getTransactionTime() != null ? 
                    receivable.getTransactionTime().format(formatter) : null);
            record.put("amount", receivable.getAmount());
            record.put("pendingAmount", receivable.getPendingAmount());
            record.put("inputDate", receivable.getInputDate() != null ? 
                    receivable.getInputDate().format(dateFormatter) : null);
            record.put("claimer", receivable.getClaimer());
            record.put("claimDate", receivable.getClaimDate() != null ? 
                    receivable.getClaimDate().format(dateFormatter) : null);
            record.put("debitCreditFlag", receivable.getDebitCreditFlag());
            record.put("summary", receivable.getSummary());
            record.put("remark", receivable.getRemark());
            record.put("status", receivable.getStatus());
            record.put("createTime", receivable.getCreateTime().format(formatter));
            record.put("updateTime", receivable.getUpdateTime().format(formatter));
            return record;
        }).toList();
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", records);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", pageResult);
        
        return response;
    }
    
    @PostMapping
    public Map<String, Object> createReceivable(@RequestBody ReceivableAccount receivable) {
        receivable.setId(nextId++);
        receivable.setStatus(1);
        receivable.setCreateTime(LocalDateTime.now());
        receivable.setUpdateTime(LocalDateTime.now());
        
        receivableStorage.put(receivable.getId(), receivable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", receivable);
        
        return response;
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateReceivable(@PathVariable Long id, @RequestBody ReceivableAccount receivable) {
        ReceivableAccount existingReceivable = receivableStorage.get(id);
        if (existingReceivable == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "应收账款不存在");
            return response;
        }
        
        // 更新字段
        existingReceivable.setBatchNo(receivable.getBatchNo());
        existingReceivable.setLineNo(receivable.getLineNo());
        existingReceivable.setProcessStatus(receivable.getProcessStatus());
        existingReceivable.setCompany(receivable.getCompany());
        existingReceivable.setProjectId(receivable.getProjectId());
        existingReceivable.setPaymentMethod(receivable.getPaymentMethod());
        existingReceivable.setPayeeName(receivable.getPayeeName());
        existingReceivable.setPayeeAccount(receivable.getPayeeAccount());
        existingReceivable.setPayeeBank(receivable.getPayeeBank());
        existingReceivable.setPayee(receivable.getPayee());
        existingReceivable.setPayerName(receivable.getPayerName());
        existingReceivable.setTenantName(receivable.getTenantName());
        existingReceivable.setContractNo(receivable.getContractNo());
        existingReceivable.setPayerAccount(receivable.getPayerAccount());
        existingReceivable.setPayer(receivable.getPayer());
        existingReceivable.setPayerBankCode(receivable.getPayerBankCode());
        existingReceivable.setTransactionTime(receivable.getTransactionTime());
        existingReceivable.setAmount(receivable.getAmount());
        existingReceivable.setPendingAmount(receivable.getPendingAmount());
        existingReceivable.setInputDate(receivable.getInputDate());
        existingReceivable.setDebitCreditFlag(receivable.getDebitCreditFlag());
        existingReceivable.setSummary(receivable.getSummary());
        existingReceivable.setRemark(receivable.getRemark());
        existingReceivable.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", existingReceivable);
        
        return response;
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteReceivable(@PathVariable Long id) {
        ReceivableAccount receivable = receivableStorage.get(id);
        if (receivable == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "应收账款不存在");
            return response;
        }
        
        // 软删除
        receivable.setStatus(0);
        receivable.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        
        return response;
    }
    
    @PostMapping("/{id}/claim")
    public Map<String, Object> claimReceivable(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        ReceivableAccount receivable = receivableStorage.get(id);
        if (receivable == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "应收账款不存在");
            return response;
        }
        
        String claimer = (String) request.get("claimer");
        String claimDateStr = (String) request.get("claimDate");
        
        receivable.setClaimer(claimer);
        receivable.setClaimDate(LocalDate.parse(claimDateStr));
        receivable.setProcessStatus("COMPLETED");
        receivable.setPendingAmount(0.00); // 认领后待认领金额清零
        receivable.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "认领成功");
        response.put("data", receivable);
        
        return response;
    }
    
    // 应收账款实体类
    public static class ReceivableAccount {
        private Long id;
        private String batchNo;
        private String lineNo;
        private String processStatus; // PENDING, PROCESSING, COMPLETED, REJECTED
        private String company;
        private Long projectId;
        private String paymentMethod;
        private String payeeName;
        private String payeeAccount;
        private String payeeBank;
        private String payee;
        private String payerName;
        private String tenantName;
        private String contractNo;
        private String payerAccount;
        private String payer;
        private String payerBankCode;
        private LocalDateTime transactionTime;
        private Double amount;
        private Double pendingAmount;
        private LocalDate inputDate;
        private String claimer;
        private LocalDate claimDate;
        private String debitCreditFlag; // DEBIT, CREDIT
        private String summary;
        private String remark;
        private Integer status; // 1-正常 0-删除
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getBatchNo() { return batchNo; }
        public void setBatchNo(String batchNo) { this.batchNo = batchNo; }
        
        public String getLineNo() { return lineNo; }
        public void setLineNo(String lineNo) { this.lineNo = lineNo; }
        
        public String getProcessStatus() { return processStatus; }
        public void setProcessStatus(String processStatus) { this.processStatus = processStatus; }
        
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public String getPayeeName() { return payeeName; }
        public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
        
        public String getPayeeAccount() { return payeeAccount; }
        public void setPayeeAccount(String payeeAccount) { this.payeeAccount = payeeAccount; }
        
        public String getPayeeBank() { return payeeBank; }
        public void setPayeeBank(String payeeBank) { this.payeeBank = payeeBank; }
        
        public String getPayee() { return payee; }
        public void setPayee(String payee) { this.payee = payee; }
        
        public String getPayerName() { return payerName; }
        public void setPayerName(String payerName) { this.payerName = payerName; }
        
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        
        public String getContractNo() { return contractNo; }
        public void setContractNo(String contractNo) { this.contractNo = contractNo; }
        
        public String getPayerAccount() { return payerAccount; }
        public void setPayerAccount(String payerAccount) { this.payerAccount = payerAccount; }
        
        public String getPayer() { return payer; }
        public void setPayer(String payer) { this.payer = payer; }
        
        public String getPayerBankCode() { return payerBankCode; }
        public void setPayerBankCode(String payerBankCode) { this.payerBankCode = payerBankCode; }
        
        public LocalDateTime getTransactionTime() { return transactionTime; }
        public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }
        
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        
        public Double getPendingAmount() { return pendingAmount; }
        public void setPendingAmount(Double pendingAmount) { this.pendingAmount = pendingAmount; }
        
        public LocalDate getInputDate() { return inputDate; }
        public void setInputDate(LocalDate inputDate) { this.inputDate = inputDate; }
        
        public String getClaimer() { return claimer; }
        public void setClaimer(String claimer) { this.claimer = claimer; }
        
        public LocalDate getClaimDate() { return claimDate; }
        public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }
        
        public String getDebitCreditFlag() { return debitCreditFlag; }
        public void setDebitCreditFlag(String debitCreditFlag) { this.debitCreditFlag = debitCreditFlag; }
        
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        
        public LocalDateTime getUpdateTime() { return updateTime; }
        public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    }
}