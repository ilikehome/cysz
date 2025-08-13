package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Receivable;
import com.cysz.property.service.ReceivableService;
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
 * 应收账款管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "应收账款管理", description = "应收账款管理相关接口")
@RestController
@RequestMapping("/api/receivables")
@RequiredArgsConstructor
@Validated
public class ReceivableController {

    private final ReceivableService receivableService;

    @Operation(summary = "分页查询应收账款列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getReceivablePage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "账单编号") @RequestParam(required = false) String billNumber,
            @Parameter(description = "账单类型") @RequestParam(required = false) Integer billType,
            @Parameter(description = "账单状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "租户名称") @RequestParam(required = false) String tenantName,
            @Parameter(description = "合同编号") @RequestParam(required = false) String contractNumber,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        PageResult<Map<String, Object>> result = receivableService.getReceivablePage(
                pageQuery, billNumber, billType, contractNumber, tenantName, status, startDate, endDate);
        return Result.success(result);
    }

    @Operation(summary = "获取应收账款详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getReceivableDetail(
            @Parameter(description = "应收账款ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = receivableService.getReceivableDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建应收账款")
    @PostMapping
    public Result<Boolean> createReceivable(
            @Parameter(description = "应收账款信息") @RequestBody @Valid Receivable receivable) {
        boolean result = receivableService.createReceivable(receivable);
        return Result.success(result);
    }

    @Operation(summary = "更新应收账款")
    @PutMapping("/{id}")
    public Result<Boolean> updateReceivable(
            @Parameter(description = "应收账款ID") @PathVariable @NotNull Long id,
            @Parameter(description = "应收账款信息") @RequestBody @Valid Receivable receivable) {
        receivable.setId(id);
        boolean result = receivableService.updateReceivable(receivable);
        return Result.success(result);
    }

    @Operation(summary = "删除应收账款")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteReceivable(
            @Parameter(description = "应收账款ID") @PathVariable @NotNull Long id) {
        boolean result = receivableService.deleteReceivable(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除应收账款")
    @DeleteMapping
    public Result<Boolean> batchDeleteReceivables(
            @Parameter(description = "应收账款ID列表") @RequestBody List<Long> receivableIds) {
        boolean result = receivableService.batchDeleteReceivables(receivableIds);
        return Result.success(result);
    }

    @Operation(summary = "获取应收账款统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getReceivableStatistics() {
        Map<String, Object> result = receivableService.getReceivableStatistics();
        return Result.success(result);
    }

    @Operation(summary = "获取逾期应收账款统计")
    @GetMapping("/statistics/overdue")
    public Result<Map<String, Object>> getOverdueReceivableStatistics() {
        Map<String, Object> result = receivableService.getOverdueReceivableStatistics();
        return Result.success(result);
    }

    @Operation(summary = "获取应收账款趋势统计")
    @GetMapping("/statistics/trend")
    public Result<List<Map<String, Object>>> getReceivableTrendStatistics(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        List<Map<String, Object>> result = receivableService.getReceivableTrendStatistics(year);
        return Result.success(result);
    }

    @Operation(summary = "根据账单类型统计应收账款")
    @GetMapping("/statistics/bill-type")
    public Result<List<Map<String, Object>>> getReceivableStatisticsByBillType() {
        List<Map<String, Object>> result = receivableService.getReceivableStatisticsByBillType();
        return Result.success(result);
    }

    @Operation(summary = "根据状态统计应收账款")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> getReceivableStatisticsByStatus() {
        List<Map<String, Object>> result = receivableService.getReceivableStatisticsByStatus();
        return Result.success(result);
    }

    @Operation(summary = "生成单笔应收账款")
    @PostMapping("/generate-single")
    public Result<Boolean> generateSingleReceivable(
            @Parameter(description = "应收账款信息") @RequestBody @Valid Receivable receivable) {
        boolean result = receivableService.generateSingleReceivable(receivable);
        return Result.success(result);
    }

    @Operation(summary = "批量生成应收账款")
    @PostMapping("/generate-batch")
    public Result<Map<String, Object>> generateBatchReceivables(
            @Parameter(description = "应收账款列表") @RequestBody List<Receivable> receivables) {
        Map<String, Object> result = receivableService.generateBatchReceivables(receivables);
        return Result.success(result);
    }

    @Operation(summary = "根据合同自动生成应收账款")
    @PostMapping("/generate-by-contract")
    public Result<Map<String, Object>> generateReceivablesByContract(
            @Parameter(description = "合同ID") @RequestParam @NotNull Long contractId,
            @Parameter(description = "生成类型") @RequestParam Integer generateType,
            @Parameter(description = "期数") @RequestParam(required = false) Integer periods) {
        Map<String, Object> result = receivableService.generateReceivablesByContract(contractId, generateType, periods);
        return Result.success(result);
    }

    @Operation(summary = "自动匹配应收账款")
    @PostMapping("/auto-match")
    public Result<Map<String, Object>> autoMatchReceivables() {
        Map<String, Object> result = receivableService.autoMatchReceivables();
        return Result.success(result);
    }

    @Operation(summary = "手动匹配应收账款")
    @PostMapping("/manual-match")
    public Result<Boolean> manualMatchReceivable(
            @Parameter(description = "应收账款ID") @RequestParam @NotNull Long receivableId,
            @Parameter(description = "已收款ID") @RequestParam @NotNull Long receivedId) {
        boolean result = receivableService.manualMatchReceivable(receivableId, receivedId);
        return Result.success(result);
    }

    @Operation(summary = "更新应收账款状态")
    @PostMapping("/{id}/update-status")
    public Result<Boolean> updateReceivableStatus(
            @Parameter(description = "应收账款ID") @PathVariable @NotNull Long id,
            @Parameter(description = "已收金额") @RequestParam BigDecimal receivedAmount) {
        boolean result = receivableService.updateReceivableStatus(id, receivedAmount);
        return Result.success(result);
    }

    @Operation(summary = "计算逾期信息")
    @PostMapping("/{id}/calculate-overdue")
    public Result<Map<String, Object>> calculateOverdueInfo(
            @Parameter(description = "应收账款ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = receivableService.calculateOverdueInfo(id);
        return Result.success(result);
    }

    @Operation(summary = "获取应收账款分析数据")
    @GetMapping("/analysis")
    public Result<Map<String, Object>> getReceivableAnalysisData(
            @Parameter(description = "分析类型") @RequestParam String analysisType,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        Map<String, Object> result = receivableService.getReceivableAnalysisData(analysisType, startDate, endDate, projectId);
        return Result.success(result);
    }

    @Operation(summary = "导出应收账款数据")
    @GetMapping("/export")
    public Result<String> exportReceivableData(
            @Parameter(description = "账单编号") @RequestParam(required = false) String billNumber,
            @Parameter(description = "账单类型") @RequestParam(required = false) Integer billType,
            @Parameter(description = "账单状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "租户名称") @RequestParam(required = false) String tenantName,
            @Parameter(description = "合同编号") @RequestParam(required = false) String contractNumber,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String result = receivableService.exportReceivableData(
                billNumber, status, contractNumber, tenantName, billType, projectId, startDate, endDate);
        return Result.success(result);
    }

    @Operation(summary = "生成应收账款报表")
    @GetMapping("/report")
    public Result<Map<String, Object>> generateReceivableReport(
            @Parameter(description = "报表类型") @RequestParam String reportType,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        Map<String, Object> result = receivableService.generateReceivableReport(reportType, startDate, endDate, projectId);
        return Result.success(result);
    }

    @Operation(summary = "应收账款提醒")
    @GetMapping("/reminders")
    public Result<List<Map<String, Object>>> getReceivableReminders(
            @Parameter(description = "提醒类型") @RequestParam String reminderType,
            @Parameter(description = "天数") @RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> result = receivableService.getReceivableReminders(reminderType, days);
        return Result.success(result);
    }

}