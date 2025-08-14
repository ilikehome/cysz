-- 租户表结构更新脚本 - 根据Vue界面字段设计
-- 执行时间：2025-01-08

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 删除现有租户表
DROP TABLE IF EXISTS tenant;

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 创建新的租户表 - 根据Vue界面字段设计
CREATE TABLE tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
    tenant_name VARCHAR(255) NOT NULL COMMENT '租户名称',
    tenant_nature ENUM('个人', '公司', '政府机构') NOT NULL COMMENT '租户性质',
    enterprise_nature VARCHAR(100) COMMENT '企业性质',
    social_credit_code VARCHAR(50) COMMENT '社会信用代码',
    taxpayer_id VARCHAR(50) COMMENT '纳税人识别号',
    business_registration_number VARCHAR(50) COMMENT '工商注册号',
    individual_license_number VARCHAR(50) COMMENT '个体户证件号',
    brand VARCHAR(100) COMMENT '品牌',
    brand_qualification ENUM('直营', '加盟', '联营') COMMENT '品牌资质',
    business_format VARCHAR(50) COMMENT '业态',
    business_scope TEXT COMMENT '经营范围',
    legal_person_name VARCHAR(50) COMMENT '法人姓名',
    legal_person_phone VARCHAR(20) COMMENT '法人手机号',
    legal_person_id_card VARCHAR(50) COMMENT '法人身份证',
    finance_contact VARCHAR(50) COMMENT '财务联系人',
    finance_phone VARCHAR(20) COMMENT '财务电话',
    payer_name VARCHAR(100) COMMENT '付款人名称',
    payment_account VARCHAR(100) COMMENT '付款账号',
    remark TEXT COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tenant_name (tenant_name),
    INDEX idx_social_credit_code (social_credit_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户信息表';

-- 插入测试数据
INSERT INTO tenant (
    tenant_name, tenant_nature, enterprise_nature, social_credit_code, 
    taxpayer_id, business_registration_number, brand, brand_qualification,
    business_format, business_scope, legal_person_name, legal_person_phone,
    legal_person_id_card, finance_contact, finance_phone, payer_name,
    payment_account, remark, status
) VALUES 
(
    '星巴克咖啡有限公司', '公司', '有限责任公司', '91110000123456789A',
    '91110000123456789A', '110000123456789', '星巴克', '直营',
    '餐饮服务', '咖啡、茶饮、轻食销售', '张三', '13800138001',
    '110101199001011234', '李四', '13800138002', '星巴克咖啡有限公司',
    '6225881234567890', '知名咖啡连锁品牌', 1
),
(
    '麦当劳餐厅有限公司', '公司', '外商投资企业', '91110000987654321B',
    '91110000987654321B', '110000987654321', '麦当劳', '加盟',
    '快餐服务', '汉堡、薯条、饮料销售', '王五', '13800138003',
    '110101199002021234', '赵六', '13800138004', '麦当劳餐厅有限公司',
    '6225889876543210', '全球知名快餐连锁', 1
),
(
    '张小明', '个人', NULL, NULL,
    NULL, NULL, '张小明服装店', NULL,
    '服装零售', '服装、鞋帽销售', '张小明', '13800138005',
    '110101199003031234', '张小明', '13800138005', '张小明',
    '6225881111111111', '个体服装经营者', 1
),
(
    '北京科技发展有限公司', '公司', '有限责任公司', '91110000555666777C',
    '91110000555666777C', '110000555666777', '科技创新', '直营',
    '科技服务', '软件开发、技术咨询', '刘七', '13800138006',
    '110101199004041234', '陈八', '13800138007', '北京科技发展有限公司',
    '6225882222222222', '高新技术企业', 1
);