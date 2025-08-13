package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Receivable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 应收账款Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface ReceivableService extends IService<Receivable> {

    /**
     * 分页查询应收账款列表
     *
     * @param pageQuery 分页查询参数
     * @param keyword 关键词
     * @param status 状态
     * @param contractNumber 合同编号
     * @param tenantName 租户名称
     * @param billType 账单类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 应收账款列表
     */
    PageResult<Map<String, Object>> getReceivablePage(PageQuery pageQuery,
                                                      String keyword,
                                                      Integer status,
                                                      String contractNumber,
                                                      String tenantName,
                                                      Integer billType,
                                                      LocalDate startDate,
                                                      LocalDate endDate);

    /**
     * 获取应收账款详细信息
     *
     * @param receivableId 应收账款ID
     * @return 应收账款详细信息
     */
    Map<String, Object> getReceivableDetail(Long receivableId);

    /**
     * 创建应收账款
     *
     * @param receivable 应收账款信息
     * @return 创建结果
     */
    boolean createReceivable(Receivable receivable);

    /**
     * 更新应收账款
     *
     * @param receivable 应收账款信息
     * @return 更新结果
     */
    boolean updateReceivable(Receivable receivable);

    /**
     * 删除应收账款
     *
     * @param receivableId 应收账款ID
     * @return 删除结果
     */
    boolean deleteReceivable(Long receivableId);

    /**
     * 批量删除应收账款
     *
     * @param receivableIds 应收账款ID列表
     * @return 删除结果
     */
    boolean batchDeleteReceivables(List<Long> receivableIds);

    /**
     * 获取应收账款统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getReceivableStatistics();

    /**
     * 获取逾期应收账款
     *
     * @return 逾期应收账款列表
     */
    List<Map<String, Object>> getOverdueReceivables();

    /**
     * 获取逾期应收账款统计
     *
     * @return 逾期应收账款统计
     */
    Map<String, Object> getOverdueReceivableStatistics();

    /**
     * 获取应收账款趋势统计
     *
     * @param months 月数
     * @return 趋势统计
     */
    List<Map<String, Object>> getReceivableTrendStatistics(Integer months);

    /**
     * 根据账单类型统计应收金额
     *
     * @return 账单类型统计
     */
    List<Map<String, Object>> getReceivableAmountByBillType();

    /**
     * 根据状态统计应收账款数量
     *
     * @return 状态统计
     */
    List<Map<String, Object>> getReceivableCountByStatus();

    /**
     * 根据账单类型统计应收账款
     *
     * @return 账单类型统计
     */
    List<Map<String, Object>> getReceivableStatisticsByBillType();

    /**
     * 根据状态统计应收账款
     *
     * @return 状态统计
     */
    List<Map<String, Object>> getReceivableStatisticsByStatus();

    /**
     * 生成应收账款
     *
     * @param contractId 合同ID
     * @param billType 账单类型
     * @param periodStart 期间开始
     * @param periodEnd 期间结束
     * @param amount 金额
     * @param dueDate 应收日期
     * @return 生成结果
     */
    boolean generateReceivable(Long contractId,
                              Integer billType,
                              LocalDate periodStart,
                              LocalDate periodEnd,
                              BigDecimal amount,
                              LocalDate dueDate);

    /**
     * 生成单个应收账款
     *
     * @param receivable 应收账款信息
     * @return 生成结果
     */
    boolean generateSingleReceivable(Receivable receivable);

    /**
     * 批量生成应收账款
     *
     * @param receivables 应收账款列表
     * @return 生成结果
     */
    boolean batchGenerateReceivables(List<Receivable> receivables);

    /**
     * 批量生成应收账款（别名）
     *
     * @param receivables 应收账款列表
     * @return 生成结果
     */
    Map<String, Object> generateBatchReceivables(List<Receivable> receivables);

    /**
     * 根据合同生成应收账款
     *
     * @param contractId 合同ID
     * @param generateType 生成类型
     * @param periods 期数
     * @return 生成结果
     */
    Map<String, Object> generateReceivablesByContract(Long contractId, Integer generateType, Integer periods);

    /**
     * 自动生成合同应收账款
     *
     * @param contractId 合同ID
     * @param generateType 生成类型（1-按月，2-按季，3-按年）
     * @param periods 期数
     * @return 生成结果
     */
    Map<String, Object> autoGenerateContractReceivables(Long contractId, Integer generateType, Integer periods);

    /**
     * 自动匹配应收账款
     *
     * @param receivedId 已收款ID
     * @param amount 金额
     * @return 匹配结果
     */
    Map<String, Object> autoMatchReceivable(Long receivedId, BigDecimal amount);

    /**
     * 自动匹配应收账款（批量）
     *
     * @return 匹配结果
     */
    Map<String, Object> autoMatchReceivables();

    /**
     * 手动匹配应收账款
     *
     * @param receivableId 应收账款ID
     * @param receivedId 已收款ID
     * @param amount 匹配金额
     * @return 匹配结果
     */
    boolean manualMatchReceivable(Long receivableId, Long receivedId, BigDecimal amount);

    /**
     * 手动匹配应收账款（重载方法）
     *
     * @param receivableId 应收账款ID
     * @param receivedId 已收款ID
     * @return 匹配结果
     */
    boolean manualMatchReceivable(Long receivableId, Long receivedId);

    /**
     * 更新应收账款状态
     *
     * @param receivableId 应收账款ID
     * @param receivedAmount 已收金额
     * @return 更新结果
     */
    boolean updateReceivableStatus(Long receivableId, BigDecimal receivedAmount);

    /**
     * 计算逾期天数和滞纳金
     *
     * @param receivableId 应收账款ID
     * @return 计算结果
     */
    Map<String, Object> calculateOverdueInfo(Long receivableId);

    /**
     * 批量更新逾期信息
     *
     * @return 更新结果
     */
    Map<String, Object> batchUpdateOverdueInfo();

    /**
     * 获取应收账款分析数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分析数据
     */
    Map<String, Object> getReceivableAnalysis(LocalDate startDate, LocalDate endDate);

    /**
     * 获取应收账款分析数据（扩展）
     *
     * @param analysisType 分析类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param projectId 项目ID
     * @return 分析数据
     */
    Map<String, Object> getReceivableAnalysisData(String analysisType, LocalDate startDate, LocalDate endDate, Long projectId);

    /**
     * 导出应收账款数据
     *
     * @param keyword 关键词
     * @param status 状态
     * @param contractNumber 合同编号
     * @param tenantName 租户名称
     * @param billType 账单类型
     * @param contractId 合同ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导出数据
     */
    String exportReceivableData(String keyword,
                               Integer status,
                               String contractNumber,
                               String tenantName,
                               Integer billType,
                               Long contractId,
                               LocalDate startDate,
                               LocalDate endDate);

    /**
     * 生成应收账款报表
     *
     * @param reportType 报表类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param projectId 项目ID
     * @return 报表数据
     */
    Map<String, Object> generateReceivableReport(String reportType,
                                                 LocalDate startDate,
                                                 LocalDate endDate,
                                                 Long projectId);

    /**
     * 应收账款提醒
     *
     * @param keyword 关键词
     * @param days 提前天数
     * @return 提醒列表
     */
    List<Map<String, Object>> getReceivableReminders(String keyword, Integer days);

    /**
     * 发送应收账款提醒
     *
     * @param receivableIds 应收账款ID列表
     * @param reminderType 提醒类型
     * @return 发送结果
     */
    Map<String, Object> sendReceivableReminders(List<Long> receivableIds, Integer reminderType);

}