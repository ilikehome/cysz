package com.cysz.minimal.service;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.entity.Tenant;
import java.util.List;
import java.util.Map;

/**
 * 租户服务接口
 */
public interface TenantService {
    
    /**
     * 分页查询租户信息
     * @param current 当前页
     * @param size 每页大小
     * @param keyword 关键词搜索
     * @param tenantNature 租户性质
     * @param businessFormat 业态
     * @return 分页结果
     */
    PageResult<Tenant> getTenantPage(int current, int size, String keyword, String tenantNature, String businessFormat);
    
    /**
     * 根据ID查询租户
     * @param id 租户ID
     * @return 租户信息
     */
    Tenant getTenantById(Long id);
    
    /**
     * 创建租户
     * @param tenant 租户信息
     * @return 创建的租户
     */
    Tenant createTenant(Tenant tenant);
    
    /**
     * 更新租户
     * @param id 租户ID
     * @param tenant 租户信息
     * @return 更新的租户
     */
    Tenant updateTenant(Long id, Tenant tenant);
    
    /**
     * 删除租户
     * @param id 租户ID
     */
    void deleteTenant(Long id);
    
    /**
     * 获取租户风险统计
     * @return 风险统计数据
     */
    Map<String, Object> getRiskStats();
    
    /**
     * 获取租户风险列表
     * @param current 当前页
     * @param size 每页大小
     * @return 风险列表
     */
    PageResult<Map<String, Object>> getTenantRiskList(int current, int size);
    
    /**
     * 评估租户风险
     * @param tenantId 租户ID
     * @param creditScore 信用评分
     * @param businessStatus 经营状态
     * @param financialStatus 财务状态
     * @param complianceStatus 合规状态
     * @param riskNote 风险备注
     */
    void assessTenantRisk(Long tenantId, Integer creditScore, String businessStatus, 
                         String financialStatus, String complianceStatus, String riskNote);
    
    /**
     * 获取租户画像列表
     * @param current 当前页
     * @param size 每页大小
     * @return 画像列表
     */
    PageResult<Map<String, Object>> getTenantProfileList(int current, int size);
    
    /**
     * 获取租户画像详情
     * @param id 租户ID
     * @return 画像详情
     */
    Map<String, Object> getTenantProfileDetail(Long id);
    
    /**
     * 生成租户画像
     * @param tenantId 租户ID
     */
    void generateTenantProfile(Long tenantId);
    
    /**
     * 获取业态概览
     * @return 业态概览数据
     */
    Map<String, Object> getBusinessOverview();
    
    /**
     * 获取业态详情
     * @param businessType 业态类型
     * @return 业态详情
     */
    List<Map<String, Object>> getBusinessDetails(String businessType);
    
    /**
     * 获取业态趋势
     * @return 业态趋势数据
     */
    Map<String, Object> getBusinessTrends();
    
    /**
     * 获取业态建议
     * @return 业态建议
     */
    List<Map<String, Object>> getBusinessSuggestions();
    
    /**
     * 导出业态报告
     * @return 报告数据
     */
    byte[] exportBusinessReport();
}