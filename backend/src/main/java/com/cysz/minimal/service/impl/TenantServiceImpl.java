package com.cysz.minimal.service.impl;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.entity.Tenant;
import com.cysz.minimal.enums.BrandQualification;
import com.cysz.minimal.enums.TenantNature;
import com.cysz.minimal.exception.BusinessException;
import com.cysz.minimal.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 租户服务实现类
 */
@Service
public class TenantServiceImpl implements TenantService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
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
     * 自定义RowMapper，处理数据库到前端的转换
     */
    private static class TenantRowMapper implements RowMapper<Tenant> {
        private final TenantServiceImpl service;
        
        public TenantRowMapper(TenantServiceImpl service) {
            this.service = service;
        }
        
        @Override
        public Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tenant tenant = new Tenant();
            tenant.setId(rs.getLong("id"));
            tenant.setTenantName(rs.getString("tenant_name"));
            tenant.setTenantNature(service.convertTenantNatureFromDb(rs.getString("tenant_nature")));
            tenant.setEnterpriseNature(rs.getString("enterprise_nature"));
            tenant.setSocialCreditCode(rs.getString("social_credit_code"));
            tenant.setTaxpayerId(rs.getString("taxpayer_id"));
            tenant.setBusinessRegistrationNumber(rs.getString("business_registration_number"));
            tenant.setIndividualLicenseNumber(rs.getString("individual_license_number"));
            tenant.setBrand(rs.getString("brand"));
            tenant.setBrandQualification(service.convertBrandQualificationFromDb(rs.getString("brand_qualification")));
            tenant.setBusinessFormat(rs.getString("business_format"));
            tenant.setBusinessScope(rs.getString("business_scope"));
            tenant.setLegalPersonName(rs.getString("legal_person_name"));
            tenant.setLegalPersonPhone(rs.getString("legal_person_phone"));
            tenant.setLegalPersonIdCard(rs.getString("legal_person_id_card"));
            tenant.setFinanceContact(rs.getString("finance_contact"));
            tenant.setFinancePhone(rs.getString("finance_phone"));
            tenant.setPayerName(rs.getString("payer_name"));
            tenant.setPaymentAccount(rs.getString("payment_account"));
            tenant.setRemark(rs.getString("remark"));
            tenant.setStatus(rs.getInt("status"));
            
            // 处理时间字段
            if (rs.getTimestamp("create_time") != null) {
                tenant.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            }
            if (rs.getTimestamp("update_time") != null) {
                tenant.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
            }
            
            return tenant;
        }
    }
    
    @Override
    public PageResult<Tenant> getTenantPage(int current, int size, String keyword, String tenantNature, String businessFormat) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tenant WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        // 添加搜索条件
        if (StringUtils.hasText(keyword)) {
            sql.append(" AND tenant_name LIKE ?");
            params.add("%" + keyword + "%");
        }
        
        if (StringUtils.hasText(tenantNature)) {
            sql.append(" AND tenant_nature = ?");
            params.add(convertTenantNatureToDb(tenantNature)); // 转换为数据库值
        }
        
        if (StringUtils.hasText(businessFormat)) {
            sql.append(" AND business_format = ?");
            params.add(businessFormat);
        }
        
        // 查询总数
        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") t";
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());
        
        // 添加分页
        sql.append(" ORDER BY create_time DESC LIMIT ? OFFSET ?");
        params.add(size);
        params.add((current - 1) * size);
        
        // 查询数据，使用自定义RowMapper
        List<Tenant> records = jdbcTemplate.query(sql.toString(), params.toArray(), new TenantRowMapper(this));
        
        return new PageResult<>(records, total != null ? total : 0, current, size);
    }
    
    @Override
    public Tenant getTenantById(Long id) {
        String sql = "SELECT * FROM tenant WHERE id = ?";
        List<Tenant> tenants = jdbcTemplate.query(sql, new TenantRowMapper(this), id);
        return tenants.isEmpty() ? null : tenants.get(0);
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
        
        String sql = "INSERT INTO tenant (tenant_name, tenant_nature, enterprise_nature, social_credit_code, " +
                    "taxpayer_id, business_registration_number, individual_license_number, brand, " +
                    "brand_qualification, business_format, business_scope, legal_person_name, " +
                    "legal_person_phone, legal_person_id_card, finance_contact, finance_phone, " +
                    "payer_name, payment_account, remark, status, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tenant.getTenantName());
            ps.setString(2, convertTenantNatureToDb(tenant.getTenantNature()));
            ps.setString(3, tenant.getEnterpriseNature());
            ps.setString(4, tenant.getSocialCreditCode());
            ps.setString(5, tenant.getTaxpayerId());
            ps.setString(6, tenant.getBusinessRegistrationNumber());
            ps.setString(7, tenant.getIndividualLicenseNumber());
            ps.setString(8, tenant.getBrand());
            ps.setString(9, convertBrandQualificationToDb(tenant.getBrandQualification()));
            ps.setString(10, tenant.getBusinessFormat());
            ps.setString(11, tenant.getBusinessScope());
            ps.setString(12, tenant.getLegalPersonName());
            ps.setString(13, tenant.getLegalPersonPhone());
            ps.setString(14, tenant.getLegalPersonIdCard());
            ps.setString(15, tenant.getFinanceContact());
            ps.setString(16, tenant.getFinancePhone());
            ps.setString(17, tenant.getPayerName());
            ps.setString(18, tenant.getPaymentAccount());
            ps.setString(19, tenant.getRemark());
            ps.setInt(20, tenant.getStatus() != null ? tenant.getStatus() : 1);
            ps.setObject(21, now);
            ps.setObject(22, now);
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            tenant.setId(key.longValue());
            tenant.setCreateTime(now);
            tenant.setUpdateTime(now);
        }
        
        return tenant;
    }
    
    @Override
    public Tenant updateTenant(Long id, Tenant tenant) {
        // 检查租户是否存在
        Tenant existingTenant = getTenantById(id);
        if (existingTenant == null) {
            throw new BusinessException("租户不存在");
        }
        
        String sql = "UPDATE tenant SET tenant_name=?, tenant_nature=?, enterprise_nature=?, " +
                    "social_credit_code=?, taxpayer_id=?, business_registration_number=?, " +
                    "individual_license_number=?, brand=?, brand_qualification=?, business_format=?, " +
                    "business_scope=?, legal_person_name=?, legal_person_phone=?, legal_person_id_card=?, " +
                    "finance_contact=?, finance_phone=?, payer_name=?, payment_account=?, " +
                    "remark=?, status=?, update_time=? WHERE id=?";
        
        LocalDateTime now = LocalDateTime.now();
        
        jdbcTemplate.update(sql,
                tenant.getTenantName(),
                convertTenantNatureToDb(tenant.getTenantNature()),
                tenant.getEnterpriseNature(),
                tenant.getSocialCreditCode(),
                tenant.getTaxpayerId(),
                tenant.getBusinessRegistrationNumber(),
                tenant.getIndividualLicenseNumber(),
                tenant.getBrand(),
                convertBrandQualificationToDb(tenant.getBrandQualification()),
                tenant.getBusinessFormat(),
                tenant.getBusinessScope(),
                tenant.getLegalPersonName(),
                tenant.getLegalPersonPhone(),
                tenant.getLegalPersonIdCard(),
                tenant.getFinanceContact(),
                tenant.getFinancePhone(),
                tenant.getPayerName(),
                tenant.getPaymentAccount(),
                tenant.getRemark(),
                tenant.getStatus(),
                now,
                id);
        
        tenant.setId(id);
        tenant.setUpdateTime(now);
        return tenant;
    }
    
    @Override
    public void deleteTenant(Long id) {
        // 检查租户是否存在
        Tenant existingTenant = getTenantById(id);
        if (existingTenant == null) {
            throw new BusinessException("租户不存在");
        }
        
        String sql = "DELETE FROM tenant WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    @Override
    public Map<String, Object> getRiskStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总租户数
        Integer totalTenants = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tenant", Integer.class);
        stats.put("totalTenants", totalTenants != null ? totalTenants : 0);
        
        // 高风险租户数（模拟数据）
        stats.put("highRiskTenants", (totalTenants != null ? totalTenants : 0) / 10);
        
        // 中风险租户数（模拟数据）
        stats.put("mediumRiskTenants", (totalTenants != null ? totalTenants : 0) / 5);
        
        // 低风险租户数（模拟数据）
        stats.put("lowRiskTenants", (totalTenants != null ? totalTenants : 0) - 
                                   (Integer) stats.get("highRiskTenants") - 
                                   (Integer) stats.get("mediumRiskTenants"));
        
        return stats;
    }
    
    @Override
    public PageResult<Map<String, Object>> getTenantRiskList(int current, int size) {
        // 模拟风险数据
        String sql = "SELECT id, tenant_name, tenant_nature, business_format, create_time FROM tenant " +
                    "ORDER BY create_time DESC LIMIT ? OFFSET ?";
        
        String countSql = "SELECT COUNT(*) FROM tenant";
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);
        
        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql, size, (current - 1) * size);
        
        // 为每个租户添加模拟的风险评级和转换租户性质
        Random random = new Random();
        String[] riskLevels = {"低风险", "中风险", "高风险"};
        
        for (Map<String, Object> record : records) {
            record.put("riskLevel", riskLevels[random.nextInt(3)]);
            record.put("creditScore", 60 + random.nextInt(40)); // 60-100分
            record.put("lastAssessTime", LocalDateTime.now().minusDays(random.nextInt(30)));
            // 转换租户性质显示
            String tenantNature = (String) record.get("tenant_nature");
            record.put("tenant_nature", convertTenantNatureFromDb(tenantNature));
        }
        
        return new PageResult<>(records, total != null ? total : 0, current, size);
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
        String sql = "SELECT id, tenant_name, tenant_nature, business_format, brand, create_time FROM tenant " +
                    "ORDER BY create_time DESC LIMIT ? OFFSET ?";
        
        String countSql = "SELECT COUNT(*) FROM tenant";
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);
        
        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql, size, (current - 1) * size);
        
        // 为每个租户添加画像标签和转换租户性质
        Random random = new Random();
        String[] tags = {"优质客户", "成长型", "稳定型", "潜力型", "关注型"};
        
        for (Map<String, Object> record : records) {
            List<String> profileTags = new ArrayList<>();
            for (int i = 0; i < 2 + random.nextInt(3); i++) {
                profileTags.add(tags[random.nextInt(tags.length)]);
            }
            record.put("profileTags", profileTags);
            record.put("profileScore", 60 + random.nextInt(40));
            // 转换租户性质显示
            String tenantNature = (String) record.get("tenant_nature");
            record.put("tenant_nature", convertTenantNatureFromDb(tenantNature));
        }
        
        return new PageResult<>(records, total != null ? total : 0, current, size);
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
        String sql = "SELECT business_format, COUNT(*) as count FROM tenant " +
                    "WHERE business_format IS NOT NULL GROUP BY business_format";
        
        List<Map<String, Object>> businessStats = jdbcTemplate.queryForList(sql);
        overview.put("businessStats", businessStats);
        
        // 总体统计
        Integer totalTenants = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tenant", Integer.class);
        overview.put("totalTenants", totalTenants != null ? totalTenants : 0);
        
        return overview;
    }
    
    @Override
    public List<Map<String, Object>> getBusinessDetails(String businessType) {
        String sql;
        Object[] params;
        
        if (StringUtils.hasText(businessType)) {
            sql = "SELECT * FROM tenant WHERE business_format = ? ORDER BY create_time DESC";
            params = new Object[]{businessType};
        } else {
            sql = "SELECT * FROM tenant ORDER BY create_time DESC";
            params = new Object[]{};
        }
        
        return jdbcTemplate.queryForList(sql, params);
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
        String report = "业态分析报告\n\n" +
                       "总租户数: " + jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tenant", Integer.class) + "\n" +
                       "生成时间: " + LocalDateTime.now();
        
        return report.getBytes();
    }
}