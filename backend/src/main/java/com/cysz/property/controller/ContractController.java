package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Contract;
import com.cysz.property.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 合同管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "合同管理", description = "合同管理相关接口")
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@Validated
public class ContractController {

    private final ContractService contractService;

    @Operation(summary = "分页查询合同列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getContractPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "合同编号") @RequestParam(required = false) String number,
            @Parameter(description = "合同名称") @RequestParam(required = false) String name,
            @Parameter(description = "合同类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "合同状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "租户名称") @RequestParam(required = false) String tenantName,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        PageResult<Map<String, Object>> result = contractService.getContractPage(
                pageQuery, number, name, type, status, tenantName, projectId);
        return Result.success(result);
    }

    @Operation(summary = "获取合同详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getContractDetail(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = contractService.getContractDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建合同")
    @PostMapping
    public Result<Boolean> createContract(
            @Parameter(description = "合同信息") @RequestBody @Valid Contract contract) {
        boolean result = contractService.createContract(contract);
        return Result.success(result);
    }

    @Operation(summary = "更新合同")
    @PutMapping("/{id}")
    public Result<Boolean> updateContract(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id,
            @Parameter(description = "合同信息") @RequestBody @Valid Contract contract) {
        contract.setId(id);
        boolean result = contractService.updateContract(contract);
        return Result.success(result);
    }

    @Operation(summary = "删除合同")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteContract(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        boolean result = contractService.deleteContract(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除合同")
    @DeleteMapping
    public Result<Boolean> batchDeleteContracts(
            @Parameter(description = "合同ID列表") @RequestBody List<Long> contractIds) {
        boolean result = contractService.batchDeleteContracts(contractIds);
        return Result.success(result);
    }

    @Operation(summary = "签署合同")
    @PostMapping("/{id}/sign")
    public Result<Boolean> signContract(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id,
            @Parameter(description = "签署日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate signDate) {
        boolean result = contractService.signContract(id, signDate);
        return Result.success(result);
    }

    @Operation(summary = "生效合同")
    @PostMapping("/{id}/activate")
    public Result<Boolean> activateContract(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id,
            @Parameter(description = "生效日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {
        boolean result = contractService.activateContract(id, effectiveDate);
        return Result.success(result);
    }

    @Operation(summary = "终止合同")
    @PostMapping("/{id}/terminate")
    public Result<Boolean> terminateContract(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id,
            @Parameter(description = "终止日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate terminateDate,
            @Parameter(description = "终止原因") @RequestParam(required = false) String reason) {
        boolean result = contractService.terminateContract(id, terminateDate, reason);
        return Result.success(result);
    }

    @Operation(summary = "续签合同")
    @PostMapping("/{id}/renew")
    public Result<Boolean> renewContract(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id,
            @Parameter(description = "新的到期日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate,
            @Parameter(description = "新租金") @RequestParam(required = false) BigDecimal newRent) {
        boolean result = contractService.renewContract(id, newEndDate, newRent);
        return Result.success(result);
    }

    @Operation(summary = "获取合同统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getContractStatistics() {
        Map<String, Object> result = contractService.getContractStatistics();
        return Result.success(result);
    }

    @Operation(summary = "根据状态统计合同数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getContractCountByStatus() {
        List<Map<String, Object>> result = contractService.getContractCountByStatus();
        return Result.success(result);
    }

    @Operation(summary = "根据类型统计合同数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> getContractCountByType() {
        List<Map<String, Object>> result = contractService.getContractCountByType();
        return Result.success(result);
    }

    @Operation(summary = "获取即将到期的合同")
    @GetMapping("/expiring")
    public Result<List<Map<String, Object>>> getExpiringContracts(
            @Parameter(description = "天数") @RequestParam(defaultValue = "30") Integer days) {
        List<Map<String, Object>> result = contractService.getExpiringContracts(days);
        return Result.success(result);
    }

    @Operation(summary = "获取合同收益统计")
    @GetMapping("/revenue")
    public Result<Map<String, Object>> getContractRevenueStatistics(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        Map<String, Object> result = contractService.getContractRevenueStatistics(year);
        return Result.success(result);
    }

    @Operation(summary = "验证合同编号是否存在")
    @GetMapping("/check-number")
    public Result<Boolean> checkNumberExists(
            @Parameter(description = "合同编号") @RequestParam String number,
            @Parameter(description = "排除的合同ID") @RequestParam(required = false) Long excludeId) {
        boolean result = contractService.checkNumberExists(number, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取合同关联的单元信息")
    @GetMapping("/{id}/units")
    public Result<List<Map<String, Object>>> getContractUnits(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = contractService.getContractUnits(id);
        return Result.success(result);
    }

    @Operation(summary = "获取合同应收账款列表")
    @GetMapping("/{id}/receivables")
    public Result<List<Map<String, Object>>> getContractReceivables(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = contractService.getContractReceivables(id);
        return Result.success(result);
    }

    @Operation(summary = "获取合同已收款列表")
    @GetMapping("/{id}/received")
    public Result<List<Map<String, Object>>> getContractReceived(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = contractService.getContractReceived(id);
        return Result.success(result);
    }

    @Operation(summary = "生成合同应收账款")
    @PostMapping("/{id}/generate-receivables")
    public Result<Map<String, Object>> generateContractReceivables(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id,
            @Parameter(description = "生成类型") @RequestParam Integer generateType,
            @Parameter(description = "期数") @RequestParam(required = false) Integer periods) {
        Map<String, Object> result = contractService.generateContractReceivables(id, generateType, periods);
        return Result.success(result);
    }

    @Operation(summary = "获取合同生成进度")
    @GetMapping("/{id}/generation-progress")
    public Result<Map<String, Object>> getContractGenerationProgress(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = contractService.getContractGenerationProgress(id);
        return Result.success(result);
    }

    @Operation(summary = "获取合同历史记录")
    @GetMapping("/{id}/history")
    public Result<List<Map<String, Object>>> getContractHistory(
            @Parameter(description = "合同ID") @PathVariable @NotNull Long id) {
        List<Map<String, Object>> result = contractService.getContractHistory(id);
        return Result.success(result);
    }

    @Operation(summary = "验证合同数据")
    @PostMapping("/validate")
    public Result<Map<String, Object>> validateContractData(
            @Parameter(description = "合同信息") @RequestBody @Valid Contract contract) {
        Map<String, Object> result = contractService.validateContractData(contract);
        return Result.success(result);
    }

    @Operation(summary = "导出合同数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportContractData(
            @Parameter(description = "合同编号") @RequestParam(required = false) String number,
            @Parameter(description = "合同名称") @RequestParam(required = false) String name,
            @Parameter(description = "合同类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "合同状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "租户名称") @RequestParam(required = false) String tenantName,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        List<Map<String, Object>> result = contractService.exportContractData(
                number, name, type, status, tenantName, projectId);
        return Result.success(result);
    }

    @Operation(summary = "生成合同报表")
    @GetMapping("/report")
    public Result<Map<String, Object>> generateContractReport(
            @Parameter(description = "报表类型") @RequestParam String reportType,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        Map<String, Object> result = contractService.generateContractReport(reportType, startDate, endDate, projectId);
        return Result.success(result);
    }

}