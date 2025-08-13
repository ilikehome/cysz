package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Receivable;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 应收账款Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface ReceivableMapper extends BaseMapper<Receivable> {

    /**
     * 分页查询应收账款列表
     *
     * @param page 分页参数
     * @param keyword 关键词
     * @param status 状态
     * @param contractNumber 合同编号
     * @param tenantName 租户名称
     * @param billType 账单类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 应收账款列表
     */
    Page<Map<String, Object>> selectReceivablePage(Page<Map<String, Object>> page,
                                                   @Param("keyword") String keyword,
                                                   @Param("status") Integer status,
                                                   @Param("contractNumber") String contractNumber,
                                                   @Param("tenantName") String tenantName,
                                                   @Param("billType") Integer billType,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    /**
     * 获取应收账款统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "SUM(amount) as totalReceivable, " +
            "SUM(received_amount) as totalReceived, " +
            "SUM(remaining_amount) as totalRemaining, " +
            "SUM(CASE WHEN status = 3 THEN remaining_amount ELSE 0 END) as overdueAmount, " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as paidCount, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as overdueCount " +
            "FROM receivables WHERE deleted = 0")
    Map<String, Object> getReceivableStatistics();

    /**
     * 获取逾期应收账款
     *
     * @return 逾期应收账款列表
     */
    List<Map<String, Object>> getOverdueReceivables();

    /**
     * 获取应收账款趋势统计
     *
     * @param months 月数
     * @return 趋势统计
     */
    List<Map<String, Object>> getReceivableTrendStatistics(@Param("months") Integer months);

    /**
     * 根据账单类型统计应收金额
     *
     * @return 账单类型统计
     */
    @Select("SELECT bill_type, SUM(amount) as amount, SUM(remaining_amount) as remainingAmount " +
            "FROM receivables WHERE deleted = 0 " +
            "GROUP BY bill_type")
    List<Map<String, Object>> getReceivableAmountByBillType();

    /**
     * 根据状态统计应收账款数量
     *
     * @return 状态统计
     */
    @Select("SELECT status, COUNT(*) as count, SUM(remaining_amount) as amount " +
            "FROM receivables WHERE deleted = 0 " +
            "GROUP BY status")
    List<Map<String, Object>> getReceivableCountByStatus();

    /**
     * 生成应收账款
     *
     * @param contractId 合同ID
     * @param billType 账单类型
     * @param periodStart 期间开始
     * @param periodEnd 期间结束
     * @param amount 金额
     * @param dueDate 应收日期
     * @return 生成数量
     */
    int generateReceivable(@Param("contractId") Long contractId,
                          @Param("billType") Integer billType,
                          @Param("periodStart") LocalDate periodStart,
                          @Param("periodEnd") LocalDate periodEnd,
                          @Param("amount") BigDecimal amount,
                          @Param("dueDate") LocalDate dueDate);

    /**
     * 批量生成应收账款
     *
     * @param receivables 应收账款列表
     * @return 生成数量
     */
    int batchGenerateReceivables(@Param("receivables") List<Receivable> receivables);

    /**
     * 自动匹配应收账款
     *
     * @param receivedId 已收款ID
     * @param amount 金额
     * @return 匹配的应收账款ID
     */
    Long autoMatchReceivable(@Param("receivedId") Long receivedId,
                            @Param("amount") BigDecimal amount);

    /**
     * 手动匹配应收账款
     *
     * @param receivableId 应收账款ID
     * @param receivedId 已收款ID
     * @param amount 匹配金额
     * @return 匹配结果
     */
    int manualMatchReceivable(@Param("receivableId") Long receivableId,
                             @Param("receivedId") Long receivedId,
                             @Param("amount") BigDecimal amount);

    /**
     * 更新应收账款状态
     *
     * @param receivableId 应收账款ID
     * @param receivedAmount 已收金额
     * @return 更新结果
     */
    int updateReceivableStatus(@Param("receivableId") Long receivableId,
                              @Param("receivedAmount") BigDecimal receivedAmount);

    /**
     * 获取应收账款分析数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分析数据
     */
    Map<String, Object> getReceivableAnalysis(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    /**
     * 导出应收账款数据
     *
     * @param keyword 关键词
     * @param status 状态
     * @param contractNumber 合同编号
     * @param tenantName 租户名称
     * @param billType 账单类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导出数据
     */
    List<Map<String, Object>> exportReceivableData(@Param("keyword") String keyword,
                                                   @Param("status") Integer status,
                                                   @Param("contractNumber") String contractNumber,
                                                   @Param("tenantName") String tenantName,
                                                   @Param("billType") Integer billType,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

}