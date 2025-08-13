package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Received;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 已收款Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface ReceivedMapper extends BaseMapper<Received> {

    /**
     * 分页查询已收款列表
     *
     * @param page 分页参数
     * @param keyword 关键词
     * @param status 状态
     * @param contractNumber 合同编号
     * @param tenantName 租户名称
     * @param billType 账单类型
     * @param paymentMethod 收款方式
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 已收款列表
     */
    Page<Map<String, Object>> selectReceivedPage(Page<Map<String, Object>> page,
                                                 @Param("keyword") String keyword,
                                                 @Param("status") Integer status,
                                                 @Param("contractNumber") String contractNumber,
                                                 @Param("tenantName") String tenantName,
                                                 @Param("billType") Integer billType,
                                                 @Param("paymentMethod") Integer paymentMethod,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 获取已收款统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "SUM(amount) as totalReceived, " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as unmatchedCount, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as matchedCount, " +
            "SUM(CASE WHEN status = 0 THEN amount ELSE 0 END) as unmatchedAmount, " +
            "SUM(CASE WHEN status = 1 THEN amount ELSE 0 END) as matchedAmount " +
            "FROM received WHERE deleted = 0")
    Map<String, Object> getReceivedStatistics();

    /**
     * 获取已收款趋势统计
     *
     * @param months 月数
     * @return 趋势统计
     */
    List<Map<String, Object>> getReceivedTrendStatistics(@Param("months") Integer months);

    /**
     * 根据收款方式统计已收款金额
     *
     * @return 收款方式统计
     */
    @Select("SELECT payment_method, SUM(amount) as amount, COUNT(*) as count " +
            "FROM received WHERE deleted = 0 " +
            "GROUP BY payment_method")
    List<Map<String, Object>> getReceivedAmountByPaymentMethod();

    /**
     * 根据账单类型统计已收款金额
     *
     * @return 账单类型统计
     */
    @Select("SELECT bill_type, SUM(amount) as amount, COUNT(*) as count " +
            "FROM received WHERE deleted = 0 " +
            "GROUP BY bill_type")
    List<Map<String, Object>> getReceivedAmountByBillType();

    /**
     * 根据状态统计已收款数量
     *
     * @return 状态统计
     */
    @Select("SELECT status, COUNT(*) as count, SUM(amount) as amount " +
            "FROM received WHERE deleted = 0 " +
            "GROUP BY status")
    List<Map<String, Object>> getReceivedCountByStatus();

    /**
     * 获取未匹配的已收款列表
     *
     * @return 未匹配列表
     */
    @Select("SELECT * FROM received " +
            "WHERE status = 0 AND deleted = 0 " +
            "ORDER BY received_date DESC")
    List<Map<String, Object>> getUnmatchedReceived();

    /**
     * 根据合同和金额查找可匹配的已收款
     *
     * @param contractId 合同ID
     * @param amount 金额
     * @param tolerance 容差
     * @return 可匹配的已收款
     */
    List<Map<String, Object>> findMatchableReceived(@Param("contractId") Long contractId,
                                                    @Param("amount") BigDecimal amount,
                                                    @Param("tolerance") BigDecimal tolerance);

    /**
     * 自动匹配已收款
     *
     * @param receivableId 应收账款ID
     * @param amount 金额
     * @param tolerance 容差
     * @return 匹配的已收款ID
     */
    Long autoMatchReceived(@Param("receivableId") Long receivableId,
                          @Param("amount") BigDecimal amount,
                          @Param("tolerance") BigDecimal tolerance);

    /**
     * 手动匹配已收款
     *
     * @param receivedId 已收款ID
     * @param receivableId 应收账款ID
     * @param matchType 匹配方式
     * @return 匹配结果
     */
    int manualMatchReceived(@Param("receivedId") Long receivedId,
                           @Param("receivableId") Long receivableId,
                           @Param("matchType") Integer matchType);

    /**
     * 取消匹配已收款
     *
     * @param receivedId 已收款ID
     * @return 取消结果
     */
    int unmatchReceived(@Param("receivedId") Long receivedId);

    /**
     * 批量导入已收款
     *
     * @param receivedList 已收款列表
     * @return 导入数量
     */
    int batchImportReceived(@Param("receivedList") List<Received> receivedList);

    /**
     * 验证银行流水号是否存在
     *
     * @param bankTransactionNo 银行流水号
     * @param excludeId 排除的已收款ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM received " +
            "WHERE bank_transaction_no = #{bankTransactionNo} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkBankTransactionNoExists(@Param("bankTransactionNo") String bankTransactionNo,
                                    @Param("excludeId") Long excludeId);

    /**
     * 获取已收款分析数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分析数据
     */
    Map<String, Object> getReceivedAnalysis(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    /**
     * 获取收款人统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 收款人统计
     */
    @Select("SELECT receiver, SUM(amount) as totalAmount, COUNT(*) as count " +
            "FROM received WHERE deleted = 0 " +
            "AND (#{startDate} IS NULL OR received_date >= #{startDate}) " +
            "AND (#{endDate} IS NULL OR received_date <= #{endDate}) " +
            "GROUP BY receiver " +
            "ORDER BY totalAmount DESC")
    List<Map<String, Object>> getReceiverStatistics(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    /**
     * 导出已收款数据
     *
     * @param keyword 关键词
     * @param status 状态
     * @param contractNumber 合同编号
     * @param tenantName 租户名称
     * @param billType 账单类型
     * @param paymentMethod 收款方式
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导出数据
     */
    List<Map<String, Object>> exportReceivedData(@Param("keyword") String keyword,
                                                 @Param("status") Integer status,
                                                 @Param("contractNumber") String contractNumber,
                                                 @Param("tenantName") String tenantName,
                                                 @Param("billType") Integer billType,
                                                 @Param("paymentMethod") Integer paymentMethod,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 获取收款对账报表
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 对账报表
     */
    List<Map<String, Object>> getReconciliationReport(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

}