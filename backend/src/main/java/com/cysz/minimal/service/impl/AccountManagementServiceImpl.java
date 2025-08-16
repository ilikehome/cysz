package com.cysz.minimal.service.impl;

import com.cysz.minimal.service.AccountManagementService;
import com.cysz.minimal.vo.account.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账务管理服务实现类
 */
@Service
public class AccountManagementServiceImpl implements AccountManagementService {

    // 模拟数据存储
    private static final List<ReceivableRecordVO> receivableRecords = new ArrayList<>();
    private static final List<ReceivedRecordVO> receivedRecords = new ArrayList<>();
    private static final List<BankTransactionVO> bankTransactions = new ArrayList<>();

    static {
        // 初始化模拟数据
        initMockData();
    }

    private static void initMockData() {
        // 初始化应收记录
        ReceivableRecordVO r1 = new ReceivableRecordVO();
        r1.setContractNo("R001");
        r1.setContractName("C001");
        r1.setTenantName("张三");
        r1.setUnitDescription("A座101");
        r1.setAmount(new BigDecimal("5000.00"));
        r1.setDueDate(LocalDate.of(2024, 1, 1));
        r1.setBillStatus("未收");
        receivableRecords.add(r1);
        
        ReceivableRecordVO r2 = new ReceivableRecordVO();
        r2.setContractNo("R002");
        r2.setContractName("C002");
        r2.setTenantName("李四");
        r2.setUnitDescription("A座102");
        r2.setAmount(new BigDecimal("4500.00"));
        r2.setDueDate(LocalDate.of(2024, 1, 1));
        r2.setBillStatus("已收");
        receivableRecords.add(r2);
        
        ReceivableRecordVO r3 = new ReceivableRecordVO();
        r3.setContractNo("R003");
        r3.setContractName("C003");
        r3.setTenantName("王五");
        r3.setUnitDescription("B座201");
        r3.setAmount(new BigDecimal("6000.00"));
        r3.setDueDate(LocalDate.of(2024, 1, 1));
        r3.setBillStatus("部分收款");
        receivableRecords.add(r3);
        
        ReceivableRecordVO r4 = new ReceivableRecordVO();
        r4.setContractNo("R004");
        r4.setContractName("C004");
        r4.setTenantName("赵六");
        r4.setUnitDescription("B座202");
        r4.setAmount(new BigDecimal("5500.00"));
        r4.setDueDate(LocalDate.of(2024, 2, 1));
        r4.setBillStatus("未收");
        receivableRecords.add(r4);
        
        ReceivableRecordVO r5 = new ReceivableRecordVO();
        r5.setContractNo("R005");
        r5.setContractName("C005");
        r5.setTenantName("钱七");
        r5.setUnitDescription("C座301");
        r5.setAmount(new BigDecimal("4800.00"));
        r5.setDueDate(LocalDate.of(2024, 2, 1));
        r5.setBillStatus("已收");
        receivableRecords.add(r5);

        // 初始化已收记录
        ReceivedRecordVO p1 = new ReceivedRecordVO();
        p1.setContractNo("P001");
        p1.setContractName("C002");
        p1.setTenantName("李四");
        p1.setUnitDescription("A座102");
        p1.setAmount(new BigDecimal("4500.00"));
        p1.setReceivedDate(LocalDate.of(2024, 1, 5));
        p1.setPaymentMethod("银行转账");
        receivedRecords.add(p1);
        
        ReceivedRecordVO p2 = new ReceivedRecordVO();
        p2.setContractNo("P002");
        p2.setContractName("C003");
        p2.setTenantName("王五");
        p2.setUnitDescription("B座201");
        p2.setAmount(new BigDecimal("3000.00"));
        p2.setReceivedDate(LocalDate.of(2024, 1, 10));
        p2.setPaymentMethod("现金");
        receivedRecords.add(p2);
        
        ReceivedRecordVO p3 = new ReceivedRecordVO();
        p3.setContractNo("P003");
        p3.setContractName("C005");
        p3.setTenantName("钱七");
        p3.setUnitDescription("C座301");
        p3.setAmount(new BigDecimal("4800.00"));
        p3.setReceivedDate(LocalDate.of(2024, 2, 3));
        p3.setPaymentMethod("银行转账");
        receivedRecords.add(p3);
        
        ReceivedRecordVO p4 = new ReceivedRecordVO();
        p4.setContractNo("P004");
        p4.setContractName("C001");
        p4.setTenantName("张三");
        p4.setUnitDescription("A座101");
        p4.setAmount(new BigDecimal("2500.00"));
        p4.setReceivedDate(LocalDate.of(2024, 1, 15));
        p4.setPaymentMethod("支付宝");
        receivedRecords.add(p4);

        // 初始化银行流水
        BankTransactionVO t1 = new BankTransactionVO();
        t1.setTransactionNo("T001");
        t1.setAmount(new BigDecimal("4500.00"));
        t1.setCreateTime(LocalDateTime.of(2024, 1, 5, 14, 30));
        t1.setTransactionType("转账收入");
        t1.setCounterparty("李四转账");
        t1.setClaimStatus("未认领");
        bankTransactions.add(t1);
        
        BankTransactionVO t2 = new BankTransactionVO();
        t2.setTransactionNo("T002");
        t2.setAmount(new BigDecimal("4800.00"));
        t2.setCreateTime(LocalDateTime.of(2024, 2, 3, 9, 15));
        t2.setTransactionType("转账收入");
        t2.setCounterparty("钱七转账");
        t2.setClaimStatus("未认领");
        bankTransactions.add(t2);
        
        BankTransactionVO t3 = new BankTransactionVO();
        t3.setTransactionNo("T003");
        t3.setAmount(new BigDecimal("2500.00"));
        t3.setCreateTime(LocalDateTime.of(2024, 1, 15, 16, 45));
        t3.setTransactionType("转账收入");
        t3.setCounterparty("张三转账");
        t3.setClaimStatus("未认领");
        bankTransactions.add(t3);
        
        BankTransactionVO t4 = new BankTransactionVO();
        t4.setTransactionNo("T004");
        t4.setAmount(new BigDecimal("5500.00"));
        t4.setCreateTime(LocalDateTime.of(2024, 2, 8, 11, 20));
        t4.setTransactionType("转账收入");
        t4.setCounterparty("赵六转账");
        t4.setClaimStatus("未认领");
        bankTransactions.add(t4);
        
        BankTransactionVO t5 = new BankTransactionVO();
        t5.setTransactionNo("T005");
        t5.setAmount(new BigDecimal("3200.00"));
        t5.setCreateTime(LocalDateTime.of(2024, 2, 12, 13, 10));
        t5.setTransactionType("转账收入");
        t5.setCounterparty("未知转账");
        t5.setClaimStatus("未认领");
        bankTransactions.add(t5);
    }

    @Override
    public Map<String, Object> getReceivableRecords(int current, int size, String keyword, String billStatus) {
        List<ReceivableRecordVO> filteredData = receivableRecords;
        
        // 关键词过滤
        if (keyword != null && !keyword.trim().isEmpty()) {
            filteredData = receivableRecords.stream()
                .filter(record -> {
                    String tenantName = record.getTenantName();
                    String unitName = record.getUnitName();
                    String contractId = record.getContractId();
                    return (tenantName != null && tenantName.contains(keyword)) ||
                           (unitName != null && unitName.contains(keyword)) ||
                           (contractId != null && contractId.contains(keyword));
                })
                .collect(Collectors.toList());
        }
        
        // 状态过滤
        if (billStatus != null && !billStatus.trim().isEmpty()) {
            filteredData = filteredData.stream()
                .filter(record -> billStatus.equals(record.getStatus()))
                .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = filteredData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<ReceivableRecordVO> pageData = start < total ? filteredData.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData);
        result.put("total", total);
        result.put("current", current);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Map<String, Object> getReceivedRecords(int current, int size, String keyword, String billStatus) {
        List<ReceivedRecordVO> filteredData = receivedRecords;
        
        // 关键词过滤
        if (keyword != null && !keyword.trim().isEmpty()) {
            filteredData = receivedRecords.stream()
                .filter(record -> {
                    String tenantName = record.getTenantName();
                    String unitName = record.getUnitName();
                    String contractId = record.getContractId();
                    return (tenantName != null && tenantName.contains(keyword)) ||
                           (unitName != null && unitName.contains(keyword)) ||
                           (contractId != null && contractId.contains(keyword));
                })
                .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = filteredData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<ReceivedRecordVO> pageData = start < total ? filteredData.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData);
        result.put("total", total);
        result.put("current", current);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Map<String, Object> getBankTransactions(int current, int size, String keyword, String claimStatus) {
        List<BankTransactionVO> filteredData = bankTransactions;
        
        // 关键词过滤
        if (keyword != null && !keyword.trim().isEmpty()) {
            filteredData = bankTransactions.stream()
                .filter(transaction -> {
                    String description = transaction.getDescription();
                    String transactionId = transaction.getTransactionId();
                    return (description != null && description.contains(keyword)) ||
                           (transactionId != null && transactionId.contains(keyword));
                })
                .collect(Collectors.toList());
        }
        
        // 状态过滤
        if (claimStatus != null && !claimStatus.trim().isEmpty()) {
            filteredData = filteredData.stream()
                .filter(transaction -> claimStatus.equals(transaction.getStatus()))
                .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = filteredData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<BankTransactionVO> pageData = start < total ? filteredData.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData);
        result.put("total", total);
        result.put("current", current);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Map<String, Object> getReceivableReceivedPage(int current, int size, String keyword, String status, String contractNo, String tenantName) {
        List<Map<String, Object>> allData = new ArrayList<>();
        
        // 添加应收记录
        for (ReceivableRecordVO record : receivableRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", record.getId());
            item.put("contractId", record.getContractId());
            item.put("tenantName", record.getTenantName());
            item.put("unitName", record.getUnitName());
            item.put("amount", record.getAmount());
            item.put("dueDate", record.getDueDate());
            item.put("status", record.getStatus());
            item.put("type", "应收");
            allData.add(item);
        }
        
        // 添加已收记录
        for (ReceivedRecordVO record : receivedRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", record.getId());
            item.put("contractId", record.getContractId());
            item.put("tenantName", record.getTenantName());
            item.put("unitName", record.getUnitName());
            item.put("amount", record.getAmount());
            item.put("receivedDate", record.getReceivedDate());
            item.put("paymentMethod", record.getPaymentMethod());
            item.put("type", "已收");
            allData.add(item);
        }
        
        // 多条件过滤
        if (keyword != null && !keyword.trim().isEmpty()) {
            allData = allData.stream()
                .filter(item -> {
                    String itemTenantName = (String) item.get("tenantName");
                    String itemUnitName = (String) item.get("unitName");
                    String itemContractId = (String) item.get("contractId");
                    return (itemTenantName != null && itemTenantName.contains(keyword)) ||
                           (itemUnitName != null && itemUnitName.contains(keyword)) ||
                           (itemContractId != null && itemContractId.contains(keyword));
                })
                .collect(Collectors.toList());
        }
        
        if (status != null && !status.trim().isEmpty()) {
            allData = allData.stream()
                .filter(item -> status.equals(item.get("status")))
                .collect(Collectors.toList());
        }
        
        if (contractNo != null && !contractNo.trim().isEmpty()) {
            allData = allData.stream()
                .filter(item -> {
                    String contractId = (String) item.get("contractId");
                    return contractId != null && contractId.contains(contractNo);
                })
                .collect(Collectors.toList());
        }
        
        if (tenantName != null && !tenantName.trim().isEmpty()) {
            allData = allData.stream()
                .filter(item -> {
                    String itemTenantName = (String) item.get("tenantName");
                    return itemTenantName != null && itemTenantName.contains(tenantName);
                })
                .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = allData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> pageData = start < total ? allData.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData);
        result.put("total", total);
        result.put("current", current);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Map<String, Object> getPaymentClaimPage(int current, int size, String keyword, String claimStatus) {
        List<BankTransactionVO> filteredData = bankTransactions;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            filteredData = bankTransactions.stream()
                .filter(transaction -> {
                    String description = transaction.getDescription();
                    String transactionId = transaction.getTransactionId();
                    return (description != null && description.contains(keyword)) ||
                           (transactionId != null && transactionId.contains(keyword));
                })
                .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = filteredData.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<BankTransactionVO> pageData = start < total ? filteredData.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData);
        result.put("total", total);
        result.put("current", current);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Map<String, Object> claimPayment(Long transactionId, Map<String, Object> claimData) {
        Map<String, Object> result = new HashMap<>();
        
        // 查找银行流水
        BankTransactionVO transaction = bankTransactions.stream()
            .filter(t -> t.getTransactionId().equals(transactionId.toString()))
            .findFirst()
            .orElse(null);
            
        if (transaction == null) {
            result.put("success", false);
            result.put("message", "银行流水不存在");
            return result;
        }
        
        if (!"未认领".equals(transaction.getStatus())) {
            result.put("success", false);
            result.put("message", "该流水已被认领");
            return result;
        }
        
        // 从认领数据中获取合同ID
        String contractId = (String) claimData.get("contractId");
        if (contractId == null) {
            result.put("success", false);
            result.put("message", "合同ID不能为空");
            return result;
        }
        
        // 查找应收记录
        ReceivableRecordVO receivable = receivableRecords.stream()
            .filter(r -> r.getContractId().equals(contractId))
            .findFirst()
            .orElse(null);
            
        if (receivable == null) {
            result.put("success", false);
            result.put("message", "合同应收记录不存在");
            return result;
        }
        
        // 更新银行流水状态
        transaction.setStatus("已认领");
        
        // 创建已收记录
        String receivedId = "P" + String.format("%03d", receivedRecords.size() + 1);
        ReceivedRecordVO receivedRecord = new ReceivedRecordVO();
        receivedRecord.setReceivedId(receivedId);
        receivedRecord.setContractId(contractId);
        receivedRecord.setTenantName(receivable.getTenantName());
        receivedRecord.setUnitName(receivable.getUnitName());
        receivedRecord.setAmount(transaction.getAmount());
        receivedRecord.setReceivedDate(transaction.getTransactionTime().toLocalDate());
        receivedRecord.setPaymentMethod("银行转账");
        receivedRecords.add(receivedRecord);
        
        // 更新应收记录状态
        if (transaction.getAmount().compareTo(receivable.getAmount()) >= 0) {
            receivable.setStatus("已收");
        } else {
            receivable.setStatus("部分收款");
        }
        
        result.put("success", true);
        result.put("message", "认领成功");
        result.put("data", receivedRecord);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getClaimableContracts(Double amount) {
        List<Map<String, Object>> contracts = new ArrayList<>();
        
        // 模拟合同数据
        contracts.add(createContractMap("C001", "张三", "A座101", new BigDecimal("5000.00")));
        contracts.add(createContractMap("C002", "李四", "A座102", new BigDecimal("4500.00")));
        contracts.add(createContractMap("C003", "王五", "B座201", new BigDecimal("6000.00")));
        contracts.add(createContractMap("C004", "赵六", "B座202", new BigDecimal("5500.00")));
        contracts.add(createContractMap("C005", "钱七", "C座301", new BigDecimal("4800.00")));
        
        // 如果提供了金额，过滤匹配的合同
        if (amount != null) {
            BigDecimal targetAmount = new BigDecimal(amount.toString());
            contracts = contracts.stream()
                .filter(contract -> {
                    BigDecimal contractAmount = (BigDecimal) contract.get("amount");
                    return contractAmount.compareTo(targetAmount) == 0;
                })
                .collect(Collectors.toList());
        }
        
        return contracts;
    }

    private Map<String, Object> createContractMap(String contractId, String tenantName, String unitName, BigDecimal amount) {
        Map<String, Object> contract = new HashMap<>();
        contract.put("contractId", contractId);
        contract.put("tenantName", tenantName);
        contract.put("unitName", unitName);
        contract.put("amount", amount);
        contract.put("startDate", LocalDate.of(2024, 1, 1));
        contract.put("endDate", LocalDate.of(2024, 12, 31));
        return contract;
    }

    @Override
    public Map<String, Object> generateReceivables(String contractId, String period) {
        Map<String, Object> result = new HashMap<>();
        
        // 查找合同
        List<Map<String, Object>> contracts = getClaimableContracts(null);
        Map<String, Object> contract = contracts.stream()
            .filter(c -> c.get("contractId").equals(contractId))
            .findFirst()
            .orElse(null);
            
        if (contract == null) {
            result.put("success", false);
            result.put("message", "合同不存在");
            return result;
        }
        
        // 生成应收记录
        String receivableId = "R" + String.format("%03d", receivableRecords.size() + 1);
        ReceivableRecordVO receivable = new ReceivableRecordVO();
        receivable.setReceivableId(receivableId);
        receivable.setContractId(contractId);
        receivable.setTenantName((String) contract.get("tenantName"));
        receivable.setUnitName((String) contract.get("unitName"));
        receivable.setAmount((BigDecimal) contract.get("amount"));
        receivable.setDueDate(LocalDate.now().plusMonths(1)); // 下个月到期
        receivable.setStatus("未收");
        
        receivableRecords.add(receivable);
        
        result.put("success", true);
        result.put("message", "应收款生成成功");
        result.put("data", receivable);
        
        return result;
    }

    @Override
    public Map<String, Object> initTestData() {
        Map<String, Object> result = new HashMap<>();
        
        // 清空现有数据
        receivableRecords.clear();
        receivedRecords.clear();
        bankTransactions.clear();
        
        // 重新初始化数据
        initMockData();
        
        result.put("success", true);
        result.put("message", "测试数据初始化成功");
        result.put("receivableCount", receivableRecords.size());
        result.put("receivedCount", receivedRecords.size());
        result.put("transactionCount", bankTransactions.size());
        
        return result;
    }
}