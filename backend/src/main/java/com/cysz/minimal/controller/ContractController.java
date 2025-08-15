package com.cysz.minimal.controller;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.common.Result;
import com.cysz.minimal.enums.ContractType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        
        try {
            // 构建查询条件
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT c.*, p.project_name, t.tenant_name as actual_tenant_name FROM contract c ");
            sqlBuilder.append("LEFT JOIN project p ON c.project_id = p.id ");
            sqlBuilder.append("LEFT JOIN tenant t ON c.tenant_id = t.id ");
            sqlBuilder.append("WHERE 1=1 ");
            
            List<Object> params = new ArrayList<>();
            
            if (contractNo != null && !contractNo.trim().isEmpty()) {
                sqlBuilder.append("AND c.contract_no LIKE ? ");
                params.add("%" + contractNo.trim() + "%");
            }
            
            if (contractName != null && !contractName.trim().isEmpty()) {
                sqlBuilder.append("AND c.contract_name LIKE ? ");
                params.add("%" + contractName.trim() + "%");
            }
            
            if (tenantName != null && !tenantName.trim().isEmpty()) {
                sqlBuilder.append("AND (c.tenant_name LIKE ? OR t.tenant_name LIKE ?) ");
                params.add("%" + tenantName.trim() + "%");
                params.add("%" + tenantName.trim() + "%");
            }
            
            if (contractStatus != null && !contractStatus.trim().isEmpty()) {
                sqlBuilder.append("AND c.contract_status = ? ");
                params.add(contractStatus.trim());
            }
            
            if (projectId != null) {
                sqlBuilder.append("AND c.project_id = ? ");
                params.add(projectId);
            }
            
            // 查询总数
            String countSql = "SELECT COUNT(*) FROM (" + sqlBuilder.toString() + ") as count_table";
            Long total = jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class);
            
            // 添加分页
            sqlBuilder.append("ORDER BY c.create_time DESC ");
            sqlBuilder.append("LIMIT ? OFFSET ? ");
            params.add(size);
            params.add((page - 1) * size);
            
            System.out.println("执行查询SQL: " + sqlBuilder.toString());
            System.out.println("查询参数: " + params);
            
            // 执行查询
            List<ContractVO> contracts = jdbcTemplate.query(sqlBuilder.toString(), params.toArray(), (rs, rowNum) -> {
                ContractVO contract = new ContractVO();
                contract.setId(rs.getLong("id"));
                contract.setContractNo(rs.getString("contract_no"));
                contract.setContractName(rs.getString("contract_name"));
                contract.setContractType(rs.getString("contract_type"));
                contract.setProjectId(rs.getLong("project_id"));
                contract.setProjectName(rs.getString("project_name"));
                contract.setSignatory(rs.getString("signatory"));
                contract.setSignatoryPhone(rs.getString("signatory_phone"));
                contract.setTenantId(rs.getLong("tenant_id"));
                
                // 优先使用关联查询的租户名称，其次使用合同表中的租户名称
                String actualTenantName = rs.getString("actual_tenant_name");
                if (actualTenantName != null && !actualTenantName.trim().isEmpty()) {
                    contract.setTenantName(actualTenantName);
                } else {
                    contract.setTenantName(rs.getString("tenant_name"));
                }
                
                contract.setBusinessBrand(rs.getString("business_brand"));
                contract.setShopSign(rs.getString("shop_sign"));
                contract.setBusinessFormat(rs.getString("business_format"));
                
                // 处理日期字段
                if (rs.getDate("sign_date") != null) {
                    contract.setSignDate(rs.getDate("sign_date").toLocalDate());
                }
                if (rs.getDate("start_date") != null) {
                    contract.setStartDate(rs.getDate("start_date").toLocalDate());
                }
                if (rs.getDate("end_date") != null) {
                    contract.setEndDate(rs.getDate("end_date").toLocalDate());
                }
                if (rs.getDate("deposit_latest_date") != null) {
                    contract.setDepositLatestDate(rs.getDate("deposit_latest_date").toLocalDate());
                }
                if (rs.getDate("first_payment_latest_date") != null) {
                    contract.setFirstPaymentLatestDate(rs.getDate("first_payment_latest_date").toLocalDate());
                }
                
                // 处理数值字段
                contract.setBuildingArea(rs.getBigDecimal("building_area"));
                contract.setRentableArea(rs.getBigDecimal("rentable_area"));
                contract.setContractArea(rs.getBigDecimal("contract_area"));
                contract.setDepositAmount(rs.getBigDecimal("deposit_amount"));
                contract.setFirstPeriodRent(rs.getBigDecimal("first_period_rent"));
                contract.setPeriodRent(rs.getBigDecimal("period_rent"));
                
                contract.setFeeCompany(rs.getString("fee_company"));
                contract.setRentMode(rs.getString("rent_mode"));
                contract.setRentPeriodSetting(rs.getString("rent_period_setting"));
                contract.setLateFeeRule(rs.getString("late_fee_rule"));
                contract.setPaymentAccount(rs.getString("payment_account"));
                contract.setPaymentFrequency(rs.getString("payment_frequency"));
                contract.setLatestPaymentDay(rs.getInt("latest_payment_day"));
                contract.setContractStatus(rs.getString("contract_status"));
                contract.setStatus(rs.getInt("status"));
                
                // 处理时间戳字段
                if (rs.getTimestamp("create_time") != null) {
                    contract.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                }
                if (rs.getTimestamp("update_time") != null) {
                    contract.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
                }
                
                return contract;
            });
            
            PageResult<ContractVO> pageResult = new PageResult<>();
            pageResult.setRecords(contracts);
            pageResult.setTotal(total != null ? total : 0L);
            pageResult.setCurrent(page);
            pageResult.setSize(size);
            
            System.out.println("查询到 " + contracts.size() + " 条合同记录，总数: " + total);
            
            return Result.success(pageResult);
            
        } catch (Exception e) {
            System.err.println("查询合同分页数据失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("查询合同数据失败: " + e.getMessage());
        }
    }

    /**
     * 创建合同
     */
    @PostMapping
    public Result<Long> createContract(@RequestBody ContractCreateDTO dto) {
        System.out.println("创建合同请求数据: " + dto);
        
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
            
            // 检查项目是否存在
            try {
                String checkProjectSql = "SELECT COUNT(*) FROM project WHERE id = ?";
                Integer projectCount = jdbcTemplate.queryForObject(checkProjectSql, new Object[]{dto.getProjectId()}, Integer.class);
                if (projectCount == null || projectCount == 0) {
                    return Result.error("项目不存在，请选择有效的项目");
                }
            } catch (Exception e) {
                System.err.println("检查项目存在性失败: " + e.getMessage());
                // 如果项目表不存在，继续执行
            }
            
            // 检查合同编号是否已存在
            try {
                String checkSql = "SELECT COUNT(*) FROM contract WHERE contract_number = ?";
                Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{dto.getContractNo()}, Integer.class);
                if (count != null && count > 0) {
                    return Result.error("合同编号已存在");
                }
            } catch (Exception e) {
                System.err.println("检查合同编号重复失败: " + e.getMessage());
                // 继续执行
            }
            
            // 获取或创建租户ID
            Long tenantId = dto.getTenantId();
            if (tenantId == null && dto.getTenantName() != null && !dto.getTenantName().trim().isEmpty()) {
                try {
                    // 创建新租户
                    String insertTenantSql = "INSERT INTO tenant (tenant_name, tenant_nature, status, create_time) VALUES (?, ?, ?, NOW())";
                    jdbcTemplate.update(insertTenantSql, 
                        dto.getTenantName().trim(), 
                        "individual", 
                        1);
                    
                    // 获取新创建的租户ID
                    String getLastIdSql = "SELECT LAST_INSERT_ID()";
                    tenantId = jdbcTemplate.queryForObject(getLastIdSql, Long.class);
                    System.out.println("创建新租户，ID: " + tenantId);
                } catch (Exception e) {
                    System.err.println("创建租户失败: " + e.getMessage());
                    return Result.error("创建租户失败: " + e.getMessage());
                }
            }
            
            // 获取单元ID（优先使用unitIds，其次使用单个unitId）
            Long unitId = null;
            if (dto.getUnitIds() != null && !dto.getUnitIds().isEmpty()) {
                unitId = dto.getUnitIds().get(0);
            }
            
            // 使用实际数据库表结构的字段名
            String insertSql = "INSERT INTO contract (contract_no, contract_name, contract_type, project_id, " +
                              "signatory, signatory_phone, tenant_id, tenant_name, business_brand, shop_sign, " +
                              "business_format, sign_date, start_date, end_date, unit_ids, building_area, " +
                              "rentable_area, contract_area, deposit_amount, deposit_latest_date, fee_company, " +
                              "rent_mode, rent_period_setting, late_fee_rule, payment_account, payment_frequency, " +
                              "latest_payment_day, first_payment_latest_date, first_period_rent, period_rent, " +
                              "contract_status, status, create_time, update_time) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
            
            System.out.println("执行SQL: " + insertSql);
            System.out.println("合同编号: " + dto.getContractNo());
            System.out.println("项目ID: " + dto.getProjectId());
            System.out.println("租户ID: " + tenantId);
            
            // 将单元ID列表转换为JSON字符串
            String unitIdsJson = "[]";
            if (dto.getUnitIds() != null && !dto.getUnitIds().isEmpty()) {
                unitIdsJson = "[" + dto.getUnitIds().stream().map(String::valueOf).reduce((a, b) -> a + "," + b).orElse("") + "]";
            }
            
            jdbcTemplate.update(insertSql,
                dto.getContractNo(),                                           // contract_no
                dto.getContractName(),                                         // contract_name
                dto.getContractType() != null ? dto.getContractType() : ContractType.RENT.getCode(), // contract_type
                dto.getProjectId(),                                            // project_id
                dto.getSignatory(),                                            // signatory
                dto.getSignatoryPhone(),                                       // signatory_phone
                tenantId != null ? tenantId : 1L,                             // tenant_id
                dto.getTenantName(),                                           // tenant_name
                dto.getBusinessBrand(),                                        // business_brand
                dto.getShopSign(),                                             // shop_sign
                dto.getBusinessFormat(),                                       // business_format
                dto.getSignDate(),                                             // sign_date
                dto.getStartDate(),                                            // start_date
                dto.getEndDate(),                                              // end_date
                unitIdsJson,                                                   // unit_ids (JSON)
                dto.getBuildingArea(),                                         // building_area
                dto.getRentableArea(),                                         // rentable_area
                dto.getContractArea(),                                         // contract_area
                dto.getDepositAmount(),                                        // deposit_amount
                dto.getDepositLatestDate(),                                    // deposit_latest_date
                dto.getFeeCompany(),                                           // fee_company
                dto.getRentMode() != null ? dto.getRentMode() : "固定租金",      // rent_mode
                dto.getRentPeriodSetting(),                                    // rent_period_setting
                dto.getLateFeeRule(),                                          // late_fee_rule
                dto.getPaymentAccount(),                                       // payment_account
                dto.getPaymentFrequency(),                                     // payment_frequency
                dto.getLatestPaymentDay(),                                     // latest_payment_day
                dto.getFirstPaymentLatestDate(),                               // first_payment_latest_date
                dto.getFirstPeriodRent(),                                      // first_period_rent
                dto.getPeriodRent(),                                           // period_rent
                "DRAFT",                                                       // contract_status
                1                                                              // status
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