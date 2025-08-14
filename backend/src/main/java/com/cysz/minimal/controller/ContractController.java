package com.cysz.minimal.controller;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 合同管理控制器
 */
@RestController
@RequestMapping("/contract")
@CrossOrigin(origins = "*")
public class ContractController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 合同分页查询
     */
    @GetMapping("/page")
    public Result<PageResult<ContractVO>> getContractPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String contractName,
            @RequestParam(required = false) String tenantName,
            @RequestParam(required = false) String contractStatus,
            @RequestParam(required = false) Long projectId) {
        
        // 模拟数据
        ContractVO contract = new ContractVO();
        contract.setId(1L);
        contract.setContractNo("HT202501001");
        contract.setContractName("测试商铺租赁合同");
        contract.setContractType("商铺");
        contract.setProjectId(1L);
        contract.setProjectName("测试项目");
        contract.setSignatory("张三");
        contract.setSignatoryPhone("13800138000");
        contract.setTenantId(1L);
        contract.setTenantName("测试租户");
        contract.setBusinessBrand("测试品牌");
        contract.setShopSign("测试店招");
        contract.setBusinessFormat("餐饮");
        contract.setSignDate(LocalDate.now());
        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusYears(1));
        contract.setBuildingArea(new BigDecimal("120.50"));
        contract.setRentableArea(new BigDecimal("115.00"));
        contract.setContractArea(new BigDecimal("115.00"));
        contract.setDepositAmount(new BigDecimal("10000.00"));
        contract.setDepositLatestDate(LocalDate.now().plusDays(30));
        contract.setFeeCompany("测试费项公司");
        contract.setRentMode("固定租金");
        contract.setRentPeriodSetting("月末截止计租周期");
        contract.setLateFeeRule("5‰/日");
        contract.setPaymentAccount("1234567890");
        contract.setPaymentFrequency("月");
        contract.setLatestPaymentDay(5);
        contract.setFirstPaymentLatestDate(LocalDate.now().plusDays(15));
        contract.setFirstPeriodRent(new BigDecimal("8000.00"));
        contract.setPeriodRent(new BigDecimal("8000.00"));
        contract.setContractStatus("ACTIVE");
        contract.setStatus(1);
        contract.setCreateTime(LocalDateTime.now());
        contract.setUpdateTime(LocalDateTime.now());

        PageResult<ContractVO> pageResult = new PageResult<>();
        pageResult.setRecords(List.of(contract));
        pageResult.setTotal(1L);
        pageResult.setCurrent(page);
        pageResult.setSize(size);

        return Result.success(pageResult);
    }

    /**
     * 创建合同
     */
    @PostMapping
    public Result<Long> createContract(@RequestBody ContractCreateDTO dto) {
        System.out.println("创建合同: " + dto);
        
        try {
            // 验证必填字段
            if (dto.getContractNo() == null || dto.getContractNo().trim().isEmpty()) {
                return Result.error("合同编号不能为空");
            }
            if (dto.getContractName() == null || dto.getContractName().trim().isEmpty()) {
                return Result.error("合同名称不能为空");
            }
            if (dto.getProjectId() == null) {
                return Result.error("项目ID不能为空");
            }
            if (dto.getUnitIds() == null || dto.getUnitIds().isEmpty()) {
                return Result.error("单元ID不能为空");
            }
            
            // 检查合同编号是否已存在
            String checkSql = "SELECT COUNT(*) FROM contract WHERE contract_number = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{dto.getContractNo()}, Integer.class);
            if (count != null && count > 0) {
                return Result.error("合同编号已存在");
            }
            
            // 获取第一个单元ID作为主单元（兼容现有表结构）
            Long primaryUnitId = dto.getUnitIds().get(0);
            
            // 获取租户ID（如果没有提供，创建默认租户）
            Long tenantId = dto.getTenantId();
            if (tenantId == null) {
                // 创建默认租户
                String insertTenantSql = "INSERT INTO tenant (tenant_name, tenant_nature, status, create_time) VALUES (?, 'individual', 1, NOW())";
                jdbcTemplate.update(insertTenantSql, dto.getTenantName() != null ? dto.getTenantName() : "默认租户");
                
                // 获取新创建的租户ID
                String getLastIdSql = "SELECT LAST_INSERT_ID()";
                tenantId = jdbcTemplate.queryForObject(getLastIdSql, Long.class);
            }
            
            // 插入合同数据
            String insertSql = "INSERT INTO contract (contract_number, project_id, unit_id, tenant_id, " +
                              "contract_type, start_date, end_date, monthly_rent, deposit, " +
                              "payment_method, payment_cycle, status, notes, create_time, update_time) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
            
            jdbcTemplate.update(insertSql,
                dto.getContractNo(),
                dto.getProjectId(),
                primaryUnitId,
                tenantId,
                dto.getContractType() != null ? dto.getContractType() : "RENT",
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getFirstPeriodRent(),
                dto.getDepositAmount(),
                dto.getPaymentAccount(),
                dto.getPaymentFrequency() != null ? dto.getPaymentFrequency() : "MONTHLY",
                "DRAFT", // 默认状态为草稿
                String.format("合同名称: %s, 签订人: %s, 业态: %s", 
                    dto.getContractName(), 
                    dto.getSignatory(), 
                    dto.getBusinessFormat())
            );
            
            // 获取新创建的合同ID
            String getContractIdSql = "SELECT LAST_INSERT_ID()";
            Long contractId = jdbcTemplate.queryForObject(getContractIdSql, Long.class);
            
            System.out.println("合同创建成功，ID: " + contractId);
            
            return Result.success(contractId);
            
        } catch (Exception e) {
            System.err.println("创建合同失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("创建合同失败: " + e.getMessage());
        }
    }

    /**
     * 更新合同
     */
    @PutMapping("/{id}")
    public Result<Void> updateContract(@PathVariable Long id, @RequestBody ContractUpdateDTO dto) {
        // TODO: 实现合同更新逻辑
        return Result.success();
    }

    /**
     * 删除合同
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteContract(@PathVariable Long id) {
        // TODO: 实现合同删除逻辑
        return Result.success();
    }

    /**
     * 获取合同详情
     */
    @GetMapping("/{id}")
    public Result<ContractVO> getContract(@PathVariable Long id) {
        // TODO: 实现获取合同详情逻辑
        ContractVO contract = new ContractVO();
        contract.setId(id);
        contract.setContractNo("HT202501001");
        contract.setContractName("测试商铺租赁合同");
        return Result.success(contract);
    }

    /**
     * 合同VO类
     */
    public static class ContractVO {
        private Long id;
        private String contractNo;
        private String contractName;
        private String contractType;
        private Long projectId;
        private String projectName;
        private String signatory;
        private String signatoryPhone;
        private Long tenantId;
        private String tenantName;
        private String businessBrand;
        private String shopSign;
        private String businessFormat;
        private LocalDate signDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<Long> buildingIds;
        private List<Long> floorIds;
        private List<Long> unitIds;
        private BigDecimal buildingArea;
        private BigDecimal rentableArea;
        private BigDecimal contractArea;
        private BigDecimal depositAmount;
        private LocalDate depositLatestDate;
        private String feeCompany;
        private String rentMode;
        private String rentPeriodSetting;
        private String lateFeeRule;
        private String paymentAccount;
        private String paymentFrequency;
        private Integer latestPaymentDay;
        private LocalDate firstPaymentLatestDate;
        private BigDecimal firstPeriodRent;
        private BigDecimal periodRent;
        private List<CommissionRuleVO> commissionRules;
        private String contractStatus;
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
        
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
        
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        
        public String getProjectName() { return projectName; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
        
        public String getSignatory() { return signatory; }
        public void setSignatory(String signatory) { this.signatory = signatory; }
        
        public String getSignatoryPhone() { return signatoryPhone; }
        public void setSignatoryPhone(String signatoryPhone) { this.signatoryPhone = signatoryPhone; }
        
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        
        public String getBusinessBrand() { return businessBrand; }
        public void setBusinessBrand(String businessBrand) { this.businessBrand = businessBrand; }
        
        public String getShopSign() { return shopSign; }
        public void setShopSign(String shopSign) { this.shopSign = shopSign; }
        
        public String getBusinessFormat() { return businessFormat; }
        public void setBusinessFormat(String businessFormat) { this.businessFormat = businessFormat; }
        
        public LocalDate getSignDate() { return signDate; }
        public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public List<Long> getBuildingIds() { return buildingIds; }
        public void setBuildingIds(List<Long> buildingIds) { this.buildingIds = buildingIds; }
        
        public List<Long> getFloorIds() { return floorIds; }
        public void setFloorIds(List<Long> floorIds) { this.floorIds = floorIds; }
        
        public List<Long> getUnitIds() { return unitIds; }
        public void setUnitIds(List<Long> unitIds) { this.unitIds = unitIds; }
        
        public BigDecimal getBuildingArea() { return buildingArea; }
        public void setBuildingArea(BigDecimal buildingArea) { this.buildingArea = buildingArea; }
        
        public BigDecimal getRentableArea() { return rentableArea; }
        public void setRentableArea(BigDecimal rentableArea) { this.rentableArea = rentableArea; }
        
        public BigDecimal getContractArea() { return contractArea; }
        public void setContractArea(BigDecimal contractArea) { this.contractArea = contractArea; }
        
        public BigDecimal getDepositAmount() { return depositAmount; }
        public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
        
        public LocalDate getDepositLatestDate() { return depositLatestDate; }
        public void setDepositLatestDate(LocalDate depositLatestDate) { this.depositLatestDate = depositLatestDate; }
        
        public String getFeeCompany() { return feeCompany; }
        public void setFeeCompany(String feeCompany) { this.feeCompany = feeCompany; }
        
        public String getRentMode() { return rentMode; }
        public void setRentMode(String rentMode) { this.rentMode = rentMode; }
        
        public String getRentPeriodSetting() { return rentPeriodSetting; }
        public void setRentPeriodSetting(String rentPeriodSetting) { this.rentPeriodSetting = rentPeriodSetting; }
        
        public String getLateFeeRule() { return lateFeeRule; }
        public void setLateFeeRule(String lateFeeRule) { this.lateFeeRule = lateFeeRule; }
        
        public String getPaymentAccount() { return paymentAccount; }
        public void setPaymentAccount(String paymentAccount) { this.paymentAccount = paymentAccount; }
        
        public String getPaymentFrequency() { return paymentFrequency; }
        public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
        
        public Integer getLatestPaymentDay() { return latestPaymentDay; }
        public void setLatestPaymentDay(Integer latestPaymentDay) { this.latestPaymentDay = latestPaymentDay; }
        
        public LocalDate getFirstPaymentLatestDate() { return firstPaymentLatestDate; }
        public void setFirstPaymentLatestDate(LocalDate firstPaymentLatestDate) { this.firstPaymentLatestDate = firstPaymentLatestDate; }
        
        public BigDecimal getFirstPeriodRent() { return firstPeriodRent; }
        public void setFirstPeriodRent(BigDecimal firstPeriodRent) { this.firstPeriodRent = firstPeriodRent; }
        
        public BigDecimal getPeriodRent() { return periodRent; }
        public void setPeriodRent(BigDecimal periodRent) { this.periodRent = periodRent; }
        
        public List<CommissionRuleVO> getCommissionRules() { return commissionRules; }
        public void setCommissionRules(List<CommissionRuleVO> commissionRules) { this.commissionRules = commissionRules; }
        
        public String getContractStatus() { return contractStatus; }
        public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
        
        public LocalDateTime getUpdateTime() { return updateTime; }
        public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    }

    /**
     * 提成规则VO类
     */
    public static class CommissionRuleVO {
        private Long id;
        private Long contractId;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private BigDecimal commissionRate;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public Long getContractId() { return contractId; }
        public void setContractId(Long contractId) { this.contractId = contractId; }
        
        public BigDecimal getMinAmount() { return minAmount; }
        public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
        
        public BigDecimal getMaxAmount() { return maxAmount; }
        public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
        
        public BigDecimal getCommissionRate() { return commissionRate; }
        public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    }

    /**
     * 合同创建DTO
     */
    public static class ContractCreateDTO {
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
        private LocalDate startDate;
        private LocalDate endDate;
        private List<Long> buildingIds;
        private List<Long> floorIds;
        private List<Long> unitIds;
        private BigDecimal buildingArea;
        private BigDecimal rentableArea;
        private BigDecimal contractArea;
        private BigDecimal depositAmount;
        private LocalDate depositLatestDate;
        private String feeCompany;
        private String rentMode;
        private String rentPeriodSetting;
        private String lateFeeRule;
        private String paymentAccount;
        private String paymentFrequency;
        private Integer latestPaymentDay;
        private LocalDate firstPaymentLatestDate;
        private BigDecimal firstPeriodRent;
        private BigDecimal periodRent;
        private List<CommissionRuleDTO> commissionRules;

        // Getters and Setters (省略，与VO类似)
        public String getContractNo() { return contractNo; }
        public void setContractNo(String contractNo) { this.contractNo = contractNo; }
        
        public String getContractName() { return contractName; }
        public void setContractName(String contractName) { this.contractName = contractName; }
        
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
        
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        
        public String getSignatory() { return signatory; }
        public void setSignatory(String signatory) { this.signatory = signatory; }
        
        public String getSignatoryPhone() { return signatoryPhone; }
        public void setSignatoryPhone(String signatoryPhone) { this.signatoryPhone = signatoryPhone; }
        
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        
        public String getBusinessBrand() { return businessBrand; }
        public void setBusinessBrand(String businessBrand) { this.businessBrand = businessBrand; }
        
        public String getShopSign() { return shopSign; }
        public void setShopSign(String shopSign) { this.shopSign = shopSign; }
        
        public String getBusinessFormat() { return businessFormat; }
        public void setBusinessFormat(String businessFormat) { this.businessFormat = businessFormat; }
        
        public LocalDate getSignDate() { return signDate; }
        public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public List<Long> getBuildingIds() { return buildingIds; }
        public void setBuildingIds(List<Long> buildingIds) { this.buildingIds = buildingIds; }
        
        public List<Long> getFloorIds() { return floorIds; }
        public void setFloorIds(List<Long> floorIds) { this.floorIds = floorIds; }
        
        public List<Long> getUnitIds() { return unitIds; }
        public void setUnitIds(List<Long> unitIds) { this.unitIds = unitIds; }
        
        public BigDecimal getBuildingArea() { return buildingArea; }
        public void setBuildingArea(BigDecimal buildingArea) { this.buildingArea = buildingArea; }
        
        public BigDecimal getRentableArea() { return rentableArea; }
        public void setRentableArea(BigDecimal rentableArea) { this.rentableArea = rentableArea; }
        
        public BigDecimal getContractArea() { return contractArea; }
        public void setContractArea(BigDecimal contractArea) { this.contractArea = contractArea; }
        
        public BigDecimal getDepositAmount() { return depositAmount; }
        public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
        
        public LocalDate getDepositLatestDate() { return depositLatestDate; }
        public void setDepositLatestDate(LocalDate depositLatestDate) { this.depositLatestDate = depositLatestDate; }
        
        public String getFeeCompany() { return feeCompany; }
        public void setFeeCompany(String feeCompany) { this.feeCompany = feeCompany; }
        
        public String getRentMode() { return rentMode; }
        public void setRentMode(String rentMode) { this.rentMode = rentMode; }
        
        public String getRentPeriodSetting() { return rentPeriodSetting; }
        public void setRentPeriodSetting(String rentPeriodSetting) { this.rentPeriodSetting = rentPeriodSetting; }
        
        public String getLateFeeRule() { return lateFeeRule; }
        public void setLateFeeRule(String lateFeeRule) { this.lateFeeRule = lateFeeRule; }
        
        public String getPaymentAccount() { return paymentAccount; }
        public void setPaymentAccount(String paymentAccount) { this.paymentAccount = paymentAccount; }
        
        public String getPaymentFrequency() { return paymentFrequency; }
        public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
        
        public Integer getLatestPaymentDay() { return latestPaymentDay; }
        public void setLatestPaymentDay(Integer latestPaymentDay) { this.latestPaymentDay = latestPaymentDay; }
        
        public LocalDate getFirstPaymentLatestDate() { return firstPaymentLatestDate; }
        public void setFirstPaymentLatestDate(LocalDate firstPaymentLatestDate) { this.firstPaymentLatestDate = firstPaymentLatestDate; }
        
        public BigDecimal getFirstPeriodRent() { return firstPeriodRent; }
        public void setFirstPeriodRent(BigDecimal firstPeriodRent) { this.firstPeriodRent = firstPeriodRent; }
        
        public BigDecimal getPeriodRent() { return periodRent; }
        public void setPeriodRent(BigDecimal periodRent) { this.periodRent = periodRent; }
        
        public List<CommissionRuleDTO> getCommissionRules() { return commissionRules; }
        public void setCommissionRules(List<CommissionRuleDTO> commissionRules) { this.commissionRules = commissionRules; }
    }

    /**
     * 合同更新DTO
     */
    public static class ContractUpdateDTO extends ContractCreateDTO {
        // 继承创建DTO的所有字段
    }

    /**
     * 提成规则DTO
     */
    public static class CommissionRuleDTO {
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private BigDecimal commissionRate;

        // Getters and Setters
        public BigDecimal getMinAmount() { return minAmount; }
        public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
        
        public BigDecimal getMaxAmount() { return maxAmount; }
        public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
        
        public BigDecimal getCommissionRate() { return commissionRate; }
        public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    }
}