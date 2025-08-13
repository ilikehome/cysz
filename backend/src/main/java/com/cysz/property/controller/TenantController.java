package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Tenant;
import com.cysz.property.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 租户管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "租户管理", description = "租户管理相关接口")
@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Validated
public class TenantController {

    private final TenantService tenantService;

    @Operation(summary = "分页查询租户列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getTenantPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "租户名称") @RequestParam(required = false) String name,
            @Parameter(description = "租户编号") @RequestParam(required = false) String number,
            @Parameter(description = "租户性质") @RequestParam(required = false) Integer nature,
            @Parameter(description = "业务形态") @RequestParam(required = false) Integer businessFormat,
            @Parameter(description = "租户状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "信用等级") @RequestParam(required = false) Integer creditRating,
            @Parameter(description = "风险等级") @RequestParam(required = false) Integer riskLevel) {
        PageResult<Map<String, Object>> result = tenantService.getTenantPage(
                pageQuery, name, number, nature, businessFormat, status, creditRating, riskLevel);
        return Result.success(result);
    }

    @Operation(summary = "获取租户详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getTenantDetail(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = tenantService.getTenantDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建租户")
    @PostMapping
    public Result<Boolean> createTenant(
            @Parameter(description = "租户信息") @RequestBody @Valid Tenant tenant) {
        boolean result = tenantService.createTenant(tenant);
        return Result.success(result);
    }

    @Operation(summary = "更新租户")
    @PutMapping("/{id}")
    public Result<Boolean> updateTenant(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "租户信息") @RequestBody @Valid Tenant tenant) {
        tenant.setId(id);
        boolean result = tenantService.updateTenant(tenant);
        return Result.success(result);
    }

    @Operation(summary = "删除租户")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTenant(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        boolean result = tenantService.deleteTenant(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除租户")
    @DeleteMapping
    public Result<Boolean> batchDeleteTenants(
            @Parameter(description = "租户ID列表") @RequestBody List<Long> tenantIds) {
        boolean result = tenantService.batchDeleteTenants(tenantIds);
        return Result.success(result);
    }

    @Operation(summary = "获取租户统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getTenantStatistics() {
        Map<String, Object> result = tenantService.getTenantStatistics();
        return Result.success(result);
    }

    @Operation(summary = "根据性质统计租户数量")
    @GetMapping("/statistics/nature")
    public Result<List<Map<String, Object>>> getTenantCountByNature() {
        List<Map<String, Object>> result = tenantService.getTenantCountByNature();
        return Result.success(result);
    }

    @Operation(summary = "根据业务形态统计租户数量")
    @GetMapping("/statistics/business-format")
    public Result<List<Map<String, Object>>> getTenantCountByBusinessFormat() {
        List<Map<String, Object>> result = tenantService.getTenantCountByBusinessFormat();
        return Result.success(result);
    }

    @Operation(summary = "根据信用等级统计租户数量")
    @GetMapping("/statistics/credit-rating")
    public Result<List<Map<String, Object>>> getTenantCountByCreditRating() {
        List<Map<String, Object>> result = tenantService.getTenantCountByCreditRating();
        return Result.success(result);
    }

    @Operation(summary = "根据风险等级统计租户数量")
    @GetMapping("/statistics/risk-level")
    public Result<List<Map<String, Object>>> getTenantCountByRiskLevel() {
        List<Map<String, Object>> result = tenantService.getTenantCountByRiskLevel();
        return Result.success(result);
    }

    @Operation(summary = "获取所有启用的租户")
    @GetMapping("/enabled")
    public Result<List<Map<String, Object>>> getEnabledTenants() {
        List<Map<String, Object>> result = tenantService.getEnabledTenants();
        return Result.success(result);
    }

    @Operation(summary = "验证租户名称是否存在")
    @GetMapping("/check-name")
    public Result<Boolean> checkNameExists(
            @Parameter(description = "租户名称") @RequestParam String name,
            @Parameter(description = "排除的租户ID") @RequestParam(required = false) Long excludeId) {
        boolean result = tenantService.checkNameExists(name, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "验证租户编号是否存在")
    @GetMapping("/check-number")
    public Result<Boolean> checkNumberExists(
            @Parameter(description = "租户编号") @RequestParam String number,
            @Parameter(description = "排除的租户ID") @RequestParam(required = false) Long excludeId) {
        boolean result = tenantService.checkNumberExists(number, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "验证统一社会信用代码是否存在")
    @GetMapping("/check-credit-code")
    public Result<Boolean> checkCreditCodeExists(
            @Parameter(description = "统一社会信用代码") @RequestParam String creditCode,
            @Parameter(description = "排除的租户ID") @RequestParam(required = false) Long excludeId) {
        boolean result = tenantService.checkCreditCodeExists(creditCode, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取租户合同列表")
    @GetMapping("/{id}/contracts")
    public Result<List<Map<String, Object>>> getTenantContracts(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = tenantService.getTenantContracts(id);
        return Result.success(result);
    }

    @Operation(summary = "获取租户应收账款列表")
    @GetMapping("/{id}/receivables")
    public Result<List<Map<String, Object>>> getTenantReceivables(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = tenantService.getTenantReceivables(id);
        return Result.success(result);
    }

    @Operation(summary = "获取租户已收款列表")
    @GetMapping("/{id}/received")
    public Result<List<Map<String, Object>>> getTenantReceived(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = tenantService.getTenantReceived(id);
        return Result.success(result);
    }

    @Operation(summary = "租户风险评估")
    @GetMapping("/{id}/risk-assessment")
    public Result<Map<String, Object>> assessTenantRisk(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = tenantService.assessTenantRisk(id);
        return Result.success(result);
    }

    @Operation(summary = "租户业务分析")
    @GetMapping("/{id}/business-analysis")
    public Result<Map<String, Object>> analyzeTenantBusiness(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = tenantService.analyzeTenantBusiness(id);
        return Result.success(result);
    }

    @Operation(summary = "生成租户档案")
    @GetMapping("/{id}/profile")
    public Result<Map<String, Object>> generateTenantProfile(
            @Parameter(description = "租户ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = tenantService.generateTenantProfile(id);
        return Result.success(result);
    }

    @Operation(summary = "导出租户数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportTenantData(
            @Parameter(description = "租户名称") @RequestParam(required = false) String name,
            @Parameter(description = "租户编号") @RequestParam(required = false) String number,
            @Parameter(description = "租户性质") @RequestParam(required = false) Integer nature,
            @Parameter(description = "业务形态") @RequestParam(required = false) Integer businessFormat,
            @Parameter(description = "租户状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "信用等级") @RequestParam(required = false) Integer creditRating,
            @Parameter(description = "风险等级") @RequestParam(required = false) Integer riskLevel) {
        List<Map<String, Object>> result = tenantService.exportTenantData(
                name, number, nature, businessFormat, status, creditRating, riskLevel);
        return Result.success(result);
    }

}