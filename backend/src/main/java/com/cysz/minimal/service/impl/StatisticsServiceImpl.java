package com.cysz.minimal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cysz.minimal.entity.*;
import com.cysz.minimal.enums.UnitPurpose;
import com.cysz.minimal.mapper.*;
import com.cysz.minimal.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private BuildingMapper buildingMapper;
    
    @Autowired
    private FloorMapper floorMapper;
    
    @Autowired
    private UnitMapper unitMapper;
    
    @Autowired
    private ContractMapper contractMapper;
    
    @Autowired
    private TenantMapper tenantMapper;
    
    @Autowired
    private ReceivableAccountMapper receivableAccountMapper;

    @Override
    public Map<String, Object> getDashboardData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 项目总数
            QueryWrapper<Project> projectQuery = new QueryWrapper<>();
            projectQuery.eq("status", 1);
            Integer projectCount = Math.toIntExact(projectMapper.selectCount(projectQuery));
            
            // 租户总数
            QueryWrapper<Tenant> tenantQuery = new QueryWrapper<>();
            tenantQuery.eq("status", 1);
            Integer tenantCount = Math.toIntExact(tenantMapper.selectCount(tenantQuery));
            
            // 单元总数和出租率
            QueryWrapper<Unit> unitQuery = new QueryWrapper<>();
            // 移除status字段查询，因为Unit实体中已删除该字段
            Integer totalUnits = Math.toIntExact(unitMapper.selectCount(unitQuery));
            QueryWrapper<Unit> occupiedUnitQuery = new QueryWrapper<>();
            occupiedUnitQuery.eq("unit_status", "RENTABLE");
            Integer occupiedUnits = Math.toIntExact(unitMapper.selectCount(occupiedUnitQuery));
            
            // 活跃合同数
            QueryWrapper<Contract> activeContractQuery = new QueryWrapper<>();
            activeContractQuery.eq("status", 1).eq("contract_status", "signed_effective");
            Integer activeContracts = Math.toIntExact(contractMapper.selectCount(activeContractQuery));
            
            // 本月租金收入 (使用mock数据，后续通过service层方法获取)
            List<Contract> activeContractList = contractMapper.selectList(activeContractQuery);
            BigDecimal monthlyRent = BigDecimal.valueOf(activeContractList.size() * 5000); // Mock数据：每个合同5000元
            
            // 待处理应收账款
            QueryWrapper<ReceivableAccount> pendingQuery = new QueryWrapper<>();
            pendingQuery.eq("status", 1).eq("process_status", "PENDING");
            Integer pendingReceivables = Math.toIntExact(receivableAccountMapper.selectCount(pendingQuery));
            
            // 计算出租率
            double occupancyRate = totalUnits > 0 ? (double) occupiedUnits / totalUnits * 100 : 0;
            
            result.put("projectCount", projectCount);
            result.put("tenantCount", tenantCount);
            result.put("totalUnits", totalUnits);
            result.put("occupiedUnits", occupiedUnits);
            result.put("occupancyRate", Math.round(occupancyRate * 100.0) / 100.0);
            result.put("activeContracts", activeContracts);
            result.put("monthlyRent", monthlyRent);
            result.put("pendingReceivables", pendingReceivables);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取仪表盘数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProjectStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按项目类型统计
            QueryWrapper<Project> projectQuery = new QueryWrapper<>();
            projectQuery.eq("status", 1);
            List<Project> allProjects = projectMapper.selectList(projectQuery);
            
            Map<String, Long> typeStatsMap = allProjects.stream()
                .collect(Collectors.groupingBy(Project::getProjectType, Collectors.counting()));
            List<Map<String, Object>> projectTypeStats = typeStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    stat.put("displayName", entry.getKey()); // 暂时直接使用code作为显示名称
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 按城市统计项目数量
            Map<String, Long> cityStatsMap = allProjects.stream()
                .collect(Collectors.groupingBy(Project::getCity, Collectors.counting()));
            List<Map<String, Object>> cityStats = cityStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 项目面积统计 - 暂时移除，因为buildingArea字段已删除
            List<Map<String, Object>> areaStats = new ArrayList<>();
            
            result.put("projectTypeStats", projectTypeStats);
            result.put("cityStats", cityStats);
            result.put("areaStats", areaStats);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取项目统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTenantStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按租户类别统计
            QueryWrapper<Tenant> tenantQuery = new QueryWrapper<>();
            List<Tenant> allTenants = tenantMapper.selectList(tenantQuery);
           // 租户类别统计 (使用mock数据，后续会从其他地方获取)
            Map<String, Long> categoryStatsMap = allTenants.stream()
                .collect(Collectors.groupingBy(tenant -> {
                    // Mock数据：根据租户ID分配类别
                    Long id = tenant.getId();
                    if (id == null) return "其他";
                    int category = (int) (id % 4);
                    switch (category) {
                        case 0: return "零售";
                        case 1: return "餐饮";
                        case 2: return "服务";
                        default: return "其他";
                    }
                }, Collectors.counting()));
            List<Map<String, Object>> categoryStats = categoryStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 按项目统计租户数量
            QueryWrapper<Project> projectQuery = new QueryWrapper<>();
            projectQuery.eq("status", 1);
            List<Project> allProjects = projectMapper.selectList(projectQuery);
            
            QueryWrapper<Contract> contractQuery = new QueryWrapper<>();
            contractQuery.eq("status", 1);
            List<Contract> allContracts = contractMapper.selectList(contractQuery);
            
            List<Map<String, Object>> projectTenantStats = allProjects.stream()
                .map(project -> {
                    long tenantCount = allContracts.stream()
                        .filter(contract -> project.getId().equals(contract.getProjectId()))
                        .map(Contract::getTenantId)
                        .distinct()
                        .count();
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", project.getProjectName());
                    stat.put("value", tenantCount);
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 租户风险等级统计 - 使用mock数据，因为数据库中没有risk_level字段
            List<Map<String, Object>> riskStats = Arrays.asList(
                Map.of("name", "低风险", "value", (long)(allTenants.size() * 0.6)),
                Map.of("name", "中风险", "value", (long)(allTenants.size() * 0.3)),
                Map.of("name", "高风险", "value", (long)(allTenants.size() * 0.1))
            );
            
            result.put("categoryStats", categoryStats);
            result.put("projectTenantStats", projectTenantStats);
            result.put("riskStats", riskStats);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取租户统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUnitStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按单元状态统计
            QueryWrapper<Unit> unitQuery = new QueryWrapper<>();
            // 移除status字段查询，因为Unit实体中已删除该字段
            List<Unit> allUnits = unitMapper.selectList(unitQuery);
            
            Map<String, Long> statusStatsMap = allUnits.stream()
                .collect(Collectors.groupingBy(Unit::getUnitStatus, Collectors.counting()));
            List<Map<String, Object>> statusStats = statusStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 按楼栋统计单元数量
            QueryWrapper<Building> buildingQuery = new QueryWrapper<>();
            buildingQuery.eq("status", 1);
            List<Building> allBuildings = buildingMapper.selectList(buildingQuery);
            
            List<Map<String, Object>> buildingUnitStats = allBuildings.stream()
                .map(building -> {
                    // 通过unit->floor->building的关联关系统计单元数量
                    long unitCount = allUnits.stream()
                        .filter(unit -> {
                            // 通过unit的floorId查询floor，再通过floor的buildingId匹配building
                            Floor floor = floorMapper.selectById(unit.getFloorId());
                            return floor != null && building.getId().equals(floor.getBuildingId());
                        })
                        .count();
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", building.getBuildingName());
                    stat.put("value", unitCount);
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 按用途统计
            Map<String, Long> purposeStatsMap = allUnits.stream()
                .collect(Collectors.groupingBy(Unit::getUnitPurpose, Collectors.counting()));
            List<Map<String, Object>> purposeStats = purposeStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    if (entry.getKey() != null) {
                        stat.put("displayName", UnitPurpose.getDisplayNameByCode(entry.getKey()));
                    }
                    return stat;
                })
                .collect(Collectors.toList());
            
            result.put("statusStats", statusStats);
            result.put("buildingUnitStats", buildingUnitStats);
            result.put("purposeStats", purposeStats);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取单元统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getContractStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有合同数据
            QueryWrapper<Contract> contractQuery = new QueryWrapper<>();
            contractQuery.eq("status", 1);
            List<Contract> allContracts = contractMapper.selectList(contractQuery);
            
            // 按合同状态统计
            Map<String, Long> statusStatsMap = allContracts.stream()
                .collect(Collectors.groupingBy(Contract::getContractStatus, Collectors.counting()));
            List<Map<String, Object>> statusStats = statusStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 按合同类型统计
            Map<String, Long> typeStatsMap = allContracts.stream()
                .collect(Collectors.groupingBy(Contract::getContractType, Collectors.counting()));
            List<Map<String, Object>> typeStats = typeStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    String contractType = entry.getKey();
                    stat.put("name", contractType);
                    stat.put("value", entry.getValue());
                    if (contractType != null) {
                        try {
                            stat.put("name", com.cysz.minimal.enums.ContractType.getDisplayNameByCode(contractType));
                        } catch (Exception e) {
                            // 如果转换失败，保持原值
                        }
                    }
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 月租金分布
            List<Contract> effectiveContracts = allContracts.stream()
                .filter(contract -> "signed_effective".equals(contract.getContractStatus()))
                .collect(Collectors.toList());
            
            Map<String, Long> rentDistributionMap = effectiveContracts.stream()
                .collect(Collectors.groupingBy(contract -> {
                    // Mock数据：根据合同ID生成租金范围
                    Long contractId = contract.getId();
                    if (contractId == null) return "未知";
                    int rentRange = (int) (contractId % 4);
                    switch (rentRange) {
                        case 0: return "1万以下";
                        case 1: return "1-2万";
                        case 2: return "2-5万";
                        default: return "5万以上";
                    }
                }, Collectors.counting()));
            List<Map<String, Object>> rentDistribution = rentDistributionMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 即将到期合同（30天内）
            LocalDate now = LocalDate.now();
            LocalDate thirtyDaysLater = now.plusDays(30);
            List<Map<String, Object>> expiringContracts = effectiveContracts.stream()
                .filter(contract -> {
                    LocalDate endDate = contract.getEndDate();
                    return endDate != null && !endDate.isBefore(now) && !endDate.isAfter(thirtyDaysLater);
                })
                .sorted((c1, c2) -> c1.getEndDate().compareTo(c2.getEndDate()))
                .map(contract -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", contract.getContractName());
                    stat.put("value", contract.getEndDate());
                    return stat;
                })
                .collect(Collectors.toList());
            
            result.put("statusStats", statusStats);
            result.put("typeStats", typeStats);
            result.put("rentDistribution", rentDistribution);
            result.put("expiringContracts", expiringContracts);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取合同统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getFinancialStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有应收账款数据
            QueryWrapper<ReceivableAccount> accountQuery = new QueryWrapper<>();
            accountQuery.eq("status", 1);
            List<ReceivableAccount> allAccounts = receivableAccountMapper.selectList(accountQuery);
            
            // 应收账款状态统计
            Map<String, Long> accountStatusStatsMap = allAccounts.stream()
                .collect(Collectors.groupingBy(ReceivableAccount::getAccountStatus, Collectors.counting()));
            List<Map<String, Object>> receivableStatusStats = accountStatusStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 应收账款金额统计
            Map<String, Long> amountStatsMap = allAccounts.stream()
                .collect(Collectors.groupingBy(account -> {
                    BigDecimal receivableAmount = account.getReceivableAmount();
                    if (receivableAmount == null) return "未知";
                    if (receivableAmount.compareTo(new BigDecimal("10000")) < 0) return "1万以下";
                    if (receivableAmount.compareTo(new BigDecimal("50000")) < 0) return "1-5万";
                    if (receivableAmount.compareTo(new BigDecimal("100000")) < 0) return "5-10万";
                    return "10万以上";
                }, Collectors.counting()));
            List<Map<String, Object>> receivableAmountStats = amountStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 月度收款趋势（最近6个月）
            LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
            List<ReceivableAccount> recentAccounts = allAccounts.stream()
                .filter(account -> account.getInputDate() != null && 
                         !account.getInputDate().isBefore(sixMonthsAgo))
                .collect(Collectors.toList());
            
            Map<String, BigDecimal> monthlyTrendMap = recentAccounts.stream()
                .collect(Collectors.groupingBy(
                    account -> account.getInputDate().toString().substring(0, 7), // YYYY-MM format
                    Collectors.reducing(BigDecimal.ZERO, 
                        account -> account.getAmount() != null ? account.getAmount() : BigDecimal.ZERO,
                        BigDecimal::add)
                ));
            List<Map<String, Object>> monthlyTrend = monthlyTrendMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", entry.getKey());
                    stat.put("value", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 总租金收入
            QueryWrapper<Contract> activeContractQuery = new QueryWrapper<>();
            activeContractQuery.eq("status", 1).eq("contract_status", "signed_effective");
            List<Contract> activeContracts = contractMapper.selectList(activeContractQuery);
            // 总租金收入 (使用mock数据，后续通过service层方法获取)
            BigDecimal totalRentIncome = BigDecimal.valueOf(activeContracts.size() * 8000); // Mock数据：每个合同平均8000元
            
            // 待收金额
            BigDecimal pendingAmount = allAccounts.stream()
                .filter(account -> "PENDING".equals(account.getProcessStatus()))
                .map(account -> account.getPendingAmount() != null ? account.getPendingAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            result.put("receivableStatusStats", receivableStatusStats);
            result.put("receivableAmountStats", receivableAmountStats);
            result.put("monthlyTrend", monthlyTrend);
            result.put("totalRentIncome", totalRentIncome);
            result.put("pendingAmount", pendingAmount);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取财务统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getOperationAnalysis() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有项目数据
            QueryWrapper<Project> projectQuery = new QueryWrapper<>();
            projectQuery.eq("status", 1);
            List<Project> allProjects = projectMapper.selectList(projectQuery);
            
            // 获取所有单元数据
            QueryWrapper<Unit> unitQuery = new QueryWrapper<>();
            unitQuery.eq("status", 1);
            List<Unit> allUnits = unitMapper.selectList(unitQuery);
            
            // 获取所有有效合同数据
            QueryWrapper<Contract> contractQuery = new QueryWrapper<>();
            contractQuery.eq("status", 1).eq("contract_status", "signed_effective");
            List<Contract> activeContracts = contractMapper.selectList(contractQuery);
            
            // 出租率趋势分析（按项目）
            List<Map<String, Object>> occupancyByProject = allProjects.stream()
                .map(project -> {
                    // 获取该项目下的所有单元（通过关联查询）
                    List<Unit> projectUnits = allUnits.stream()
                        .filter(unit -> {
                            if (unit.getFloorId() == null) return false;
                            Floor floor = floorMapper.selectById(unit.getFloorId());
                            if (floor == null || floor.getBuildingId() == null) return false;
                            Building building = buildingMapper.selectById(floor.getBuildingId());
                            return building != null && Objects.equals(building.getProjectId(), project.getId());
                        })
                        .collect(Collectors.toList());
                    
                    if (projectUnits.isEmpty()) {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("name", project.getProjectName());
                        stat.put("value", 0.0);
                        return stat;
                    }
                    
                    long rentableCount = projectUnits.stream()
                        .filter(unit -> "RENTABLE".equals(unit.getUnitStatus()))
                        .count();
                    
                    double occupancyRate = (double) rentableCount * 100.0 / projectUnits.size();
                    
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", project.getProjectName());
                    stat.put("value", Math.round(occupancyRate * 100.0) / 100.0);
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 平均租金水平（按项目）
            List<Map<String, Object>> avgRentByProject = allProjects.stream()
                .map(project -> {
                    // 获取该项目下的所有合同（通过关联查询）
                    List<Contract> projectContracts = activeContracts.stream()
                        .filter(contract -> {
                            if (contract.getUnitIds() == null || contract.getUnitIds().trim().isEmpty()) return false;
                            try {
                                // 解析unitIds字符串为ID列表
                                String[] unitIdArray = contract.getUnitIds().split(",");
                                for (String unitIdStr : unitIdArray) {
                                    Long unitId = Long.parseLong(unitIdStr.trim());
                                    Unit unit = unitMapper.selectById(unitId);
                                    if (unit != null && unit.getFloorId() != null) {
                                        Floor floor = floorMapper.selectById(unit.getFloorId());
                                        if (floor != null && floor.getBuildingId() != null) {
                                            Building building = buildingMapper.selectById(floor.getBuildingId());
                                            if (building != null && Objects.equals(building.getProjectId(), project.getId())) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // 忽略解析错误
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
                    
                    if (projectContracts.isEmpty()) {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("name", project.getProjectName());
                        stat.put("value", 0.0);
                        return stat;
                    }
                    
                    // 平均租金 (使用mock数据，后续通过service层方法获取)
                    double avgRent = projectContracts.isEmpty() ? 0.0 : 
                        projectContracts.stream()
                            .mapToDouble(contract -> {
                                // Mock数据：根据合同ID生成租金
                                Long contractId = contract.getId();
                                return contractId != null ? (contractId % 10 + 5) * 1000 : 5000;
                            })
                            .average()
                            .orElse(0.0);
                    
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", project.getProjectName());
                    stat.put("value", Math.round(avgRent * 100.0) / 100.0);
                    return stat;
                })
                .collect(Collectors.toList());
            
            // 空置单元分析
            List<Map<String, Object>> vacantUnits = allUnits.stream()
                .filter(unit -> "DISABLED".equals(unit.getUnitStatus()))
                .map(unit -> {
                    // 通过关联查询获取项目信息
                    String projectName = "未知项目";
                    if (unit.getFloorId() != null) {
                        Floor floor = floorMapper.selectById(unit.getFloorId());
                        if (floor != null && floor.getBuildingId() != null) {
                            Building building = buildingMapper.selectById(floor.getBuildingId());
                            if (building != null && building.getProjectId() != null) {
                                Project project = projectMapper.selectById(building.getProjectId());
                                if (project != null && project.getProjectName() != null) {
                                    projectName = project.getProjectName();
                                }
                            }
                        }
                    }
                    
                    // 通过关联查询获取楼栋名称
                    String buildingName = "未知楼栋";
                    if (unit.getFloorId() != null) {
                        Floor floor = floorMapper.selectById(unit.getFloorId());
                        if (floor != null && floor.getBuildingId() != null) {
                            Building building = buildingMapper.selectById(floor.getBuildingId());
                            if (building != null && building.getBuildingName() != null) {
                                buildingName = building.getBuildingName();
                            }
                        }
                    }
                    
                    String unitName = String.format("%s-%s-%s", 
                        projectName, 
                        buildingName,
                        unit.getUnitCode() != null ? unit.getUnitCode() : "未知单元");
                    
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("name", unitName);
                    // 计算空置天数（使用更新时间到现在的天数）
                    long daysSinceUpdate = unit.getUpdateTime() != null ? 
                        java.time.temporal.ChronoUnit.DAYS.between(unit.getUpdateTime().toLocalDate(), LocalDate.now()) : 0;
                    stat.put("value", daysSinceUpdate);
                    return stat;
                })
                .sorted((u1, u2) -> Long.compare((Long) u2.get("value"), (Long) u1.get("value")))
                .limit(10)
                .collect(Collectors.toList());
            
            result.put("occupancyByProject", occupancyByProject);
            result.put("avgRentByProject", avgRentByProject);
            result.put("vacantUnits", vacantUnits);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取运营分析数据失败: " + e.getMessage());
        }
    }
}