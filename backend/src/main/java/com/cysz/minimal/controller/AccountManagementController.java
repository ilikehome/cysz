package com.cysz.minimal.controller;

import com.cysz.minimal.service.AccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountManagementController {
    
    @Autowired
    private AccountManagementService accountManagementService;
    
    // 获取应收记录列表
    @GetMapping("/receivable")
    public Map<String, Object> getReceivableRecords(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String billStatus) {
        return accountManagementService.getReceivableRecords(current, size, keyword, billStatus);
    }
    
    // 获取已收记录列表
    @GetMapping("/received")
    public Map<String, Object> getReceivedRecords(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String billStatus) {
        return accountManagementService.getReceivedRecords(current, size, keyword, billStatus);
    }
    
    // 获取银行流水记录
    @GetMapping("/bank-transactions")
    public Map<String, Object> getBankTransactions(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String claimStatus) {
        return accountManagementService.getBankTransactions(current, size, keyword, claimStatus);
    }
    
    // 应收已收管理
    @GetMapping("/receivable-received/page")
    public Map<String, Object> getReceivableReceivedPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String tenantName) {
        return accountManagementService.getReceivableReceivedPage(current, size, keyword, status, contractNo, tenantName);
    }
    
    // 收款认领管理
    @GetMapping("/payment-claim/page")
    public Map<String, Object> getPaymentClaimPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String claimStatus) {
        return accountManagementService.getPaymentClaimPage(current, size, keyword, claimStatus);
    }
    
    // 执行收款认领
    @PostMapping("/payment-claim/{transactionId}/claim")
    public Map<String, Object> claimPayment(
            @PathVariable Long transactionId,
            @RequestBody Map<String, Object> claimData) {
        return accountManagementService.claimPayment(transactionId, claimData);
    }
    
    // 获取可认领的合同列表
    @GetMapping("/claimable-contracts")
    public Map<String, Object> getClaimableContracts(
            @RequestParam(required = false) Double amount) {
        List<Map<String, Object>> contracts = accountManagementService.getClaimableContracts(amount);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("data", contracts);
        return result;
    }
    
    // 生成应收账款
    @PostMapping("/receivables/generate")
    public Map<String, Object> generateReceivables(@RequestBody Map<String, Object> request) {
        String contractId = (String) request.get("contractId");
        String period = (String) request.get("period");
        return accountManagementService.generateReceivables(contractId, period);
    }
    
    // 初始化测试数据
    @PostMapping("/init-test-data")
    public Map<String, Object> initTestData() {
        return accountManagementService.initTestData();
    }
    

}