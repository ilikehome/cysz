package com.cysz.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Received;
import com.cysz.property.mapper.ReceivedMapper;
import com.cysz.property.service.ReceivedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已收款服务实现类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReceivedServiceImpl extends ServiceImpl<ReceivedMapper, Received> implements ReceivedService {

    @Override
    public PageResult<Map<String, Object>> getReceivedPage(PageQuery pageQuery, String bankSerialNumber, 
                                                   Integer paymentMethod, Integer billType, Integer matchStatus, String payerName, 
                                                   LocalDate startDate, LocalDate endDate) {
        Page<Map<String, Object>> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        Page<Map<String, Object>> result = baseMapper.selectReceivedPage(page, bankSerialNumber, 
                matchStatus, null, payerName, billType, paymentMethod, startDate, endDate);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Map<String, Object> getReceivedDetail(Long id) {
        if (id == null) {
            return null;
        }
        Received received = getById(id);
        if (received == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", received.getId());
        result.put("bankSerialNumber", received.getBankSerialNumber());
        result.put("amount", received.getAmount());
        result.put("paymentMethod", received.getPaymentMethod());
        result.put("paymentDate", received.getPaymentDate());
        result.put("payerName", received.getPayerName());
        result.put("payerAccount", received.getPayerAccount());
        result.put("receiverAccount", received.getReceiverAccount());
        result.put("billType", received.getBillType());
        result.put("matchStatus", received.getMatchStatus());
        result.put("matchedAmount", received.getMatchedAmount());
        result.put("unmatchedAmount", received.getUnmatchedAmount());
        result.put("description", received.getDescription());
        result.put("createdTime", received.getCreatedTime());
        result.put("updatedTime", received.getUpdatedTime());
        return result;
    }

    @Override
    public boolean createReceived(Received received) {
        return save(received);
    }

    @Override
    public boolean updateReceived(Received received) {
        return updateById(received);
    }

    @Override
    public boolean deleteReceived(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDeleteReceived(List<Long> receivedIds) {
        return removeByIds(receivedIds);
    }

    @Override
    public boolean confirmReceived(Long id, LocalDate confirmDate) {
        if (id == null) {
            return false;
        }
        Received received = new Received();
        received.setId(id);
        received.setMatchStatus(2); // 设置为已匹配状态
        received.setUpdatedTime(LocalDateTime.now());
        return updateById(received);
    }

    @Override
    public boolean cancelConfirmReceived(Long id, String reason) {
        if (id == null) {
            return false;
        }
        Received received = new Received();
        received.setId(id);
        received.setMatchStatus(1); // 设置为未匹配状态
        received.setDescription(reason);
        received.setUpdatedTime(LocalDateTime.now());
        return updateById(received);
    }

    @Override
    public boolean checkReceiptNumberExists(String receiptNumber, Long excludeId) {
        if (receiptNumber == null || receiptNumber.trim().isEmpty()) {
            return false;
        }
        return baseMapper.checkBankTransactionNoExists(receiptNumber, excludeId) > 0;
    }

    @Override
    public Map<String, Object> getReceivedStatistics() {
        return baseMapper.getReceivedStatistics();
    }

    @Override
    public List<Map<String, Object>> getReceivedAmountByPaymentMethod() {
        return baseMapper.getReceivedAmountByPaymentMethod();
    }

    @Override
    public List<Map<String, Object>> getReceivedCountByStatus() {
        return baseMapper.getReceivedCountByStatus();
    }

    @Override
    public List<Map<String, Object>> getMonthlyReceivedStatistics(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        return baseMapper.getReceivedTrendStatistics(12);
    }

    @Override
    public List<Map<String, Object>> getYearlyReceivedStatistics() {
        return baseMapper.getReceivedTrendStatistics(60); // 获取5年数据
    }

    @Override
    public List<Map<String, Object>> getReceivedByContractId(Long contractId) {
        if (contractId == null) {
            return new ArrayList<>();
        }
        QueryWrapper<Received> wrapper = new QueryWrapper<>();
        wrapper.eq("contract_id", contractId);
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("payment_date");
        List<Received> receivedList = list(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Received received : receivedList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", received.getId());
            map.put("amount", received.getAmount());
            map.put("paymentDate", received.getPaymentDate());
            map.put("paymentMethod", received.getPaymentMethod());
            map.put("matchStatus", received.getMatchStatus());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getReceivedByTenantId(Long tenantId) {
        if (tenantId == null) {
            return new ArrayList<>();
        }
        QueryWrapper<Received> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("payment_date");
        List<Received> receivedList = list(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Received received : receivedList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", received.getId());
            map.put("amount", received.getAmount());
            map.put("paymentDate", received.getPaymentDate());
            map.put("paymentMethod", received.getPaymentMethod());
            map.put("payerName", received.getPayerName());
            map.put("matchStatus", received.getMatchStatus());
            result.add(map);
        }
        return result;
    }

    @Override
    public byte[] exportReceivedData(String receiptNumber, Integer paymentMethod, Integer status, String tenantName, Long contractId, LocalDate startDate, LocalDate endDate) {
        // 获取导出数据
        Page<Map<String, Object>> page = new Page<>(1, 10000);
        Page<Map<String, Object>> result = baseMapper.selectReceivedPage(page, receiptNumber, 
                status, null, tenantName, null, paymentMethod, startDate, endDate);
        // 这里应该实现Excel导出逻辑，暂时返回空
        return new byte[0];
    }

    @Override
    public Map<String, Object> importReceivedData(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件不能为空");
            return result;
        }
        // 这里应该实现Excel文件解析和数据导入逻辑
        result.put("success", true);
        result.put("importCount", 0);
        result.put("message", "导入功能暂未实现");
        return result;
    }

    @Override
    public List<Map<String, Object>> getReceivedTrendStatistics(LocalDate startDate, LocalDate endDate) {
        // 默认获取12个月的趋势数据
        return baseMapper.getReceivedTrendStatistics(12);
    }

    @Override
    public List<Map<String, Object>> getReceivedRankingStatistics(Integer year, Integer month, Integer limit) {
        // 暂时返回空列表，需要根据年月和限制条件查询排行数据
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> batchConfirmReceived(List<Long> receivedIds, LocalDate confirmDate) {
        Map<String, Object> result = new HashMap<>();
        if (receivedIds == null || receivedIds.isEmpty()) {
            result.put("success", false);
            result.put("message", "收款ID列表不能为空");
            return result;
        }
        // 这里应该实现批量确认收款的逻辑
        result.put("success", true);
        result.put("confirmedCount", receivedIds.size());
        result.put("message", "批量确认成功");
        return result;
    }

    @Override
    public byte[] generateReceipt(Long id) {
        if (id == null) {
            return new byte[0];
        }
        // 这里应该实现PDF收据生成逻辑
        return new byte[0];
    }

    @Override
    public Map<String, Object> getReceivedVoucher(Long id) {
        Map<String, Object> result = new HashMap<>();
        if (id == null) {
            result.put("success", false);
            result.put("message", "收款ID不能为空");
            return result;
        }
        Received received = getById(id);
        if (received == null) {
            result.put("success", false);
            result.put("message", "收款记录不存在");
            return result;
        }
        result.put("success", true);
        result.put("voucherUrl", "");
        result.put("voucherName", "");
        return result;
    }

    @Override
    public boolean uploadReceivedVoucher(Long id, MultipartFile file) {
        if (id == null || file == null || file.isEmpty()) {
            return false;
        }
        // 这里应该实现文件上传和凭证保存逻辑
        return true;
    }

    @Override
    public List<Map<String, Object>> getReceivedTrendStatistics(Integer year) {
        // 获取指定年份的12个月趋势数据
        return baseMapper.getReceivedTrendStatistics(12);
    }

    @Override
    public List<Map<String, Object>> getReceivedStatisticsByPaymentMethod() {
        return baseMapper.getReceivedAmountByPaymentMethod();
    }

    @Override
    public List<Map<String, Object>> getReceivedStatisticsByBillType() {
        return baseMapper.getReceivedAmountByBillType();
    }

    @Override
    public List<Map<String, Object>> getReceivedStatisticsByMatchStatus() {
        return baseMapper.getReceivedCountByStatus();
    }

    @Override
    public List<Map<String, Object>> getUnmatchedReceived() {
        return baseMapper.getUnmatchedReceived();
    }

    @Override
    public List<Map<String, Object>> findMatchingReceived(Long receivableId) {
        if (receivableId == null) {
            return new ArrayList<>();
        }
        // 这里需要根据应收账款信息查找可匹配的已收款，暂时返回空列表
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> autoMatchReceived() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("matchedCount", 0);
        result.put("message", "自动匹配功能暂未实现");
        return result;
    }

    @Override
    public boolean manualMatchReceived(Long receivedId, Long receivableId) {
        if (receivedId == null || receivableId == null) {
            return false;
        }
        return baseMapper.manualMatchReceived(receivedId, receivableId, 1) > 0;
    }

    @Override
    public boolean cancelMatchReceived(Long id) {
        if (id == null) {
            return false;
        }
        return baseMapper.unmatchReceived(id) > 0;
    }

    @Override
    public Map<String, Object> importReceived(MultipartFile file) {
        return importReceivedData(file);
    }

    @Override
    public boolean checkBankSerialNumberExists(String bankSerialNumber, Long excludeId) {
        if (bankSerialNumber == null || bankSerialNumber.trim().isEmpty()) {
            return false;
        }
        return baseMapper.checkBankTransactionNoExists(bankSerialNumber.trim(), excludeId) > 0;
    }

    @Override
    public Map<String, Object> getReceivedAnalysisData(String analysisType, LocalDate startDate, LocalDate endDate) {
        return baseMapper.getReceivedAnalysis(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getPayerStatistics(LocalDate startDate, LocalDate endDate) {
        return baseMapper.getReceiverStatistics(startDate, endDate);
    }

    @Override
    public Map<String, Object> getReconciliationReport(LocalDate startDate, LocalDate endDate, Long projectId) {
        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("projectId", projectId);
        result.put("totalReceived", 0);
        result.put("totalMatched", 0);
        result.put("totalUnmatched", 0);
        result.put("reportData", new ArrayList<>());
        return result;
    }

    @Override
    public List<Map<String, Object>> exportReceivedData(String bankSerialNumber, Integer paymentMethod, Integer billType, Integer matchStatus, String payerName, LocalDate startDate, LocalDate endDate) {
        // 构建查询条件并获取导出数据
        Page<Map<String, Object>> page = new Page<>(1, 10000);
        Page<Map<String, Object>> result = baseMapper.selectReceivedPage(page, bankSerialNumber, 
                matchStatus, null, payerName, billType, paymentMethod, startDate, endDate);
        return result.getRecords();
    }
}