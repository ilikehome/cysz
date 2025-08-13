package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Tenant;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 租户Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 分页查询租户列表
     *
     * @param page 分页参数
     * @param name 租户名称
     * @param nature 租户性质
     * @param businessFormat 业务形态
     * @param status 租户状态
     * @param creditRating 信用等级
     * @param riskLevel 风险等级
     * @return 租户列表
     */
    Page<Tenant> selectTenantPage(Page<Tenant> page,
                                 @Param("name") String name,
                                 @Param("nature") Integer nature,
                                 @Param("businessFormat") Integer businessFormat,
                                 @Param("status") Integer status,
                                 @Param("creditRating") Integer creditRating,
                                 @Param("riskLevel") Integer riskLevel);

    /**
     * 获取租户统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT COUNT(*) as totalTenants, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as activeTenants, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as suspendedTenants, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as blacklistedTenants " +
            "FROM tenants WHERE deleted = 0")
    Map<String, Object> getTenantStatistics();

    /**
     * 根据性质统计租户数量
     *
     * @return 性质统计
     */
    @Select("SELECT nature, COUNT(*) as count " +
            "FROM tenants WHERE deleted = 0 AND status = 0 " +
            "GROUP BY nature")
    List<Map<String, Object>> getTenantCountByNature();

    /**
     * 根据业务形态统计租户数量
     *
     * @return 业务形态统计
     */
    @Select("SELECT business_format, COUNT(*) as count " +
            "FROM tenants WHERE deleted = 0 AND status = 0 " +
            "GROUP BY business_format")
    List<Map<String, Object>> getTenantCountByBusinessFormat();

    /**
     * 根据信用等级统计租户数量
     *
     * @return 信用等级统计
     */
    @Select("SELECT credit_rating, COUNT(*) as count " +
            "FROM tenants WHERE deleted = 0 AND status = 0 " +
            "GROUP BY credit_rating")
    List<Map<String, Object>> getTenantCountByCreditRating();

    /**
     * 根据风险等级统计租户数量
     *
     * @return 风险等级统计
     */
    @Select("SELECT risk_level, COUNT(*) as count " +
            "FROM tenants WHERE deleted = 0 AND status = 0 " +
            "GROUP BY risk_level")
    List<Map<String, Object>> getTenantCountByRiskLevel();

    /**
     * 获取租户风险评估数据
     *
     * @param tenantId 租户ID
     * @return 风险评估数据
     */
    Map<String, Object> getTenantRiskAssessment(@Param("tenantId") Long tenantId);

    /**
     * 获取租户经营分析数据
     *
     * @param tenantId 租户ID
     * @return 经营分析数据
     */
    Map<String, Object> getTenantBusinessAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 生成租户档案数据
     *
     * @param tenantId 租户ID
     * @return 档案数据
     */
    Map<String, Object> generateTenantProfile(@Param("tenantId") Long tenantId);

}