package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Received;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 已收款管理Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface ReceivedService extends IService<Received> {

    /**
     * 分页查询已收款列表
     */
    PageResult<Map<String, Object>> getReceivedPage(PageQuery pageQuery, String bankSerialNumber, 
                                                   Integer paymentMethod, Integer billType, Integer matchStatus, 
                                                   String payerName, LocalDate startDate, LocalDate endDate);

    /**
     * 根据ID查询已收款详情
     */
    Map<String, Object> getReceivedDetail(Long id);

    /**
     * 创建已收款记录
     */
    boolean createReceived(Received received);

    /**
     * 更新已收款记录
     */
    boolean updateReceived(Received received);

    /**
     * 删除已收款记录
     */
    boolean deleteReceived(Long id);

    /**
     * 批量删除已收款记录
     */
    boolean batchDeleteReceived(List<Long> receivedIds);

    /**
     * 确认收款
     */
    boolean confirmReceived(Long id, LocalDate confirmDate);

    /**
     * 取消确认收款
     */
    boolean cancelConfirmReceived(Long id, String reason);

    /**
     * 验证收据编号是否存在
     */
    boolean checkReceiptNumberExists(String receiptNumber, Long excludeId);

    /**
     * 获取已收款统计信息
     */
    Map<String, Object> getReceivedStatistics();

    /**
     * 根据支付方式统计已收款金额
     */
    List<Map<String, Object>> getReceivedAmountByPaymentMethod();

    /**
     * 根据状态统计已收款数量
     */
    List<Map<String, Object>> getReceivedCountByStatus();

    /**
     * 获取月度收款统计
     */
    List<Map<String, Object>> getMonthlyReceivedStatistics(Integer year);

    /**
     * 获取年度收款统计
     */
    List<Map<String, Object>> getYearlyReceivedStatistics();

    /**
     * 根据合同ID查询已收款列表
     */
    List<Map<String, Object>> getReceivedByContractId(Long contractId);

    /**
     * 根据租户ID查询已收款列表
     */
    List<Map<String, Object>> getReceivedByTenantId(Long tenantId);

    /**
     * 导出已收款数据
     */
    byte[] exportReceivedData(String receiptNumber, Integer paymentMethod, Integer status, 
                             String tenantName, Long contractId, LocalDate startDate, LocalDate endDate);

    /**
     * 导入已收款数据
     */
    Map<String, Object> importReceivedData(MultipartFile file);

    /**
     * 获取收款趋势统计
     */
    List<Map<String, Object>> getReceivedTrendStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 获取收款排行统计
     */
    List<Map<String, Object>> getReceivedRankingStatistics(Integer year, Integer month, Integer limit);

    /**
     * 批量确认收款
     */
    Map<String, Object> batchConfirmReceived(List<Long> receivedIds, LocalDate confirmDate);

    /**
     * 生成收据
     */
    byte[] generateReceipt(Long id);

    /**
     * 获取收款凭证
     */
    Map<String, Object> getReceivedVoucher(Long id);

    /**
     * 上传收款凭证
     */
    boolean uploadReceivedVoucher(Long id, MultipartFile file);

    /**
     * 获取已收款趋势统计
     */
    List<Map<String, Object>> getReceivedTrendStatistics(Integer year);

    /**
     * 根据收款方式统计已收款
     */
    List<Map<String, Object>> getReceivedStatisticsByPaymentMethod();

    /**
     * 根据账单类型统计已收款
     */
    List<Map<String, Object>> getReceivedStatisticsByBillType();

    /**
     * 根据匹配状态统计已收款
     */
    List<Map<String, Object>> getReceivedStatisticsByMatchStatus();

    /**
     * 获取未匹配的已收款列表
     */
    List<Map<String, Object>> getUnmatchedReceived();

    /**
     * 查找匹配的已收款
     */
    List<Map<String, Object>> findMatchingReceived(Long receivableId);

    /**
     * 自动匹配已收款
     */
    Map<String, Object> autoMatchReceived();

    /**
     * 手动匹配已收款
     */
    boolean manualMatchReceived(Long receivedId, Long receivableId);

    /**
     * 取消匹配已收款
     */
    boolean cancelMatchReceived(Long id);

    /**
     * 批量导入已收款
     */
    Map<String, Object> importReceived(MultipartFile file);

    /**
     * 验证银行流水号是否存在
     */
    boolean checkBankSerialNumberExists(String bankSerialNumber, Long excludeId);

    /**
     * 获取已收款分析数据
     */
    Map<String, Object> getReceivedAnalysisData(String analysisType, LocalDate startDate, LocalDate endDate);

    /**
     * 收款人统计
     */
    List<Map<String, Object>> getPayerStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 导出已收款数据
     */
    List<Map<String, Object>> exportReceivedData(String bankSerialNumber, Integer paymentMethod, Integer billType, Integer matchStatus, String payerName, LocalDate startDate, LocalDate endDate);

    /**
     * 收款对账报表
     */
    Map<String, Object> getReconciliationReport(LocalDate startDate, LocalDate endDate, Long projectId);
}