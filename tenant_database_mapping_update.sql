-- 租户表数据映射更新脚本
-- 将数据库中的英文枚举值与前端中文显示进行映射
-- 执行时间：2025-01-08

USE cysz;

-- 更新现有数据中的租户性质映射
-- individual -> 个人, company -> 公司, government -> 政府机构
UPDATE tenant SET tenant_nature = 
    CASE tenant_nature
        WHEN 'individual' THEN '个人'
        WHEN 'company' THEN '公司'  
        WHEN 'government' THEN '政府机构'
        ELSE tenant_nature
    END
WHERE tenant_nature IN ('individual', 'company', 'government');

-- 更新现有数据中的品牌资质映射
-- direct -> 直营, franchise -> 加盟, joint -> 联营
UPDATE tenant SET brand_qualification = 
    CASE brand_qualification
        WHEN 'direct' THEN '直营'
        WHEN 'franchise' THEN '加盟'
        WHEN 'joint' THEN '联营'
        ELSE brand_qualification
    END
WHERE brand_qualification IN ('direct', 'franchise', 'joint');

-- 插入一些测试数据（如果表为空）
INSERT IGNORE INTO tenant (
    tenant_name, tenant_nature, enterprise_nature, social_credit_code, 
    taxpayer_id, business_registration_number, brand, brand_qualification,
    business_format, business_scope, legal_person_name, legal_person_phone,
    legal_person_id_card, finance_contact, finance_phone, payer_name,
    payment_account, remark, status
) VALUES 
(
    '星巴克咖啡有限公司', '公司', '有限责任公司（外商投资企业法人独资）', '91110000123456789A',
    '91110000123456789A', '110000123456789', '星巴克', '直营',
    '咖啡厅', '咖啡、茶饮、轻食销售服务', '张三', '13800138001',
    '110101199001011234', '李四', '13800138002', '星巴克咖啡有限公司',
    '6225881234567890', '知名国际咖啡连锁品牌', 1
),
(
    '麦当劳餐厅有限公司', '公司', '有限责任公司（外商投资企业法人独资）', '91110000987654321B',
    '91110000987654321B', '110000987654321', '麦当劳', '加盟',
    '快餐', '汉堡、薯条、饮料等快餐食品销售', '王五', '13800138003',
    '110101199002021234', '赵六', '13800138004', '麦当劳餐厅有限公司',
    '6225889876543210', '全球知名快餐连锁品牌', 1
),
(
    '张小明服装店', '个人', '个体经营户', NULL,
    NULL, NULL, '张小明服装店', NULL,
    '专卖店', '服装、鞋帽、箱包零售', '张小明', '13800138005',
    '110101199003031234', '张小明', '13800138005', '张小明',
    '6225881111111111', '个体服装经营者', 1
),
(
    '北京科技发展有限公司', '公司', '有限责任公司（自然人投资或控股）', '91110000555666777C',
    '91110000555666777C', '110000555666777', '科技创新', '直营',
    '教育培训', '软件开发、技术咨询、教育培训服务', '刘七', '13800138006',
    '110101199004041234', '陈八', '13800138007', '北京科技发展有限公司',
    '6225882222222222', '高新技术企业，专注教育科技', 1
),
(
    '华润万家超市', '公司', '有限责任公司（非自然人投资或控股的法）', '91110000444555666D',
    '91110000444555666D', '110000444555666', '华润万家', '联营',
    '超市', '食品、日用品、生鲜商品零售', '李九', '13800138008',
    '110101199005051234', '王十', '13800138009', '华润万家超市有限公司',
    '6225883333333333', '大型连锁超市品牌', 1
);

-- 查看更新后的数据
SELECT id, tenant_name, tenant_nature, brand_qualification, business_format, status 
FROM tenant 
ORDER BY create_time DESC 
LIMIT 10;