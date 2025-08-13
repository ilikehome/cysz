package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Tenant;

import java.util.List;
import java.util.Map;

/**
 * 租户Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 分页查询租户列表
     *
     * @param pageQuery 分页查询参数
     * @param name 租户名称
     * @param number 租户编号
     * @param nature 租户性质
     * @param businessFormat 业务形态
     * @param status 租户状态
     * @param creditRating 信用等级
     * @param riskLevel 风险等级
     * @return 租户列表
     */
    PageResult<Map<String, Object>> getTenantPage(PageQuery pageQuery,
                                                  String name,
                                                  String number,
                                                  Integer nature,
                                                  Integer businessFormat,
                                                  Integer status,
                                                  Integer creditRating,
                                                  Integer riskLevel);

    /**
     * 获取租户详细信息
     *
     * @param tenantId 租户ID
     * @return 租户详细信息
     */
    Map<String, Object> getTenantDetail(Long tenantId);

    /**
     * 创建租户
     *
     * @param tenant 租户信息
     * @return 创建结果
     */
    boolean createTenant(Tenant tenant);

    /**
     * 更新租户
     *
     * @param tenant 租户信息
     * @return 更新结果
     */
    boolean updateTenant(Tenant tenant);

    /**
     * 删除租户
     *
     * @param tenantId 租户ID
     * @return 删除结果
     */
    boolean deleteTenant(Long tenantId);

    /**
     * 批量删除租户
     *
     * @param tenantIds 租户ID列表
     * @return 删除结果
     */
    boolean batchDeleteTenants(List<Long> tenantIds);

    /**
     * 获取租户统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getTenantStatistics();

    /**
     * 根据性质统计租户数量
     *
     * @return 性质统计
     */
    List<Map<String, Object>> getTenantCountByNature();

    /**
     * 根据业务形态统计租户数量
     *
     * @return 业务形态统计
     */
    List<Map<String, Object>> getTenantCountByBusinessFormat();

    /**
     * 根据信用等级统计租户数量
     *
     * @return 信用等级统计
     */
    List<Map<String, Object>> getTenantCountByCreditRating();

    /**
     * 根据风险等级统计租户数量
     *
     * @return 风险等级统计
     */
    List<Map<String, Object>> getTenantCountByRiskLevel();

    /**
     * 获取所有启用的租户
     *
     * @return 租户列表
     */
    List<Map<String, Object>> getEnabledTenants();

    /**
     * 验证租户名称是否存在
     *
     * @param name 租户名称
     * @param excludeId 排除的租户ID
     * @return 是否存在
     */
    boolean checkNameExists(String name, Long excludeId);

    /**
     * 验证租户编号是否存在
     *
     * @param number 租户编号
     * @param excludeId 排除的租户ID
     * @return 是否存在
     */
    boolean checkNumberExists(String number, Long excludeId);

    /**
     * 验证统一社会信用代码是否存在
     *
     * @param creditCode 统一社会信用代码
     * @param excludeId 排除的租户ID
     * @return 是否存在
     */
    boolean checkCreditCodeExists(String creditCode, Long excludeId);

    /**
     * 获取租户合同列表
     *
     * @param tenantId 租户ID
     * @return 合同列表
     */
    List<Map<String, Object>> getTenantContracts(Long tenantId);

    /**
     * 获取租户应收账款列表
     *
     * @param tenantId 租户ID
     * @return 应收账款列表
     */
    List<Map<String, Object>> getTenantReceivables(Long tenantId);

    /**
     * 获取租户已收款列表
     *
     * @param tenantId 租户ID
     * @return 已收款列表
     */
    List<Map<String, Object>> getTenantReceived(Long tenantId);

    /**
     * 租户风险评估
     *
     * @param tenantId 租户ID
     * @return 风险评估结果
     */
    Map<String, Object> assessTenantRisk(Long tenantId);

    /**
     * 租户业务分析
     *
     * @param tenantId 租户ID
     * @return 业务分析结果
     */
    Map<String, Object> analyzeTenantBusiness(Long tenantId);

    /**
     * 生成租户档案
     *
     * @param tenantId 租户ID
     * @return 租户档案
     */
    Map<String, Object> generateTenantProfile(Long tenantId);

    /**
     * 导出租户数据
     *
     * @param name 租户名称
     * @param number 租户编号
     * @param nature 租户性质
     * @param businessFormat 业务形态
     * @param status 租户状态
     * @param creditRating 信用等级
     * @param riskLevel 风险等级
     * @return 导出数据
     */
    List<Map<String, Object>> exportTenantData(String name,
                                               String number,
                                               Integer nature,
                                               Integer businessFormat,
                                               Integer status,
                                               Integer creditRating,
                                               Integer riskLevel);

}