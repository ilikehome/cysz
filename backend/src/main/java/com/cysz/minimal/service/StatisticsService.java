package com.cysz.minimal.service;

import java.util.Map;

/**
 * 统计服务接口
 * 提供各种统计数据的业务逻辑
 */
public interface StatisticsService {

    /**
     * 获取仪表盘概览数据
     * @return 仪表盘数据
     */
    Map<String, Object> getDashboardData();

    /**
     * 获取项目统计数据
     * @return 项目统计数据
     */
    Map<String, Object> getProjectStatistics();

    /**
     * 获取租户统计数据
     * @return 租户统计数据
     */
    Map<String, Object> getTenantStatistics();

    /**
     * 获取单元统计数据
     * @return 单元统计数据
     */
    Map<String, Object> getUnitStatistics();

    /**
     * 获取合同统计数据
     * @return 合同统计数据
     */
    Map<String, Object> getContractStatistics();

    /**
     * 获取财务统计数据
     * @return 财务统计数据
     */
    Map<String, Object> getFinancialStatistics();

    /**
     * 获取运营分析数据
     * @return 运营分析数据
     */
    Map<String, Object> getOperationAnalysis();
}