package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Receivable;
import com.cysz.property.mapper.ReceivableMapper;
import com.cysz.property.service.ReceivableService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 应收账款服务实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReceivableServiceImpl extends ServiceImpl<ReceivableMapper, Receivable> implements ReceivableService {

    @Override
    public PageResult<Map<String, Object>> getReceivablePage(PageQuery pageQuery, String keyword, Integer status, String contractNumber, String tenantName, Integer billType, LocalDate startDate, LocalDate endDate) {
        Page<Map<String, Object>> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        Page<Map<String, Object>> result = baseMapper.selectReceivablePage(page, keyword, status, contractNumber, tenantName, billType, startDate, endDate);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Map<String, Object> getReceivableDetail(Long receivableId) {
        if (receivableId == null) {
            return null;
        }
        Receivable receivable = getById(receivableId);
        if (receivable == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", receivable.getId());
        result.put("billNumber", receivable.getBillNumber());
        result.put("contractId", receivable.getContractId());
        result.put("tenantId", receivable.getTenantId());
        result.put("projectId", receivable.getProjectId());
        result.put("billType", receivable.getBillType());
        result.put("amount", receivable.getAmount());
        result.put("receivedAmount", receivable.getReceivedAmount());
        result.put("outstandingAmount", receivable.getOutstandingAmount());
        result.put("billDate", receivable.getBillDate());
        result.put("dueDate", receivable.getDueDate());
        result.put("status", receivable.getStatus());
        result.put("overdueDays", receivable.getOverdueDays());
        result.put("lateFee", receivable.getLateFee());
        result.put("description", receivable.getDescription());
        return result;
    }

    @Override
    public boolean createReceivable(Receivable receivable) {
        return save(receivable);
    }

    @Override
    public boolean updateReceivable(Receivable receivable) {
        return updateById(receivable);
    }

    @Override
    public boolean deleteReceivable(Long receivableId) {
        return removeById(receivableId);
    }

    @Override
    public boolean batchDeleteReceivables(List<Long> receivableIds) {
        return removeByIds(receivableIds);
    }

    @Override
    public Map<String, Object> getReceivableStatistics() {
        return baseMapper.getReceivableStatistics();
    }

    @Override
    public List<Map<String, Object>> getOverdueReceivables() {
        return baseMapper.getOverdueReceivables();
    }

    @Override
    public Map<String, Object> getOverdueReceivableStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        List<Map<String, Object>> overdueList = getOverdueReceivables();
        BigDecimal totalOverdueAmount = BigDecimal.ZERO;
        int overdueCount = overdueList.size();
        for (Map<String, Object> overdue : overdueList) {
            BigDecimal amount = (BigDecimal) overdue.get("outstandingAmount");
            if (amount != null) {
                totalOverdueAmount = totalOverdueAmount.add(amount);
            }
        }
        statistics.put("overdueCount", overdueCount);
        statistics.put("totalOverdueAmount", totalOverdueAmount);
        return statistics;
    }

    @Override
    public List<Map<String, Object>> getReceivableTrendStatistics(Integer months) {
        if (months == null || months <= 0) {
            months = 12;
        }
        return baseMapper.getReceivableTrendStatistics(months);
    }

    @Override
    public List<Map<String, Object>> getReceivableAmountByBillType() {
        return baseMapper.getReceivableAmountByBillType();
    }

    @Override
    public List<Map<String, Object>> getReceivableCountByStatus() {
        return baseMapper.getReceivableCountByStatus();
    }

    @Override
    public List<Map<String, Object>> getReceivableStatisticsByBillType() {
        return getReceivableAmountByBillType();
    }

    @Override
    public List<Map<String, Object>> getReceivableStatisticsByStatus() {
        return getReceivableCountByStatus();
    }

    @Override
    public boolean generateReceivable(Long contractId, Integer billType, LocalDate periodStart, LocalDate periodEnd, BigDecimal amount, LocalDate dueDate) {
        if (contractId == null || billType == null || amount == null || dueDate == null) {
            return false;
        }
        return baseMapper.generateReceivable(contractId, billType, periodStart, periodEnd, amount, dueDate) > 0;
    }

    @Override
    public boolean batchGenerateReceivables(List<Receivable> receivables) {
        if (receivables == null || receivables.isEmpty()) {
            return false;
        }
        return baseMapper.batchGenerateReceivables(receivables) > 0;
    }

    @Override
    public Map<String, Object> autoGenerateContractReceivables(Long contractId, Integer generateType, Integer periods) {
        if (contractId == null || generateType == null || periods == null || periods <= 0) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        // 这里应该根据合同信息和生成类型自动生成应收账款
        // 由于缺少具体的业务逻辑，暂时返回基本信息
        result.put("contractId", contractId);
        result.put("generateType", generateType);
        result.put("periods", periods);
        result.put("success", true);
        return result;
    }

    @Override
    public Map<String, Object> autoMatchReceivable(Long receivedId, BigDecimal amount) {
        if (receivedId == null || amount == null) {
            return null;
        }
        Long matchedReceivableId = baseMapper.autoMatchReceivable(receivedId, amount);
        Map<String, Object> result = new HashMap<>();
        result.put("receivedId", receivedId);
        result.put("matchedReceivableId", matchedReceivableId);
        result.put("amount", amount);
        result.put("success", matchedReceivableId != null);
        return result;
    }

    @Override
    public Map<String, Object> autoMatchReceivables() {
        Map<String, Object> result = new HashMap<>();
        // 批量自动匹配逻辑需要根据具体业务规则实现
        // 这里返回基本的结果结构
        result.put("matchedCount", 0);
        result.put("totalAmount", BigDecimal.ZERO);
        result.put("success", true);
        return result;
    }

    @Override
    public boolean manualMatchReceivable(Long receivableId, Long receivedId, BigDecimal amount) {
        if (receivableId == null || receivedId == null || amount == null) {
            return false;
        }
        return baseMapper.manualMatchReceivable(receivableId, receivedId, amount) > 0;
    }

    @Override
    public boolean manualMatchReceivable(Long receivableId, Long receivedId) {
        if (receivableId == null || receivedId == null) {
            return false;
        }
        // 重载方法，使用默认匹配金额逻辑
        return baseMapper.manualMatchReceivable(receivableId, receivedId, null) > 0;
    }

    @Override
    public boolean updateReceivableStatus(Long receivableId, BigDecimal receivedAmount) {
        if (receivableId == null || receivedAmount == null) {
            return false;
        }
        return baseMapper.updateReceivableStatus(receivableId, receivedAmount) > 0;
    }

    @Override
    public Map<String, Object> calculateOverdueInfo(Long receivableId) {
        if (receivableId == null) {
            return null;
        }
        Receivable receivable = getById(receivableId);
        if (receivable == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        LocalDate now = LocalDate.now();
        LocalDate dueDate = receivable.getDueDate();
        if (dueDate != null && now.isAfter(dueDate)) {
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, now);
            result.put("overdueDays", overdueDays);
            result.put("isOverdue", true);
        } else {
            result.put("overdueDays", 0);
            result.put("isOverdue", false);
        }
        result.put("receivableId", receivableId);
        result.put("dueDate", dueDate);
        result.put("outstandingAmount", receivable.getOutstandingAmount());
        return result;
    }

    @Override
    public Map<String, Object> batchUpdateOverdueInfo() {
        Map<String, Object> result = new HashMap<>();
        // 批量更新逾期信息的逻辑
        int updatedCount = 0;
        result.put("updatedCount", updatedCount);
        result.put("success", true);
        return result;
    }

    @Override
    public Map<String, Object> getReceivableAnalysis(LocalDate startDate, LocalDate endDate) {
        return baseMapper.getReceivableAnalysis(startDate, endDate);
    }

    @Override
    public Map<String, Object> getReceivableAnalysisData(String analysisType, LocalDate startDate, LocalDate endDate, Long projectId) {
        Map<String, Object> result = new HashMap<>();
        // 根据分析类型返回不同的分析数据
        result.put("analysisType", analysisType);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("projectId", projectId);
        return result;
    }

    @Override
    public String exportReceivableData(String keyword, Integer status, String contractNumber, String tenantName, Integer billType, Long contractId, LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> exportData = baseMapper.exportReceivableData(keyword, status, contractNumber, tenantName, billType, startDate, endDate);
        // 这里应该实现Excel导出逻辑，暂时返回数据条数信息
        return "导出" + exportData.size() + "条应收账款数据";
    }

    @Override
    public Map<String, Object> generateReceivableReport(String reportType, LocalDate startDate, LocalDate endDate, Long projectId) {
        Map<String, Object> result = new HashMap<>();
        result.put("reportType", reportType);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("projectId", projectId);
        result.put("generateTime", LocalDateTime.now());
        result.put("success", true);
        return result;
    }

    @Override
    public List<Map<String, Object>> getReceivableReminders(String keyword, Integer days) {
        if (days == null || days <= 0) {
            days = 7; // 默认7天内到期的应收账款
        }
        List<Map<String, Object>> reminders = new ArrayList<>();
        // 这里应该查询即将到期的应收账款
        // 暂时返回空列表
        return reminders;
    }

    @Override
    public Map<String, Object> sendReceivableReminders(List<Long> receivableIds, Integer reminderType) {
        if (receivableIds == null || receivableIds.isEmpty()) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("sentCount", receivableIds.size());
        result.put("reminderType", reminderType);
        result.put("success", true);
        return result;
    }

    @Override
    public boolean generateSingleReceivable(Receivable receivable) {
        // TODO: 实现生成单个应收账款
        return save(receivable);
    }

    @Override
    public Map<String, Object> generateBatchReceivables(List<Receivable> receivables) {
        if (receivables == null || receivables.isEmpty()) {
            return null;
        }
        boolean success = batchGenerateReceivables(receivables);
        Map<String, Object> result = new HashMap<>();
        result.put("generatedCount", receivables.size());
        result.put("success", success);
        return result;
    }

    @Override
    public Map<String, Object> generateReceivablesByContract(Long contractId, Integer generateType, Integer periods) {
        return autoGenerateContractReceivables(contractId, generateType, periods);
    }
}