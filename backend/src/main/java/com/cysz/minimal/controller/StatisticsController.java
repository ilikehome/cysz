package com.cysz.minimal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取仪表盘概览数据
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 项目总数
            Integer projectCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM project WHERE status = 1", Integer.class);
            
            // 租户总数
            Integer tenantCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tenant WHERE status = 1", Integer.class);
            
            // 单元总数和出租率
            Integer totalUnits = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM unit WHERE status = 1", Integer.class);
            Integer occupiedUnits = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM unit WHERE status = 1 AND unit_status = 'OCCUPIED'", Integer.class);
            
            // 活跃合同数
            Integer activeContracts = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM contract WHERE status = 1 AND contract_status = 'ACTIVE'", Integer.class);
            
            // 本月租金收入
            BigDecimal monthlyRent = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(monthly_rent), 0) FROM contract WHERE status = 1 AND contract_status = 'ACTIVE'", 
                BigDecimal.class);
            
            // 待处理应收账款
            Integer pendingReceivables = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM receivable_account WHERE status = 1 AND process_status = 'PENDING'", 
                Integer.class);
            
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
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取仪表盘数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取项目统计数据
     */
    @GetMapping("/project")
    public Map<String, Object> getProjectStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按项目类型统计
            List<Map<String, Object>> projectTypeStats = jdbcTemplate.queryForList(
                "SELECT project_type as name, COUNT(*) as value FROM project WHERE status = 1 GROUP BY project_type");
            
            // 按城市统计项目数量
            List<Map<String, Object>> cityStats = jdbcTemplate.queryForList(
                "SELECT city as name, COUNT(*) as value FROM project WHERE status = 1 GROUP BY city");
            
            // 项目面积统计
            List<Map<String, Object>> areaStats = jdbcTemplate.queryForList(
                "SELECT project_name as name, building_area as value FROM project WHERE status = 1 ORDER BY building_area DESC LIMIT 10");
            
            result.put("projectTypeStats", projectTypeStats);
            result.put("cityStats", cityStats);
            result.put("areaStats", areaStats);
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取项目统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取租户统计数据
     */
    @GetMapping("/tenant")
    public Map<String, Object> getTenantStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按租户类别统计
            List<Map<String, Object>> categoryStats = jdbcTemplate.queryForList(
                "SELECT tenant_category as name, COUNT(*) as value FROM tenant WHERE status = 1 GROUP BY tenant_category");
            
            // 按项目统计租户数量
            List<Map<String, Object>> projectTenantStats = jdbcTemplate.queryForList(
                "SELECT p.project_name as name, COUNT(t.id) as value " +
                "FROM project p LEFT JOIN tenant t ON p.id = t.project_id AND t.status = 1 " +
                "WHERE p.status = 1 GROUP BY p.id, p.project_name");
            
            result.put("categoryStats", categoryStats);
            result.put("projectTenantStats", projectTenantStats);
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取租户统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取单元统计数据
     */
    @GetMapping("/unit")
    public Map<String, Object> getUnitStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按单元状态统计
            List<Map<String, Object>> statusStats = jdbcTemplate.queryForList(
                "SELECT unit_status as name, COUNT(*) as value FROM unit WHERE status = 1 GROUP BY unit_status");
            
            // 按楼栋统计单元数量
            List<Map<String, Object>> buildingUnitStats = jdbcTemplate.queryForList(
                "SELECT b.building_name as name, COUNT(u.id) as value " +
                "FROM building b LEFT JOIN unit u ON b.id = u.building_id AND u.status = 1 " +
                "WHERE b.status = 1 GROUP BY b.id, b.building_name");
            
            // 按用途统计
            List<Map<String, Object>> purposeStats = jdbcTemplate.queryForList(
                "SELECT unit_purpose as name, COUNT(*) as value FROM unit WHERE status = 1 GROUP BY unit_purpose");
            
            result.put("statusStats", statusStats);
            result.put("buildingUnitStats", buildingUnitStats);
            result.put("purposeStats", purposeStats);
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取单元统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取合同统计数据
     */
    @GetMapping("/contract")
    public Map<String, Object> getContractStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 按合同状态统计
            List<Map<String, Object>> statusStats = jdbcTemplate.queryForList(
                "SELECT contract_status as name, COUNT(*) as value FROM contract WHERE status = 1 GROUP BY contract_status");
            
            // 按合同类型统计
            List<Map<String, Object>> typeStats = jdbcTemplate.queryForList(
                "SELECT contract_type as name, COUNT(*) as value FROM contract WHERE status = 1 GROUP BY contract_type");
            
            // 月租金分布
            List<Map<String, Object>> rentDistribution = jdbcTemplate.queryForList(
                "SELECT " +
                "CASE " +
                "  WHEN monthly_rent < 10000 THEN '1万以下' " +
                "  WHEN monthly_rent < 20000 THEN '1-2万' " +
                "  WHEN monthly_rent < 50000 THEN '2-5万' " +
                "  ELSE '5万以上' " +
                "END as name, COUNT(*) as value " +
                "FROM contract WHERE status = 1 AND contract_status = 'ACTIVE' GROUP BY " +
                "CASE " +
                "  WHEN monthly_rent < 10000 THEN '1万以下' " +
                "  WHEN monthly_rent < 20000 THEN '1-2万' " +
                "  WHEN monthly_rent < 50000 THEN '2-5万' " +
                "  ELSE '5万以上' " +
                "END");
            
            // 即将到期合同（30天内）
            List<Map<String, Object>> expiringContracts = jdbcTemplate.queryForList(
                "SELECT contract_name as name, end_date as value FROM contract " +
                "WHERE status = 1 AND contract_status = 'ACTIVE' " +
                "AND end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                "ORDER BY end_date");
            
            result.put("statusStats", statusStats);
            result.put("typeStats", typeStats);
            result.put("rentDistribution", rentDistribution);
            result.put("expiringContracts", expiringContracts);
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取合同统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取财务统计数据
     */
    @GetMapping("/financial")
    public Map<String, Object> getFinancialStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 应收账款状态统计
            List<Map<String, Object>> receivableStatusStats = jdbcTemplate.queryForList(
                "SELECT process_status as name, COUNT(*) as value FROM receivable_account WHERE status = 1 GROUP BY process_status");
            
            // 应收账款金额统计
            List<Map<String, Object>> receivableAmountStats = jdbcTemplate.queryForList(
                "SELECT process_status as name, COALESCE(SUM(amount), 0) as value FROM receivable_account WHERE status = 1 GROUP BY process_status");
            
            // 月度收款趋势（最近6个月）
            List<Map<String, Object>> monthlyTrend = jdbcTemplate.queryForList(
                "SELECT DATE_FORMAT(input_date, '%Y-%m') as name, COALESCE(SUM(amount), 0) as value " +
                "FROM receivable_account WHERE status = 1 AND input_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                "GROUP BY DATE_FORMAT(input_date, '%Y-%m') ORDER BY name");
            
            // 总租金收入
            BigDecimal totalRentIncome = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(monthly_rent), 0) FROM contract WHERE status = 1 AND contract_status = 'ACTIVE'", 
                BigDecimal.class);
            
            // 待收金额
            BigDecimal pendingAmount = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(pending_amount), 0) FROM receivable_account WHERE status = 1 AND process_status = 'PENDING'", 
                BigDecimal.class);
            
            result.put("receivableStatusStats", receivableStatusStats);
            result.put("receivableAmountStats", receivableAmountStats);
            result.put("monthlyTrend", monthlyTrend);
            result.put("totalRentIncome", totalRentIncome);
            result.put("pendingAmount", pendingAmount);
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取财务统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取运营分析数据
     */
    @GetMapping("/operation")
    public Map<String, Object> getOperationAnalysis() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 出租率趋势分析（按项目）
            List<Map<String, Object>> occupancyByProject = jdbcTemplate.queryForList(
                "SELECT p.project_name as name, " +
                "ROUND(COUNT(CASE WHEN u.unit_status = 'OCCUPIED' THEN 1 END) * 100.0 / COUNT(u.id), 2) as value " +
                "FROM project p LEFT JOIN building b ON p.id = b.project_id " +
                "LEFT JOIN unit u ON b.id = u.building_id AND u.status = 1 " +
                "WHERE p.status = 1 GROUP BY p.id, p.project_name");
            
            // 平均租金水平（按项目）
            List<Map<String, Object>> avgRentByProject = jdbcTemplate.queryForList(
                "SELECT p.project_name as name, COALESCE(AVG(c.monthly_rent), 0) as value " +
                "FROM project p LEFT JOIN contract c ON p.id = c.project_id AND c.status = 1 AND c.contract_status = 'ACTIVE' " +
                "WHERE p.status = 1 GROUP BY p.id, p.project_name");
            
            // 空置单元分析
            List<Map<String, Object>> vacantUnits = jdbcTemplate.queryForList(
                "SELECT CONCAT(p.project_name, '-', b.building_name, '-', u.unit_code) as name, " +
                "DATEDIFF(CURDATE(), u.update_time) as value " +
                "FROM unit u JOIN building b ON u.building_id = b.id " +
                "JOIN project p ON b.project_id = p.id " +
                "WHERE u.status = 1 AND u.unit_status = 'VACANT' " +
                "ORDER BY value DESC LIMIT 10");
            
            result.put("occupancyByProject", occupancyByProject);
            result.put("avgRentByProject", avgRentByProject);
            result.put("vacantUnits", vacantUnits);
            
            return Map.of("code", 200, "message", "success", "data", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("code", 500, "message", "获取运营分析数据失败: " + e.getMessage());
        }
    }
}