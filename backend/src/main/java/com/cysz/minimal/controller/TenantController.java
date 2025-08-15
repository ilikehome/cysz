package com.cysz.minimal.controller;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.common.Result;
import com.cysz.minimal.entity.Tenant;
import com.cysz.minimal.service.TenantService;
import com.cysz.minimal.enums.BusinessFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 租户管理控制器
 */
@RestController
@RequestMapping("/tenant")
@CrossOrigin(origins = "*")
public class TenantController {
    
    @Autowired
    private TenantService tenantService;
    
    /**
     * 分页查询租户信息
     */
    @GetMapping("/page")
    public Result<PageResult<Tenant>> getTenantPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tenantNature,
            @RequestParam(required = false) String businessFormat) {
        
        PageResult<Tenant> result = tenantService.getTenantPage(current, size, keyword, tenantNature, businessFormat);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询租户
     */
    @GetMapping("/{id}")
    public Result<Tenant> getTenantById(@PathVariable Long id) {
        Tenant tenant = tenantService.getTenantById(id);
        if (tenant == null) {
            return Result.error("租户不存在");
        }
        return Result.success(tenant);
    }
    
    /**
     * 创建租户
     */
    @PostMapping
    public Result<Tenant> createTenant(@RequestBody Tenant tenant) {
        try {
            // 验证业态
            if (tenant.getBusinessFormat() != null && !tenant.getBusinessFormat().trim().isEmpty()) {
                if (!BusinessFormat.isValidCode(tenant.getBusinessFormat())) {
                    return Result.error("无效的业态代码: " + tenant.getBusinessFormat());
                }
            }
            
            Tenant createdTenant = tenantService.createTenant(tenant);
            return Result.success(createdTenant);
        } catch (Exception e) {
            return Result.error("创建租户失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新租户
     */
    @PutMapping("/{id}")
    public Result<Tenant> updateTenant(@PathVariable Long id, @RequestBody Tenant tenant) {
        try {
            // 验证业态
            if (tenant.getBusinessFormat() != null && !tenant.getBusinessFormat().trim().isEmpty()) {
                if (!BusinessFormat.isValidCode(tenant.getBusinessFormat())) {
                    return Result.error("无效的业态代码: " + tenant.getBusinessFormat());
                }
            }
            
            Tenant updatedTenant = tenantService.updateTenant(id, tenant);
            return Result.success(updatedTenant);
        } catch (Exception e) {
            return Result.error("更新租户失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTenant(@PathVariable Long id) {
        try {
            tenantService.deleteTenant(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除租户失败: " + e.getMessage());
        }
    }
    
    // ==================== 租户风险管控相关接口 ====================
    
    /**
     * 获取租户风险统计
     */
    @GetMapping("/risk/stats")
    public Result<Map<String, Object>> getRiskStats() {
        Map<String, Object> stats = tenantService.getRiskStats();
        return Result.success(stats);
    }
    
    /**
     * 获取租户风险列表
     */
    @GetMapping("/risk/list")
    public Result<PageResult<Map<String, Object>>> getTenantRiskList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        
        PageResult<Map<String, Object>> result = tenantService.getTenantRiskList(current, size);
        return Result.success(result);
    }
    
    /**
     * 评估租户风险
     */
    @PostMapping("/risk/assess")
    public Result<Void> assessTenantRisk(@RequestBody Map<String, Object> request) {
        try {
            Long tenantId = Long.valueOf(request.get("tenantId").toString());
            Integer creditScore = Integer.valueOf(request.get("creditScore").toString());
            String businessStatus = (String) request.get("businessStatus");
            String financialStatus = (String) request.get("financialStatus");
            String complianceStatus = (String) request.get("complianceStatus");
            String riskNote = (String) request.get("riskNote");
            
            tenantService.assessTenantRisk(tenantId, creditScore, businessStatus, 
                                         financialStatus, complianceStatus, riskNote);
            return Result.success();
        } catch (Exception e) {
            return Result.error("风险评估失败: " + e.getMessage());
        }
    }
    
    // ==================== 租户画像相关接口 ====================
    
    /**
     * 获取租户画像列表
     */
    @GetMapping("/profile/list")
    public Result<PageResult<Map<String, Object>>> getTenantProfileList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        
        PageResult<Map<String, Object>> result = tenantService.getTenantProfileList(current, size);
        return Result.success(result);
    }
    
    /**
     * 获取租户画像详情
     */
    @GetMapping("/profile/{id}")
    public Result<Map<String, Object>> getTenantProfileDetail(@PathVariable Long id) {
        try {
            Map<String, Object> profile = tenantService.getTenantProfileDetail(id);
            return Result.success(profile);
        } catch (Exception e) {
            return Result.error("获取租户画像失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成租户画像
     */
    @PostMapping("/profile/generate")
    public Result<Void> generateTenantProfile(@RequestBody Map<String, Object> request) {
        try {
            Long tenantId = Long.valueOf(request.get("tenantId").toString());
            tenantService.generateTenantProfile(tenantId);
            return Result.success();
        } catch (Exception e) {
            return Result.error("生成租户画像失败: " + e.getMessage());
        }
    }
    
    // ==================== 业态分析相关接口 ====================
    
    /**
     * 获取业态概览
     */
    @GetMapping("/business-analysis/overview")
    public Result<Map<String, Object>> getBusinessOverview() {
        Map<String, Object> overview = tenantService.getBusinessOverview();
        return Result.success(overview);
    }
    
    /**
     * 获取业态详情
     */
    @GetMapping("/business-analysis/details")
    public Result<List<Map<String, Object>>> getBusinessDetails(
            @RequestParam(required = false) String businessType) {
        
        List<Map<String, Object>> details = tenantService.getBusinessDetails(businessType);
        return Result.success(details);
    }
    
    /**
     * 获取业态趋势
     */
    @GetMapping("/business-analysis/trends")
    public Result<Map<String, Object>> getBusinessTrends() {
        Map<String, Object> trends = tenantService.getBusinessTrends();
        return Result.success(trends);
    }
    
    /**
     * 获取业态建议
     */
    @GetMapping("/business-analysis/suggestions")
    public Result<List<Map<String, Object>>> getBusinessSuggestions() {
        List<Map<String, Object>> suggestions = tenantService.getBusinessSuggestions();
        return Result.success(suggestions);
    }
    
    /**
     * 导出业态报告
     */
    @GetMapping("/business-analysis/export")
    public ResponseEntity<byte[]> exportBusinessReport() {
        try {
            byte[] reportData = tenantService.exportBusinessReport();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "business-report.txt");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}