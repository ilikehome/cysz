package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountManagementController {
    
    // 模拟数据存储
    private static final Map<Long, ReceivableRecord> receivableStorage = new HashMap<>();
    private static final Map<Long, ReceivedRecord> receivedStorage = new HashMap<>();
    private static final Map<Long, BankTransaction> bankTransactionStorage = new HashMap<>();
    private static Long nextReceivableId = 1L;
    private static Long nextReceivedId = 1L;
    private static Long nextBankTransactionId = 1L;
    
    static {
        initTestData();
    }
    
    // 应收已收管理
    @GetMapping("/receivable-received/page")
    public Map<String, Object> getReceivableReceivedPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String tenantName) {
        
        List<Map<String, Object>> allRecords = new ArrayList<>();
        
        // 合并应收和已收数据
        for (ReceivableRecord receivable : receivableStorage.values()) {
            if (receivable.getStatus() != 1) continue;
            
            Map<String, Object> record = new HashMap<>();
            record.put("id", "R" + receivable.getId());
            record.put("type", "RECEIVABLE");
            record.put("contractNo", receivable.getContractNo());
            record.put("contractName", receivable.getContractName());
            record.put("tenantName", receivable.getTenantName());
            record.put("unitDescription", receivable.getUnitDescription());
            record.put("billType", receivable.getBillType());
            record.put("amount", receivable.getAmount());
            record.put("dueDate", receivable.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            record.put("billStatus", receivable.getBillStatus());
            record.put("createTime", receivable.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            record.put("matchedAmount", receivable.getMatchedAmount());
            record.put("remainingAmount", receivable.getAmount().subtract(receivable.getMatchedAmount()));
            
            allRecords.add(record);
        }
        
        for (ReceivedRecord received : receivedStorage.values()) {
            if (received.getStatus() != 1) continue;
            
            Map<String, Object> record = new HashMap<>();
            record.put("id", "P" + received.getId());
            record.put("type", "RECEIVED");
            record.put("contractNo", received.getContractNo());
            record.put("contractName", received.getContractName());
            record.put("tenantName", received.getTenantName());
            record.put("unitDescription", received.getUnitDescription());
            record.put("billType", received.getBillType());
            record.put("amount", received.getAmount());
            record.put("receivedDate", received.getReceivedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            record.put("billStatus", received.getBillStatus());
            record.put("createTime", received.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            record.put("bankTransactionNo", received.getBankTransactionNo());
            record.put("paymentMethod", received.getPaymentMethod());
            
            allRecords.add(record);
        }
        
        // 过滤
        List<Map<String, Object>> filteredRecords = allRecords.stream()
                .filter(record -> keyword == null || keyword.isEmpty() || 
                        record.get("contractNo").toString().contains(keyword) ||
                        record.get("tenantName").toString().contains(keyword))
                .filter(record -> status == null || status.isEmpty() || 
                        record.get("billStatus").toString().equals(status))
                .filter(record -> contractNo == null || contractNo.isEmpty() || 
                        record.get("contractNo").toString().contains(contractNo))
                .filter(record -> tenantName == null || tenantName.isEmpty() || 
                        record.get("tenantName").toString().contains(tenantName))
                .toList();
        
        // 分页
        int total = filteredRecords.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        List<Map<String, Object>> pageData = filteredRecords.subList(start, end);
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", pageData);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", pageResult);
        
        return response;
    }
    
    // 收款认领管理
    @GetMapping("/payment-claim/page")
    public Map<String, Object> getPaymentClaimPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String claimStatus) {
        
        List<BankTransaction> allTransactions = new ArrayList<>(bankTransactionStorage.values());
        
        // 过滤条件
        List<BankTransaction> filteredTransactions = allTransactions.stream()
                .filter(transaction -> keyword == null || keyword.isEmpty() || 
                        transaction.getTransactionNo().contains(keyword) ||
                        (transaction.getPayerName() != null && transaction.getPayerName().contains(keyword)))
                .filter(transaction -> claimStatus == null || claimStatus.isEmpty() || 
                        transaction.getClaimStatus().equals(claimStatus))
                .filter(transaction -> transaction.getStatus() == 1)
                .toList();
        
        // 分页
        int total = filteredTransactions.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        List<BankTransaction> pageData = filteredTransactions.subList(start, end);
        
        // 转换为响应格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> records = pageData.stream().map(transaction -> {
            Map<String, Object> record = new HashMap<>();
            record.put("id", transaction.getId());
            record.put("transactionNo", transaction.getTransactionNo());
            record.put("amount", transaction.getAmount());
            record.put("transactionDate", transaction.getTransactionDate().format(dateFormatter));
            record.put("payerName", transaction.getPayerName());
            record.put("payerAccount", transaction.getPayerAccount());
            record.put("receiverAccount", transaction.getReceiverAccount());
            record.put("transactionType", transaction.getTransactionType());
            record.put("claimStatus", transaction.getClaimStatus());
            record.put("claimedContractNo", transaction.getClaimedContractNo());
            record.put("claimedTenantName", transaction.getClaimedTenantName());
            record.put("claimTime", transaction.getClaimTime() != null ? 
                    transaction.getClaimTime().format(formatter) : null);
            record.put("claimOperator", transaction.getClaimOperator());
            record.put("remark", transaction.getRemark());
            record.put("createTime", transaction.getCreateTime().format(formatter));
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
    
    // 执行收款认领
    @PostMapping("/payment-claim/{transactionId}/claim")
    public Map<String, Object> claimPayment(
            @PathVariable Long transactionId,
            @RequestBody Map<String, Object> claimData) {
        
        BankTransaction transaction = bankTransactionStorage.get(transactionId);
        if (transaction == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "银行流水不存在");
            return response;
        }
        
        if (!"UNCLAIMED".equals(transaction.getClaimStatus())) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 400);
            response.put("message", "该流水已被认领");
            return response;
        }
        
        // 更新认领信息
        transaction.setClaimStatus("CLAIMED");
        transaction.setClaimedContractNo((String) claimData.get("contractNo"));
        transaction.setClaimedTenantName((String) claimData.get("tenantName"));
        transaction.setClaimTime(LocalDateTime.now());
        transaction.setClaimOperator((String) claimData.get("operator"));
        transaction.setRemark((String) claimData.get("remark"));
        
        // 创建已收记录
        ReceivedRecord received = new ReceivedRecord();
        received.setId(nextReceivedId++);
        received.setContractNo(transaction.getClaimedContractNo());
        received.setContractName((String) claimData.get("contractName"));
        received.setTenantName(transaction.getClaimedTenantName());
        received.setUnitDescription((String) claimData.get("unitDescription"));
        received.setBillType((String) claimData.get("billType"));
        received.setAmount(transaction.getAmount());
        received.setReceivedDate(transaction.getTransactionDate());
        received.setBillStatus("RECEIVED");
        received.setBankTransactionNo(transaction.getTransactionNo());
        received.setPaymentMethod("银行转账");
        received.setStatus(1);
        received.setCreateTime(LocalDateTime.now());
        received.setUpdateTime(LocalDateTime.now());
        
        receivedStorage.put(received.getId(), received);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "认领成功");
        response.put("data", received);
        
        return response;
    }
    
    // 获取可认领的合同列表
    @GetMapping("/contracts/claimable")
    public Map<String, Object> getClaimableContracts() {
        // 这里应该从合同服务获取，暂时模拟数据
        List<Map<String, Object>> contracts = Arrays.asList(
            Map.of("contractNo", "HT001", "contractName", "万达广场租赁合同", 
                   "tenantName", "北京科技有限公司", "unitDescription", "A栋1层101室"),
            Map.of("contractNo", "HT002", "contractName", "中关村写字楼租赁合同", 
                   "tenantName", "上海贸易公司", "unitDescription", "B栋2层201室")
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", contracts);
        
        return response;
    }
    
    // 生成应收账款（基于合同账期）
    @PostMapping("/receivable/generate")
    public Map<String, Object> generateReceivables(@RequestBody Map<String, Object> params) {
        String contractNo = (String) params.get("contractNo");
        String billType = (String) params.get("billType");
        
        // 模拟生成应收账款
        ReceivableRecord receivable = new ReceivableRecord();
        receivable.setId(nextReceivableId++);
        receivable.setContractNo(contractNo);
        receivable.setContractName("合同名称");
        receivable.setTenantName("租户名称");
        receivable.setUnitDescription("单元描述");
        receivable.setBillType(billType);
        receivable.setAmount(new BigDecimal("50000.00"));
        receivable.setDueDate(LocalDate.now().plusDays(30));
        receivable.setBillStatus("PENDING");
        receivable.setMatchedAmount(BigDecimal.ZERO);
        receivable.setStatus(1);
        receivable.setCreateTime(LocalDateTime.now());
        receivable.setUpdateTime(LocalDateTime.now());
        
        receivableStorage.put(receivable.getId(), receivable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "生成成功");
        response.put("data", receivable);
        
        return response;
    }
    
    // 初始化测试数据
    private static void initTestData() {
        // 应收账款测试数据
        ReceivableRecord receivable1 = new ReceivableRecord();
        receivable1.setId(nextReceivableId++);
        receivable1.setContractNo("HT001");
        receivable1.setContractName("万达广场租赁合同");
        receivable1.setTenantName("北京科技有限公司");
        receivable1.setUnitDescription("A栋1层101室");
        receivable1.setBillType("租金");
        receivable1.setAmount(new BigDecimal("50000.00"));
        receivable1.setDueDate(LocalDate.now().plusDays(15));
        receivable1.setBillStatus("PENDING");
        receivable1.setMatchedAmount(BigDecimal.ZERO);
        receivable1.setStatus(1);
        receivable1.setCreateTime(LocalDateTime.now().minusDays(5));
        receivable1.setUpdateTime(LocalDateTime.now().minusDays(5));
        receivableStorage.put(receivable1.getId(), receivable1);
        
        // 已收账款测试数据
        ReceivedRecord received1 = new ReceivedRecord();
        received1.setId(nextReceivedId++);
        received1.setContractNo("HT002");
        received1.setContractName("中关村写字楼租赁合同");
        received1.setTenantName("上海贸易公司");
        received1.setUnitDescription("B栋2层201室");
        received1.setBillType("租金");
        received1.setAmount(new BigDecimal("35000.00"));
        received1.setReceivedDate(LocalDate.now().minusDays(2));
        received1.setBillStatus("RECEIVED");
        received1.setBankTransactionNo("TXN20240809001");
        received1.setPaymentMethod("银行转账");
        received1.setStatus(1);
        received1.setCreateTime(LocalDateTime.now().minusDays(2));
        received1.setUpdateTime(LocalDateTime.now().minusDays(2));
        receivedStorage.put(received1.getId(), received1);
        
        // 银行流水测试数据
        BankTransaction transaction1 = new BankTransaction();
        transaction1.setId(nextBankTransactionId++);
        transaction1.setTransactionNo("TXN20240809002");
        transaction1.setAmount(new BigDecimal("50000.00"));
        transaction1.setTransactionDate(LocalDate.now());
        transaction1.setPayerName("北京科技有限公司");
        transaction1.setPayerAccount("6222021234567890");
        transaction1.setReceiverAccount("6222020987654321");
        transaction1.setTransactionType("转账");
        transaction1.setClaimStatus("UNCLAIMED");
        transaction1.setStatus(1);
        transaction1.setCreateTime(LocalDateTime.now());
        bankTransactionStorage.put(transaction1.getId(), transaction1);
        
        BankTransaction transaction2 = new BankTransaction();
        transaction2.setId(nextBankTransactionId++);
        transaction2.setTransactionNo("TXN20240809003");
        transaction2.setAmount(new BigDecimal("25000.00"));
        transaction2.setTransactionDate(LocalDate.now().minusDays(1));
        transaction2.setPayerName("未知付款方");
        transaction2.setPayerAccount("6222021111111111");
        transaction2.setReceiverAccount("6222020987654321");
        transaction2.setTransactionType("转账");
        transaction2.setClaimStatus("UNCLAIMED");
        transaction2.setStatus(1);
        transaction2.setCreateTime(LocalDateTime.now().minusDays(1));
        bankTransactionStorage.put(transaction2.getId(), transaction2);
    }
    
    // 应收记录实体类
    public static class ReceivableRecord {
        private Long id;
        private String contractNo;
        private String contractName;
        private String tenantName;
        private String unitDescription;
        private String billType; // 租金、物业费、水电费等
        private BigDecimal amount;
        private LocalDate dueDate;
        private String billStatus; // PENDING, OVERDUE, PARTIAL, COMPLETED
        private BigDecimal matchedAmount; // 已匹配金额
        private Integer status;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getContractNo() { return contractNo; }
        public void setContractNo(String contractNo) { this.contractNo = contractNo; }
        public String getContractName() { return contractName; }
        public void setContractName(String contractName) { this.contractName = contractName; }
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        public String getUnitDescription() { return unitDescription; }
        public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }
        public String getBillType() { return billType; }
        public void setBillType(String billType) { this.billType = billType; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
        public String getBillStatus() { return billStatus; }
        public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
        public BigDecimal getMatchedAmount() { return matchedAmount; }
        public void setMatchedAmount(BigDecimal matchedAmount) { this.matchedAmount = matchedAmount; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        public LocalDateTime getUpdateTime() { return updateTime; }
        public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    }
    
    // 已收记录实体类
    public static class ReceivedRecord {
        private Long id;
        private String contractNo;
        private String contractName;
        private String tenantName;
        private String unitDescription;
        private String billType;
        private BigDecimal amount;
        private LocalDate receivedDate;
        private String billStatus;
        private String bankTransactionNo;
        private String paymentMethod;
        private Integer status;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getContractNo() { return contractNo; }
        public void setContractNo(String contractNo) { this.contractNo = contractNo; }
        public String getContractName() { return contractName; }
        public void setContractName(String contractName) { this.contractName = contractName; }
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        public String getUnitDescription() { return unitDescription; }
        public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }
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
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        public LocalDateTime getUpdateTime() { return updateTime; }
        public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    }
    
    // 银行流水实体类
    public static class BankTransaction {
        private Long id;
        private String transactionNo;
        private BigDecimal amount;
        private LocalDate transactionDate;
        private String payerName;
        private String payerAccount;
        private String receiverAccount;
        private String transactionType;
        private String claimStatus; // UNCLAIMED, CLAIMED, REJECTED
        private String claimedContractNo;
        private String claimedTenantName;
        private LocalDateTime claimTime;
        private String claimOperator;
        private String remark;
        private Integer status;
        private LocalDateTime createTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTransactionNo() { return transactionNo; }
        public void setTransactionNo(String transactionNo) { this.transactionNo = transactionNo; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public LocalDate getTransactionDate() { return transactionDate; }
        public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
        public String getPayerName() { return payerName; }
        public void setPayerName(String payerName) { this.payerName = payerName; }
        public String getPayerAccount() { return payerAccount; }
        public void setPayerAccount(String payerAccount) { this.payerAccount = payerAccount; }
        public String getReceiverAccount() { return receiverAccount; }
        public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }
        public String getTransactionType() { return transactionType; }
        public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
        public String getClaimStatus() { return claimStatus; }
        public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
        public String getClaimedContractNo() { return claimedContractNo; }
        public void setClaimedContractNo(String claimedContractNo) { this.claimedContractNo = claimedContractNo; }
        public String getClaimedTenantName() { return claimedTenantName; }
        public void setClaimedTenantName(String claimedTenantName) { this.claimedTenantName = claimedTenantName; }
        public LocalDateTime getClaimTime() { return claimTime; }
        public void setClaimTime(LocalDateTime claimTime) { this.claimTime = claimTime; }
        public String getClaimOperator() { return claimOperator; }
        public void setClaimOperator(String claimOperator) { this.claimOperator = claimOperator; }
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    }
}