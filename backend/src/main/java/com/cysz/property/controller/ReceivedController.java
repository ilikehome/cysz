package com.cysz.property.controller;

import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.common.Result;
import com.cysz.property.entity.Received;
import com.cysz.property.service.ReceivedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 已收款管理Controller
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Tag(name = "已收款管理", description = "已收款管理相关接口")
@RestController
@RequestMapping("/api/received")
@RequiredArgsConstructor
@Validated
public class ReceivedController {

    private final ReceivedService receivedService;

    @Operation(summary = "分页查询已收款列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> getReceivedPage(
            @Parameter(description = "分页查询参数") @Valid PageQuery pageQuery,
            @Parameter(description = "银行流水号") @RequestParam(required = false) String bankSerialNumber,
            @Parameter(description = "收款方式") @RequestParam(required = false) Integer paymentMethod,
            @Parameter(description = "账单类型") @RequestParam(required = false) Integer billType,
            @Parameter(description = "匹配状态") @RequestParam(required = false) Integer matchStatus,
            @Parameter(description = "收款人") @RequestParam(required = false) String payerName,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        PageResult<Map<String, Object>> result = receivedService.getReceivedPage(
                pageQuery, bankSerialNumber, paymentMethod, billType, matchStatus, payerName, startDate, endDate);
        return Result.success(result);
    }

    @Operation(summary = "获取已收款详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getReceivedDetail(
            @Parameter(description = "已收款ID") @PathVariable @NotNull Long id) {
        Map<String, Object> result = receivedService.getReceivedDetail(id);
        return Result.success(result);
    }

    @Operation(summary = "创建已收款")
    @PostMapping
    public Result<Boolean> createReceived(
            @Parameter(description = "已收款信息") @RequestBody @Valid Received received) {
        boolean result = receivedService.createReceived(received);
        return Result.success(result);
    }

    @Operation(summary = "更新已收款")
    @PutMapping("/{id}")
    public Result<Boolean> updateReceived(
            @Parameter(description = "已收款ID") @PathVariable @NotNull Long id,
            @Parameter(description = "已收款信息") @RequestBody @Valid Received received) {
        received.setId(id);
        boolean result = receivedService.updateReceived(received);
        return Result.success(result);
    }

    @Operation(summary = "删除已收款")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteReceived(
            @Parameter(description = "已收款ID") @PathVariable @NotNull Long id) {
        boolean result = receivedService.deleteReceived(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除已收款")
    @DeleteMapping
    public Result<Boolean> batchDeleteReceived(
            @Parameter(description = "已收款ID列表") @RequestBody List<Long> receivedIds) {
        boolean result = receivedService.batchDeleteReceived(receivedIds);
        return Result.success(result);
    }

    @Operation(summary = "获取已收款统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getReceivedStatistics() {
        Map<String, Object> result = receivedService.getReceivedStatistics();
        return Result.success(result);
    }

    @Operation(summary = "获取已收款趋势统计")
    @GetMapping("/statistics/trend")
    public Result<List<Map<String, Object>>> getReceivedTrendStatistics(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        List<Map<String, Object>> result = receivedService.getReceivedTrendStatistics(year);
        return Result.success(result);
    }

    @Operation(summary = "根据收款方式统计已收款")
    @GetMapping("/statistics/payment-method")
    public Result<List<Map<String, Object>>> getReceivedStatisticsByPaymentMethod() {
        List<Map<String, Object>> result = receivedService.getReceivedStatisticsByPaymentMethod();
        return Result.success(result);
    }

    @Operation(summary = "根据账单类型统计已收款")
    @GetMapping("/statistics/bill-type")
    public Result<List<Map<String, Object>>> getReceivedStatisticsByBillType() {
        List<Map<String, Object>> result = receivedService.getReceivedStatisticsByBillType();
        return Result.success(result);
    }

    @Operation(summary = "根据匹配状态统计已收款")
    @GetMapping("/statistics/match-status")
    public Result<List<Map<String, Object>>> getReceivedStatisticsByMatchStatus() {
        List<Map<String, Object>> result = receivedService.getReceivedStatisticsByMatchStatus();
        return Result.success(result);
    }

    @Operation(summary = "获取未匹配的已收款列表")
    @GetMapping("/unmatched")
    public Result<List<Map<String, Object>>> getUnmatchedReceived() {
        List<Map<String, Object>> result = receivedService.getUnmatchedReceived();
        return Result.success(result);
    }

    @Operation(summary = "查找匹配的已收款")
    @GetMapping("/find-match")
    public Result<List<Map<String, Object>>> findMatchingReceived(
            @Parameter(description = "应收账款ID") @RequestParam @NotNull Long receivableId) {
        List<Map<String, Object>> result = receivedService.findMatchingReceived(receivableId);
        return Result.success(result);
    }

    @Operation(summary = "自动匹配已收款")
    @PostMapping("/auto-match")
    public Result<Map<String, Object>> autoMatchReceived() {
        Map<String, Object> result = receivedService.autoMatchReceived();
        return Result.success(result);
    }

    @Operation(summary = "手动匹配已收款")
    @PostMapping("/manual-match")
    public Result<Boolean> manualMatchReceived(
            @Parameter(description = "已收款ID") @RequestParam @NotNull Long receivedId,
            @Parameter(description = "应收账款ID") @RequestParam @NotNull Long receivableId) {
        boolean result = receivedService.manualMatchReceived(receivedId, receivableId);
        return Result.success(result);
    }

    @Operation(summary = "取消匹配已收款")
    @PostMapping("/{id}/cancel-match")
    public Result<Boolean> cancelMatchReceived(
            @Parameter(description = "已收款ID") @PathVariable @NotNull Long id) {
        boolean result = receivedService.cancelMatchReceived(id);
        return Result.success(result);
    }

    @Operation(summary = "批量导入已收款")
    @PostMapping("/import")
    public Result<Map<String, Object>> importReceived(
            @Parameter(description = "导入文件") @RequestParam("file") MultipartFile file) {
        Map<String, Object> result = receivedService.importReceived(file);
        return Result.success(result);
    }

    @Operation(summary = "验证银行流水号是否存在")
    @GetMapping("/check-serial-number")
    public Result<Boolean> checkBankSerialNumberExists(
            @Parameter(description = "银行流水号") @RequestParam String bankSerialNumber,
            @Parameter(description = "排除的已收款ID") @RequestParam(required = false) Long excludeId) {
        boolean result = receivedService.checkBankSerialNumberExists(bankSerialNumber, excludeId);
        return Result.success(result);
    }

    @Operation(summary = "获取已收款分析数据")
    @GetMapping("/analysis")
    public Result<Map<String, Object>> getReceivedAnalysisData(
            @Parameter(description = "分析类型") @RequestParam String analysisType,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> result = receivedService.getReceivedAnalysisData(analysisType, startDate, endDate);
        return Result.success(result);
    }

    @Operation(summary = "收款人统计")
    @GetMapping("/payer-statistics")
    public Result<List<Map<String, Object>>> getPayerStatistics(
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Map<String, Object>> result = receivedService.getPayerStatistics(startDate, endDate);
        return Result.success(result);
    }

    @Operation(summary = "导出已收款数据")
    @GetMapping("/export")
    public Result<List<Map<String, Object>>> exportReceivedData(
            @Parameter(description = "银行流水号") @RequestParam(required = false) String bankSerialNumber,
            @Parameter(description = "收款方式") @RequestParam(required = false) Integer paymentMethod,
            @Parameter(description = "账单类型") @RequestParam(required = false) Integer billType,
            @Parameter(description = "匹配状态") @RequestParam(required = false) Integer matchStatus,
            @Parameter(description = "收款人") @RequestParam(required = false) String payerName,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Map<String, Object>> result = receivedService.exportReceivedData(
                bankSerialNumber, paymentMethod, billType, matchStatus, payerName, startDate, endDate);
        return Result.success(result);
    }

    @Operation(summary = "收款对账报表")
    @GetMapping("/reconciliation-report")
    public Result<Map<String, Object>> getReconciliationReport(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "项目ID") @RequestParam(required = false) Long projectId) {
        Map<String, Object> result = receivedService.getReconciliationReport(startDate, endDate, projectId);
        return Result.success(result);
    }

}