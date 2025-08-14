package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/contract")
@CrossOrigin(origins = "*")
public class ContractController {
    
    // 模拟数据存储
    private static final Map<Long, Contract> contractStorage = new HashMap<>();
    private static Long nextId = 1L;
    
    static {
        // 初始化一些测试数据
        Contract contract1 = new Contract();
        contract1.setId(nextId++);
        contract1.setContractNo("HT001");
        contract1.setContractName("万达广场租赁合同");
        contract1.setProjectId(1L);
        contract1.setStartDate(LocalDate.of(2024, 1, 1));
        contract1.setEndDate(LocalDate.of(2024, 12, 31));
        contract1.setSignatory("张经理");
        contract1.setContractType("租赁合同");
        contract1.setContractStatus("ACTIVE");
        contract1.setRentBillCompany("万达物业");
        contract1.setPropertyBillCompany("万达物业");
        contract1.setLeaseNo("ZL001");
        contract1.setTenantId(1L);
        contract1.setTenantName("北京科技有限公司");
        contract1.setUnitId(1L);
        contract1.setUnitDescription("A栋1层101室");
        contract1.setRentMode("月付");
        contract1.setMonthlyRent(50000.00);
        contract1.setDeposit(150000.00);
        contract1.setStatus(1);
        contract1.setCreateTime(LocalDateTime.now().minusDays(10));
        contract1.setUpdateTime(LocalDateTime.now().minusDays(10));
        contractStorage.put(contract1.getId(), contract1);
        
        Contract contract2 = new Contract();
        contract2.setId(nextId++);
        contract2.setContractNo("HT002");
        contract2.setContractName("中关村写字楼租赁合同");
        contract2.setProjectId(2L);
        contract2.setStartDate(LocalDate.of(2024, 2, 1));
        contract2.setEndDate(LocalDate.of(2025, 1, 31));
        contract2.setSignatory("李经理");
        contract2.setContractType("租赁合同");
        contract2.setContractStatus("DRAFT");
        contract2.setRentBillCompany("中关村物业");
        contract2.setPropertyBillCompany("中关村物业");
        contract2.setLeaseNo("ZL002");
        contract2.setTenantId(2L);
        contract2.setTenantName("上海贸易公司");
        contract2.setUnitId(2L);
        contract2.setUnitDescription("B栋2层201室");
        contract2.setRentMode("季付");
        contract2.setMonthlyRent(35000.00);
        contract2.setDeposit(105000.00);
        contract2.setStatus(1);
        contract2.setCreateTime(LocalDateTime.now().minusDays(5));
        contract2.setUpdateTime(LocalDateTime.now().minusDays(5));
        contractStorage.put(contract2.getId(), contract2);
    }
    
    @GetMapping("/page")
    public Map<String, Object> getContractPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String contractStatus) {
        
        List<Contract> allContracts = new ArrayList<>(contractStorage.values());
        
        // 过滤条件
        List<Contract> filteredContracts = allContracts.stream()
                .filter(contract -> keyword == null || keyword.isEmpty() || 
                        contract.getContractNo().contains(keyword) || 
                        contract.getContractName().contains(keyword))
                .filter(contract -> contractStatus == null || contractStatus.isEmpty() || 
                        contract.getContractStatus().equals(contractStatus))
                .filter(contract -> contract.getStatus() == 1)
                .toList();
        
        // 分页
        int total = filteredContracts.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        List<Contract> pageData = filteredContracts.subList(start, end);
        
        // 转换为响应格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> records = pageData.stream().map(contract -> {
            Map<String, Object> record = new HashMap<>();
            record.put("id", contract.getId());
            record.put("contractNo", contract.getContractNo());
            record.put("contractName", contract.getContractName());
            record.put("projectId", contract.getProjectId());
            record.put("startDate", contract.getStartDate().format(dateFormatter));
            record.put("endDate", contract.getEndDate().format(dateFormatter));
            record.put("signatory", contract.getSignatory());
            record.put("contractType", contract.getContractType());
            record.put("contractStatus", contract.getContractStatus());
            record.put("rentBillCompany", contract.getRentBillCompany());
            record.put("propertyBillCompany", contract.getPropertyBillCompany());
            record.put("leaseNo", contract.getLeaseNo());
            record.put("tenantId", contract.getTenantId());
            record.put("tenantName", contract.getTenantName());
            record.put("unitId", contract.getUnitId());
            record.put("unitDescription", contract.getUnitDescription());
            record.put("rentMode", contract.getRentMode());
            record.put("monthlyRent", contract.getMonthlyRent());
            record.put("deposit", contract.getDeposit());
            record.put("status", contract.getStatus());
            record.put("createTime", contract.getCreateTime().format(formatter));
            record.put("updateTime", contract.getUpdateTime().format(formatter));
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
    public Map<String, Object> createContract(@RequestBody Contract contract) {
        contract.setId(nextId++);
        contract.setStatus(1);
        contract.setCreateTime(LocalDateTime.now());
        contract.setUpdateTime(LocalDateTime.now());
        
        contractStorage.put(contract.getId(), contract);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", contract);
        
        return response;
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Contract existingContract = contractStorage.get(id);
        if (existingContract == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "合同不存在");
            return response;
        }
        
        // 更新字段
        existingContract.setContractNo(contract.getContractNo());
        existingContract.setContractName(contract.getContractName());
        existingContract.setProjectId(contract.getProjectId());
        existingContract.setStartDate(contract.getStartDate());
        existingContract.setEndDate(contract.getEndDate());
        existingContract.setSignatory(contract.getSignatory());
        existingContract.setContractType(contract.getContractType());
        existingContract.setContractStatus(contract.getContractStatus());
        existingContract.setRentBillCompany(contract.getRentBillCompany());
        existingContract.setPropertyBillCompany(contract.getPropertyBillCompany());
        existingContract.setLeaseNo(contract.getLeaseNo());
        existingContract.setTenantId(contract.getTenantId());
        existingContract.setTenantName(contract.getTenantName());
        existingContract.setUnitId(contract.getUnitId());
        existingContract.setUnitDescription(contract.getUnitDescription());
        existingContract.setRentMode(contract.getRentMode());
        existingContract.setMonthlyRent(contract.getMonthlyRent());
        existingContract.setDeposit(contract.getDeposit());
        existingContract.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", existingContract);
        
        return response;
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteContract(@PathVariable Long id) {
        Contract contract = contractStorage.get(id);
        if (contract == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("message", "合同不存在");
            return response;
        }
        
        // 软删除
        contract.setStatus(0);
        contract.setUpdateTime(LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        
        return response;
    }
    
    // 合同实体类
    public static class Contract {
        private Long id;
        private String contractNo;
        private String contractName;
        private Long projectId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String signatory;
        private String contractType;
        private String contractStatus; // DRAFT, ACTIVE, EXPIRED, TERMINATED
        private String rentBillCompany;
        private String propertyBillCompany;
        private String leaseNo;
        private Long tenantId;
        private String tenantName;
        private Long unitId;
        private String unitDescription;
        private String rentMode;
        private Double monthlyRent;
        private Double deposit;
        private Integer status; // 1-正常 0-删除
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getContractNo() { return contractNo; }
        public void setContractNo(String contractNo) { this.contractNo = contractNo; }
        
        public String getContractName() { return contractName; }
        public void setContractName(String contractName) { this.contractName = contractName; }
        
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public String getSignatory() { return signatory; }
        public void setSignatory(String signatory) { this.signatory = signatory; }
        
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
        
        public String getContractStatus() { return contractStatus; }
        public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
        
        public String getRentBillCompany() { return rentBillCompany; }
        public void setRentBillCompany(String rentBillCompany) { this.rentBillCompany = rentBillCompany; }
        
        public String getPropertyBillCompany() { return propertyBillCompany; }
        public void setPropertyBillCompany(String propertyBillCompany) { this.propertyBillCompany = propertyBillCompany; }
        
        public String getLeaseNo() { return leaseNo; }
        public void setLeaseNo(String leaseNo) { this.leaseNo = leaseNo; }
        
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        
        public Long getUnitId() { return unitId; }
        public void setUnitId(Long unitId) { this.unitId = unitId; }
        
        public String getUnitDescription() { return unitDescription; }
        public void setUnitDescription(String unitDescription) { this.unitDescription = unitDescription; }
        
        public String getRentMode() { return rentMode; }
        public void setRentMode(String rentMode) { this.rentMode = rentMode; }
        
        public Double getMonthlyRent() { return monthlyRent; }
        public void setMonthlyRent(Double monthlyRent) { this.monthlyRent = monthlyRent; }
        
        public Double getDeposit() { return deposit; }
        public void setDeposit(Double deposit) { this.deposit = deposit; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        
        public LocalDateTime getUpdateTime() { return updateTime; }
        public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    }
}