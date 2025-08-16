package com.cysz.minimal.service;

import com.cysz.minimal.vo.account.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;

/**
 * 账务管理服务接口
 */
public interface AccountManagementService {

    /**
     * 获取应收记录
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param billStatus 账单状态
     * @return 应收记录列表
     */
    Map<String, Object> getReceivableRecords(int current, int size, String keyword, String billStatus);

    /**
     * 获取已收记录
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param billStatus 账单状态
     * @return 已收记录列表
     */
    Map<String, Object> getReceivedRecords(int current, int size, String keyword, String billStatus);

    /**
     * 获取银行流水记录
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param claimStatus 认领状态
     * @return 银行流水记录列表
     */
    Map<String, Object> getBankTransactions(int current, int size, String keyword, String claimStatus);

    /**
     * 获取应收已收分页数据
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param status 状态
     * @param contractNo 合同号
     * @param tenantName 租户名称
     * @return 分页数据
     */
    Map<String, Object> getReceivableReceivedPage(int current, int size, String keyword, String status, String contractNo, String tenantName);

    /**
     * 获取收款认领分页数据
     * @param current 当前页
     * @param size 页大小
     * @param keyword 关键词
     * @param claimStatus 认领状态
     * @return 分页数据
     */
    Map<String, Object> getPaymentClaimPage(int current, int size, String keyword, String claimStatus);

    /**
     * 认领收款
     * @param transactionId 交易ID
     * @param claimData 认领数据
     * @return 操作结果
     */
    Map<String, Object> claimPayment(Long transactionId, Map<String, Object> claimData);

    /**
     * 获取可认领的合同列表
     * @param amount 金额
     * @return 合同列表
     */
    List<Map<String, Object>> getClaimableContracts(Double amount);

    /**
     * 生成应收款
     * @param contractId 合同ID
     * @param period 期数
     * @return 操作结果
     */
    Map<String, Object> generateReceivables(String contractId, String period);

    /**
     * 初始化测试数据
     * @return 操作结果
     */
    Map<String, Object> initTestData();
}