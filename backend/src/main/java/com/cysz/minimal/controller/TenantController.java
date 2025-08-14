package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 租户管理控制器
 * 包含租户信息管理、风险管控、租户画像三个模块
 */
@RestController
@RequestMapping("/tenant")
@CrossOrigin
public class TenantController {
    
    // 模拟数据存储
    private static final Map<Integer, Map<String, Object>> tenantStorage = new HashMap<>();
    private static int nextId = 1;
    
    static {
        // 初始化一些测试数据
        Map<String, Object> tenant1 = new HashMap<>();
        tenant1.put("id", 1);
        tenant1.put("tenantCode", "T001");
        tenant1.put("tenantName", "北京科技有限公司");
        tenant1.put("tenantCategory", "企业");
        tenant1.put("legalPerson", "张三");
        tenant1.put("contactPhone", "13800138001");
        tenant1.put("socialCreditCode", "91110000123456789X");
        tenant1.put("createTime", "2024-01-10 10:30:00");
        tenantStorage.put(1, tenant1);
        
        Map<String, Object> tenant2 = new HashMap<>();
        tenant2.put("id", 2);
        tenant2.put("tenantCode", "T002");
        tenant2.put("tenantName", "上海贸易公司");
        tenant2.put("tenantCategory", "企业");
        tenant2.put("legalPerson", "李四");
        tenant2.put("contactPhone", "13800138002");
        tenant2.put("socialCreditCode", "91310000987654321Y");
        tenant2.put("createTime", "2024-01-12 14:20:00");
        tenantStorage.put(2, tenant2);
        
        nextId = 3;
    }
    
    /**
     * 分页查询租户
     */
    @GetMapping("/page")
    public Map<String, Object> getTenantPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tenantCategory) {
        
        System.out.println("租户分页查询 - current: " + current + ", size: " + size + ", keyword: " + keyword + ", tenantCategory: " + tenantCategory);
        
        List<Map<String, Object>> allTenants = new ArrayList<>(tenantStorage.values());
        
        // 过滤数据
        if (keyword != null && !keyword.trim().isEmpty()) {
            allTenants = allTenants.stream()
                    .filter(tenant -> {
                        String tenantName = (String) tenant.get("tenantName");
                        String tenantCode = (String) tenant.get("tenantCode");
                        return (tenantName != null && tenantName.contains(keyword)) ||
                               (tenantCode != null && tenantCode.contains(keyword));
                    })
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        }
        
        if (tenantCategory != null && !tenantCategory.trim().isEmpty()) {
            allTenants = allTenants.stream()
                    .filter(tenant -> tenantCategory.equals(tenant.get("tenantCategory")))
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        }
        
        // 分页处理
        int total = allTenants.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = new ArrayList<>();
        if (start < total) {
            records = allTenants.subList(start, end);
        }
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", records);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", pageResult);
        
        return response;
    }
    
    /**
     * 创建租户
     */
    @PostMapping
    public Map<String, Object> createTenant(@RequestBody Map<String, Object> tenantData) {
        System.out.println("创建租户: " + tenantData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查租户编码是否重复
            String tenantCode = (String) tenantData.get("tenantCode");
            boolean codeExists = tenantStorage.values().stream()
                    .anyMatch(tenant -> tenantCode.equals(tenant.get("tenantCode")));
            
            if (codeExists) {
                response.put("code", 400);
                response.put("message", "租户编码已存在");
                response.put("data", null);
                return response;
            }
            
            // 创建新租户
            Map<String, Object> newTenant = new HashMap<>(tenantData);
            newTenant.put("id", nextId++);
            newTenant.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            newTenant.put("status", 1);
            
            tenantStorage.put((Integer) newTenant.get("id"), newTenant);
            
            response.put("code", 200);
            response.put("message", "创建成功");
            response.put("data", newTenant);
            
            System.out.println("租户创建成功，ID: " + newTenant.get("id"));
            
        } catch (Exception e) {
            System.err.println("创建租户失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "创建失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 更新租户
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateTenant(@PathVariable Integer id, @RequestBody Map<String, Object> tenantData) {
        System.out.println("更新租户 ID: " + id + ", 数据: " + tenantData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> existingTenant = tenantStorage.get(id);
            if (existingTenant == null) {
                response.put("code", 404);
                response.put("message", "租户不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查租户编码是否重复（排除自己）
            String tenantCode = (String) tenantData.get("tenantCode");
            boolean codeExists = tenantStorage.values().stream()
                    .anyMatch(tenant -> tenantCode.equals(tenant.get("tenantCode")) && 
                             !id.equals(tenant.get("id")));
            
            if (codeExists) {
                response.put("code", 400);
                response.put("message", "租户编码已存在");
                response.put("data", null);
                return response;
            }
            
            // 更新租户信息
            existingTenant.putAll(tenantData);
            existingTenant.put("id", id); // 确保ID不被覆盖
            existingTenant.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            response.put("code", 200);
            response.put("message", "更新成功");
            response.put("data", existingTenant);
            
            System.out.println("租户更新成功，ID: " + id);
            
        } catch (Exception e) {
            System.err.println("更新租户失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "更新失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTenant(@PathVariable Integer id) {
        System.out.println("删除租户 ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> tenant = tenantStorage.remove(id);
            if (tenant == null) {
                response.put("code", 404);
                response.put("message", "租户不存在");
                response.put("data", null);
                return response;
            }
            
            response.put("code", 200);
            response.put("message", "删除成功");
            response.put("data", null);
            
            System.out.println("租户删除成功，ID: " + id);
            
        } catch (Exception e) {
            System.err.println("删除租户失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 获取租户风险统计
     */
    @GetMapping("/risk/stats")
    public Map<String, Object> getRiskStats() {
        System.out.println("获取租户风险统计");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("highRisk", 12);
        stats.put("mediumRisk", 35);
        stats.put("lowRisk", 128);
        stats.put("total", 175);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", stats);
        
        return response;
    }
    
    /**
     * 获取租户风险列表
     */
    @GetMapping("/risk/list")
    public Map<String, Object> getTenantRiskList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) String riskType) {
        
        System.out.println("租户风险列表查询");
        
        // 模拟风险数据
        List<Map<String, Object>> riskData = new ArrayList<>();
        
        Map<String, Object> risk1 = new HashMap<>();
        risk1.put("id", 1);
        risk1.put("tenantCode", "T001");
        risk1.put("tenantName", "北京科技有限公司");
        risk1.put("riskLevel", "高风险");
        risk1.put("riskScore", 85);
        risk1.put("riskTypes", Arrays.asList("信用风险", "财务风险"));
        risk1.put("lastAssessment", "2024-01-15 10:30:00");
        risk1.put("overdueDays", 15);
        riskData.add(risk1);
        
        Map<String, Object> risk2 = new HashMap<>();
        risk2.put("id", 2);
        risk2.put("tenantCode", "T002");
        risk2.put("tenantName", "上海贸易公司");
        risk2.put("riskLevel", "中风险");
        risk2.put("riskScore", 65);
        risk2.put("riskTypes", Arrays.asList("经营风险"));
        risk2.put("lastAssessment", "2024-01-12 14:20:00");
        risk2.put("overdueDays", 5);
        riskData.add(risk2);
        
        // 分页处理
        int total = riskData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = new ArrayList<>();
        if (start < total) {
            records = riskData.subList(start, end);
        }
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", records);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", pageResult);
        
        return response;
    }
    
    /**
     * 租户风险评估
     */
    @PostMapping("/risk/assess")
    public Map<String, Object> assessTenantRisk(@RequestBody Map<String, Object> assessmentData) {
        System.out.println("租户风险评估: " + assessmentData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 模拟风险评估逻辑
            Integer tenantId = (Integer) assessmentData.get("tenantId");
            Integer creditScore = (Integer) assessmentData.get("creditScore");
            String businessStatus = (String) assessmentData.get("businessStatus");
            String financialStatus = (String) assessmentData.get("financialStatus");
            String complianceStatus = (String) assessmentData.get("complianceStatus");
            
            // 计算综合风险评分
            double totalScore = creditScore * 0.4;
            
            Map<String, Integer> businessScoreMap = Map.of("良好", 20, "一般", 15, "较差", 5);
            totalScore += businessScoreMap.getOrDefault(businessStatus, 0);
            
            Map<String, Integer> financialScoreMap = Map.of("健康", 20, "一般", 15, "紧张", 5);
            totalScore += financialScoreMap.getOrDefault(financialStatus, 0);
            
            Map<String, Integer> complianceScoreMap = Map.of("合规", 20, "轻微违规", 10, "严重违规", 0);
            totalScore += complianceScoreMap.getOrDefault(complianceStatus, 0);
            
            // 确定风险等级
            String riskLevel = "低风险";
            if (totalScore >= 80) riskLevel = "高风险";
            else if (totalScore >= 60) riskLevel = "中风险";
            
            Map<String, Object> result = new HashMap<>();
            result.put("tenantId", tenantId);
            result.put("riskScore", Math.round(totalScore));
            result.put("riskLevel", riskLevel);
            result.put("assessmentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            response.put("code", 200);
            response.put("message", "风险评估完成");
            response.put("data", result);
            
        } catch (Exception e) {
            System.err.println("风险评估失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "风险评估失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 获取租户画像列表
     */
    @GetMapping("/profile/list")
    public Map<String, Object> getTenantProfileList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String creditLevel) {
        
        System.out.println("租户画像列表查询");
        
        // 模拟画像数据
        List<Map<String, Object>> profileData = new ArrayList<>();
        
        Map<String, Object> profile1 = new HashMap<>();
        profile1.put("id", 1);
        profile1.put("tenantCode", "T001");
        profile1.put("tenantName", "北京科技有限公司");
        profile1.put("industry", "科技");
        profile1.put("creditLevel", "AA");
        profile1.put("businessScale", "中型企业");
        profile1.put("registeredCapital", 5000000);
        profile1.put("employeeCount", 150);
        profile1.put("profileScore", 4.2);
        profile1.put("lastUpdate", "2024-01-15 10:30:00");
        profileData.add(profile1);
        
        Map<String, Object> profile2 = new HashMap<>();
        profile2.put("id", 2);
        profile2.put("tenantCode", "T002");
        profile2.put("tenantName", "上海贸易公司");
        profile2.put("industry", "贸易");
        profile2.put("creditLevel", "A");
        profile2.put("businessScale", "小型企业");
        profile2.put("registeredCapital", 1000000);
        profile2.put("employeeCount", 50);
        profile2.put("profileScore", 3.8);
        profile2.put("lastUpdate", "2024-01-12 14:20:00");
        profileData.add(profile2);
        
        // 分页处理
        int total = profileData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = new ArrayList<>();
        if (start < total) {
            records = profileData.subList(start, end);
        }
        
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("records", records);
        pageResult.put("total", total);
        pageResult.put("current", current);
        pageResult.put("size", size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", pageResult);
        
        return response;
    }
    
    /**
     * 获取租户画像详情
     */
    @GetMapping("/profile/{id}")
    public Map<String, Object> getTenantProfileDetail(@PathVariable Integer id) {
        System.out.println("获取租户画像详情 ID: " + id);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 模拟详细画像数据
            Map<String, Object> profileDetail = new HashMap<>();
            profileDetail.put("id", id);
            profileDetail.put("tenantCode", "T001");
            profileDetail.put("tenantName", "北京科技有限公司");
            profileDetail.put("industry", "科技");
            profileDetail.put("creditLevel", "AA");
            profileDetail.put("businessScale", "中型企业");
            profileDetail.put("registeredCapital", 5000000);
            profileDetail.put("employeeCount", 150);
            profileDetail.put("establishDate", "2016-03-15");
            profileDetail.put("creditScore", 85);
            profileDetail.put("contractPerformance", 4);
            profileDetail.put("paymentRecord", "良好");
            profileDetail.put("businessYears", 8);
            profileDetail.put("annualRevenue", 50000000);
            profileDetail.put("profitability", "良好");
            profileDetail.put("marketPosition", "行业前20%");
            profileDetail.put("developmentTrend", "上升");
            
            // 特征标签
            List<Map<String, Object>> tags = new ArrayList<>();
            tags.add(Map.of("name", "高成长", "type", "success", "size", "large"));
            tags.add(Map.of("name", "科技创新", "type", "primary", "size", "default"));
            tags.add(Map.of("name", "信用优良", "type", "success", "size", "default"));
            tags.add(Map.of("name", "规模扩张", "type", "warning", "size", "small"));
            profileDetail.put("tags", tags);
            
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", profileDetail);
            
        } catch (Exception e) {
            System.err.println("获取画像详情失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "获取画像详情失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 生成租户画像
     */
    @PostMapping("/profile/generate")
    public Map<String, Object> generateTenantProfile(@RequestBody Map<String, Object> generateData) {
        System.out.println("生成租户画像: " + generateData);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 模拟画像生成逻辑
            Integer tenantId = (Integer) generateData.get("tenantId");
            
            Map<String, Object> result = new HashMap<>();
            result.put("tenantId", tenantId);
            result.put("profileScore", 4.2);
            result.put("generateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            result.put("status", "生成成功");
            
            response.put("code", 200);
            response.put("message", "画像生成完成");
            response.put("data", result);
            
        } catch (Exception e) {
            System.err.println("画像生成失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "画像生成失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    // ==================== 业态分析相关接口 ====================
    
    /**
     * 获取业态概览统计
     */
    @GetMapping("/business-analysis/overview")
    public Map<String, Object> getBusinessOverview() {
        System.out.println("获取业态概览统计");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("retail", Map.of("count", 45, "ratio", 35.2));
        stats.put("catering", Map.of("count", 38, "ratio", 29.7));
        stats.put("entertainment", Map.of("count", 25, "ratio", 19.5));
        stats.put("service", Map.of("count", 20, "ratio", 15.6));
        
        Map<String, Object> overview = new HashMap<>();
        overview.put("businessStats", stats);
        overview.put("totalTenants", 128);
        overview.put("totalArea", 25000);
        overview.put("avgOccupancyRate", 88.5);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", overview);
        
        return response;
    }
    
    /**
     * 获取业态详细分析数据
     */
    @GetMapping("/business-analysis/details")
    public Map<String, Object> getBusinessDetails(
            @RequestParam(required = false) String businessType) {
        
        System.out.println("获取业态详细分析数据，业态类型: " + businessType);
        
        List<Map<String, Object>> details = new ArrayList<>();
        
        // 模拟业态详细数据
        details.add(createBusinessDetail("零售", "服装鞋帽", 15, 2800, 92, 180, 50.4, 1800, 8.5));
        details.add(createBusinessDetail("零售", "数码电器", 12, 2200, 88, 200, 44.0, 2000, 12.3));
        details.add(createBusinessDetail("餐饮", "中式正餐", 18, 3600, 95, 220, 79.2, 2200, 6.8));
        details.add(createBusinessDetail("餐饮", "快餐小食", 20, 2000, 100, 250, 50.0, 2500, 15.2));
        details.add(createBusinessDetail("娱乐", "影院KTV", 8, 4500, 75, 150, 67.5, 1500, -2.1));
        details.add(createBusinessDetail("娱乐", "健身运动", 17, 5100, 82, 120, 61.2, 1200, 18.7));
        details.add(createBusinessDetail("服务", "美容美发", 12, 1800, 90, 160, 28.8, 1600, 4.2));
        details.add(createBusinessDetail("服务", "教育培训", 8, 2400, 85, 140, 33.6, 1400, 22.1));
        
        // 根据业态类型过滤
        if (businessType != null && !businessType.isEmpty()) {
            details = details.stream()
                    .filter(detail -> businessType.equals(detail.get("businessType")))
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", details);
        
        return response;
    }
    
    /**
     * 获取业态发展趋势数据
     */
    @GetMapping("/business-analysis/trends")
    public Map<String, Object> getBusinessTrends() {
        System.out.println("获取业态发展趋势数据");
        
        Map<String, Object> trends = new HashMap<>();
        
        // 模拟趋势数据
        trends.put("months", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月"));
        trends.put("retail", Arrays.asList(85, 88, 92, 90, 94, 94.4));
        trends.put("catering", Arrays.asList(115, 120, 125, 128, 130, 129.2));
        trends.put("entertainment", Arrays.asList(135, 132, 128, 125, 130, 128.7));
        trends.put("service", Arrays.asList(55, 58, 60, 62, 61, 62.4));
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", trends);
        
        return response;
    }
    
    /**
     * 获取业态优化建议
     */
    @GetMapping("/business-analysis/suggestions")
    public Map<String, Object> getBusinessSuggestions() {
        System.out.println("获取业态优化建议");
        
        Map<String, Object> suggestions = new HashMap<>();
        
        suggestions.put("highPerformance", Arrays.asList(
            "快餐小食：坪效高，增长迅速",
            "教育培训：市场需求旺盛", 
            "健身运动：符合消费升级趋势",
            "数码电器：客单价高，利润丰厚"
        ));
        
        suggestions.put("optimization", Arrays.asList(
            "影院KTV：考虑业态调整或租金优化",
            "服装鞋帽：关注线上冲击，提升体验",
            "美容美发：增加高端服务项目",
            "中式正餐：优化成本结构"
        ));
        
        suggestions.put("marketTrend", Arrays.asList(
            "新零售体验店：线上线下融合",
            "轻奢品牌：消费升级趋势",
            "儿童业态：家庭消费增长",
            "智能科技：5G、AI体验店"
        ));
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", suggestions);
        
        return response;
    }
    
    /**
     * 导出业态分析报告
     */
    @GetMapping("/business-analysis/export")
    public Map<String, Object> exportBusinessReport() {
        System.out.println("导出业态分析报告");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 模拟报告生成
            Map<String, Object> reportInfo = new HashMap<>();
            reportInfo.put("fileName", "业态分析报告_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
            reportInfo.put("fileSize", "2.5MB");
            reportInfo.put("generateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            reportInfo.put("downloadUrl", "/api/download/business-report/" + System.currentTimeMillis());
            
            response.put("code", 200);
            response.put("message", "报告生成成功");
            response.put("data", reportInfo);
            
        } catch (Exception e) {
            System.err.println("导出报告失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "导出报告失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    /**
     * 创建业态详细数据的辅助方法
     */
    private Map<String, Object> createBusinessDetail(String businessType, String subCategory, 
            int tenantCount, int totalArea, int occupancyRate, int avgRent, 
            double monthlyRevenue, int efficiency, double growthRate) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("businessType", businessType);
        detail.put("subCategory", subCategory);
        detail.put("tenantCount", tenantCount);
        detail.put("totalArea", totalArea);
        detail.put("occupancyRate", occupancyRate);
        detail.put("avgRent", avgRent);
        detail.put("monthlyRevenue", monthlyRevenue);
        detail.put("efficiency", efficiency);
        detail.put("growthRate", growthRate);
        return detail;
    }
}
