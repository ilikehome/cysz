package com.cysz.minimal.service.impl;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.entity.Tenant;
import com.cysz.minimal.enums.BrandQualification;
import com.cysz.minimal.enums.TenantNature;
import com.cysz.minimal.exception.BusinessException;
import com.cysz.minimal.service.TenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.mapper.TenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 租户服务实现类
 */
@Service
public class TenantServiceImpl implements TenantService {
    
    @Autowired
    private TenantMapper tenantMapper;
    
    /**
     * 将前端中文租户性质转换为数据库英文值
     */
    private String convertTenantNatureToDb(String tenantNature) {
        return TenantNature.convertToDbCode(tenantNature);
    }
    
    /**
     * 将数据库英文租户性质转换为前端中文值
     */
    private String convertTenantNatureFromDb(String tenantNature) {
        return TenantNature.convertToDisplayName(tenantNature);
    }
    
    /**
     * 将前端中文品牌资质转换为数据库英文值
     */
    private String convertBrandQualificationToDb(String brandQualification) {
        return BrandQualification.convertToDbCode(brandQualification);
    }
    
    /**
     * 将数据库英文品牌资质转换为前端中文值
     */
    private String convertBrandQualificationFromDb(String brandQualification) {
        return BrandQualification.convertToDisplayName(brandQualification);
    }
    
    /**
     * 处理从数据库查询出的租户对象，转换枚举值
     */
    private Tenant convertTenantFromDb(Tenant tenant) {
        if (tenant != null) {
            tenant.setTenantNature(convertTenantNatureFromDb(tenant.getTenantNature()));
            tenant.setBrandQualification(convertBrandQualificationFromDb(tenant.getBrandQualification()));
        }
        return tenant;
    }
    
    /**
     * 处理要保存到数据库的租户对象，转换枚举值
     */
    private Tenant convertTenantToDb(Tenant tenant) {
        if (tenant != null) {
            Tenant dbTenant = new Tenant();
            // 复制所有属性
            dbTenant.setId(tenant.getId());
            dbTenant.setTenantName(tenant.getTenantName());
            dbTenant.setTenantNature(convertTenantNatureToDb(tenant.getTenantNature()));
            dbTenant.setEnterpriseNature(tenant.getEnterpriseNature());
            dbTenant.setSocialCreditCode(tenant.getSocialCreditCode());
            dbTenant.setTaxpayerId(tenant.getTaxpayerId());
            dbTenant.setBusinessRegistrationNumber(tenant.getBusinessRegistrationNumber());
            dbTenant.setIndividualLicenseNumber(tenant.getIndividualLicenseNumber());
            dbTenant.setBrand(tenant.getBrand());
            dbTenant.setBrandQualification(convertBrandQualificationToDb(tenant.getBrandQualification()));
            dbTenant.setBusinessFormat(tenant.getBusinessFormat());
            dbTenant.setBusinessScope(tenant.getBusinessScope());
            dbTenant.setLegalPersonName(tenant.getLegalPersonName());
            dbTenant.setLegalPersonPhone(tenant.getLegalPersonPhone());
            dbTenant.setLegalPersonIdCard(tenant.getLegalPersonIdCard());
            dbTenant.setFinanceContact(tenant.getFinanceContact());
            dbTenant.setFinancePhone(tenant.getFinancePhone());
            dbTenant.setPayerName(tenant.getPayerName());
            dbTenant.setPaymentAccount(tenant.getPaymentAccount());
            dbTenant.setRemark(tenant.getRemark());
            dbTenant.setCreateTime(tenant.getCreateTime());
            dbTenant.setUpdateTime(tenant.getUpdateTime());
            return dbTenant;
        }
        return null;
    }
    
    @Override
    public PageResult<Tenant> getTenantPage(int current, int size, String keyword, String tenantNature, String businessFormat) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        
        // 添加搜索条件
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("tenant_name", keyword);
        }
        
        if (StringUtils.hasText(tenantNature)) {
            queryWrapper.eq("tenant_nature", convertTenantNatureToDb(tenantNature)); // 转换为数据库值
        }
        
        if (StringUtils.hasText(businessFormat)) {
            queryWrapper.eq("business_format", businessFormat);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        // 创建分页对象
        Page<Tenant> page = new Page<>(current, size);
        
        // 执行分页查询
        IPage<Tenant> pageResult = tenantMapper.selectPage(page, queryWrapper);
        
        // 转换查询结果中的枚举值
        List<Tenant> records = pageResult.getRecords();
        for (Tenant tenant : records) {
            convertTenantFromDb(tenant);
        }
        
        return new PageResult<>(records, (int) pageResult.getTotal(), current, size);
    }
    
    @Override
    public Tenant getTenantById(Long id) {
        Tenant tenant = tenantMapper.selectById(id);
        return convertTenantFromDb(tenant);
    }
    
    @Override
    public Tenant createTenant(Tenant tenant) {
        // 验证必填字段
        if (!StringUtils.hasText(tenant.getTenantName())) {
            throw new BusinessException("租户名称不能为空");
        }
        if (!StringUtils.hasText(tenant.getTenantNature())) {
            throw new BusinessException("租户性质不能为空");
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 转换为数据库对象
        Tenant dbTenant = convertTenantToDb(tenant);
        dbTenant.setCreateTime(now);
        dbTenant.setUpdateTime(now);
        
        // 插入数据
        tenantMapper.insert(dbTenant);
        
        // 设置返回值
        tenant.setId(dbTenant.getId());
        tenant.setCreateTime(now);
        tenant.setUpdateTime(now);
        
        return tenant;
    }
    
    @Override
    public Tenant updateTenant(Long id, Tenant tenant) {
        // 检查租户是否存在
        Tenant existingTenant = getTenantById(id);
        if (existingTenant == null) {
            throw new BusinessException("租户不存在");
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 转换为数据库对象
        Tenant dbTenant = convertTenantToDb(tenant);
        dbTenant.setId(id);
        dbTenant.setUpdateTime(now);
        
        // 更新数据
        tenantMapper.updateById(dbTenant);
        
        // 设置返回值
        tenant.setId(id);
        tenant.setUpdateTime(now);
        return tenant;
    }
    
    @Override
    public void deleteTenant(Long id) {
        tenantMapper.deleteById(id);
    }
    
    @Override
    public Map<String, Object> getRiskStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总租户数
        Long totalTenants = tenantMapper.selectCount(null);
        int total = totalTenants != null ? totalTenants.intValue() : 0;
        stats.put("totalTenants", total);
        
        // 高风险租户数（模拟数据）
        stats.put("highRiskTenants", total / 10);
        
        // 中风险租户数（模拟数据）
        stats.put("mediumRiskTenants", total / 5);
        
        // 低风险租户数（模拟数据）
        stats.put("lowRiskTenants", total - 
                                   (Integer) stats.get("highRiskTenants") - 
                                   (Integer) stats.get("mediumRiskTenants"));
        
        return stats;
    }
    
    @Override
    public PageResult<Map<String, Object>> getTenantRiskList(int current, int size) {
        // 模拟风险数据
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tenant_name", "tenant_nature", "business_format", "create_time")
                   .orderByDesc("create_time");
        
        Page<Tenant> page = new Page<>(current, size);
        Page<Tenant> tenantPage = tenantMapper.selectPage(page, queryWrapper);
        
        List<Map<String, Object>> records = new ArrayList<>();
        Random random = new Random();
        
        for (Tenant tenant : tenantPage.getRecords()) {
            Map<String, Object> record = new HashMap<>();
            record.put("id", tenant.getId());
            record.put("tenant_name", tenant.getTenantName());
            record.put("tenant_nature", convertTenantNatureFromDb(tenant.getTenantNature()));
            record.put("business_format", tenant.getBusinessFormat());
            record.put("create_time", tenant.getCreateTime());
            // 移除riskLevel字段，因为数据库中不存在risk_level字段
            record.put("creditScore", 60 + random.nextInt(40)); // 60-100分
            record.put("lastAssessTime", LocalDateTime.now().minusDays(random.nextInt(30)));
            records.add(record);
        }
        
        return new PageResult<>(records, (int) tenantPage.getTotal(), current, size);
    }
    
    @Override
    public void assessTenantRisk(Long tenantId, Integer creditScore, String businessStatus, 
                                String financialStatus, String complianceStatus, String riskNote) {
        // 检查租户是否存在
        Tenant tenant = getTenantById(tenantId);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }
        
        // 这里可以实现风险评估逻辑，暂时只做记录
        // 实际项目中可能需要创建风险评估表来存储这些数据
        System.out.println("评估租户风险: " + tenantId + ", 信用评分: " + creditScore);
    }
    
    @Override
    public PageResult<Map<String, Object>> getTenantProfileList(int current, int size) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tenant_name", "tenant_nature", "business_format", "brand", "create_time")
                   .orderByDesc("create_time");
        
        Page<Tenant> page = new Page<>(current, size);
        Page<Tenant> tenantPage = tenantMapper.selectPage(page, queryWrapper);
        
        List<Map<String, Object>> records = new ArrayList<>();
        Random random = new Random();
        String[] tags = {"优质客户", "成长型", "稳定型", "潜力型", "关注型"};
        
        for (Tenant tenant : tenantPage.getRecords()) {
            Map<String, Object> record = new HashMap<>();
            record.put("id", tenant.getId());
            record.put("tenant_name", tenant.getTenantName());
            record.put("tenant_nature", convertTenantNatureFromDb(tenant.getTenantNature()));
            record.put("business_format", tenant.getBusinessFormat());
            record.put("brand", tenant.getBrand());
            record.put("create_time", tenant.getCreateTime());
            
            List<String> profileTags = new ArrayList<>();
            for (int i = 0; i < 2 + random.nextInt(3); i++) {
                profileTags.add(tags[random.nextInt(tags.length)]);
            }
            record.put("profileTags", profileTags);
            record.put("profileScore", 60 + random.nextInt(40));
            records.add(record);
        }
        
        return new PageResult<>(records, (int) tenantPage.getTotal(), current, size);
    }
    
    @Override
    public Map<String, Object> getTenantProfileDetail(Long id) {
        Tenant tenant = getTenantById(id);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("tenant", tenant);
        
        // 模拟画像数据
        Random random = new Random();
        profile.put("profileScore", 60 + random.nextInt(40));
        profile.put("businessStability", random.nextDouble());
        profile.put("paymentHistory", "良好");
        profile.put("growthPotential", "中等");
        
        return profile;
    }
    
    @Override
    public void generateTenantProfile(Long tenantId) {
        Tenant tenant = getTenantById(tenantId);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }
        
        // 这里可以实现画像生成逻辑
        System.out.println("生成租户画像: " + tenantId);
    }
    
    @Override
    public Map<String, Object> getBusinessOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        // 按业态统计租户数量
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("business_format", "COUNT(*) as count")
                   .isNotNull("business_format")
                   .groupBy("business_format");
        
        List<Map<String, Object>> businessStats = tenantMapper.selectMaps(queryWrapper);
        overview.put("businessStats", businessStats);
        
        // 总体统计
        Long totalTenants = tenantMapper.selectCount(null);
        overview.put("totalTenants", totalTenants != null ? totalTenants.intValue() : 0);
        
        return overview;
    }
    
    @Override
    public List<Map<String, Object>> getBusinessDetails(String businessType) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(businessType)) {
            queryWrapper.eq("business_format", businessType);
        }
        queryWrapper.orderByDesc("create_time");
        
        List<Tenant> tenants = tenantMapper.selectList(queryWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Tenant tenant : tenants) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", tenant.getId());
            map.put("tenant_name", tenant.getTenantName());
            map.put("tenant_nature", convertTenantNatureFromDb(tenant.getTenantNature()));
            map.put("business_format", tenant.getBusinessFormat());
            map.put("brand", tenant.getBrand());
            map.put("brand_qualification", convertBrandQualificationFromDb(tenant.getBrandQualification()));
            map.put("create_time", tenant.getCreateTime());
            map.put("update_time", tenant.getUpdateTime());
            result.add(map);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getBusinessTrends() {
        Map<String, Object> trends = new HashMap<>();
        
        // 模拟趋势数据
        List<Map<String, Object>> monthlyTrends = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 11; i >= 0; i--) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", LocalDateTime.now().minusMonths(i).getMonth().toString());
            monthData.put("newTenants", 5 + random.nextInt(15));
            monthData.put("activeTenants", 50 + random.nextInt(50));
            monthlyTrends.add(monthData);
        }
        
        trends.put("monthlyTrends", monthlyTrends);
        return trends;
    }
    
    @Override
    public List<Map<String, Object>> getBusinessSuggestions() {
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        // 模拟建议数据
        Map<String, Object> suggestion1 = new HashMap<>();
        suggestion1.put("title", "优化餐饮业态布局");
        suggestion1.put("content", "建议增加快餐类租户，满足快节奏消费需求");
        suggestion1.put("priority", "高");
        suggestions.add(suggestion1);
        
        Map<String, Object> suggestion2 = new HashMap<>();
        suggestion2.put("title", "发展新兴业态");
        suggestion2.put("content", "考虑引入新零售、体验式消费等新兴业态");
        suggestion2.put("priority", "中");
        suggestions.add(suggestion2);
        
        return suggestions;
    }
    
    @Override
    public byte[] exportBusinessReport() {
        // 这里应该生成实际的报告文件，暂时返回模拟数据
        Long totalTenants = tenantMapper.selectCount(null);
        String report = "业态分析报告\n\n" +
                       "总租户数: " + (totalTenants != null ? totalTenants : 0) + "\n" +
                       "生成时间: " + LocalDateTime.now();
        
        return report.getBytes();
    }
}