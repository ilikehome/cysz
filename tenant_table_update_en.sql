-- Tenant table structure update script - Based on Vue interface fields
-- Execution time: 2025-01-08

-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop existing tenant table
DROP TABLE IF EXISTS tenant;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create new tenant table - Based on Vue interface fields
CREATE TABLE tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Tenant ID',
    tenant_name VARCHAR(255) NOT NULL COMMENT 'Tenant Name',
    tenant_nature ENUM('individual', 'company', 'government') NOT NULL COMMENT 'Tenant Nature',
    enterprise_nature VARCHAR(100) COMMENT 'Enterprise Nature',
    social_credit_code VARCHAR(50) COMMENT 'Social Credit Code',
    taxpayer_id VARCHAR(50) COMMENT 'Taxpayer ID',
    business_registration_number VARCHAR(50) COMMENT 'Business Registration Number',
    individual_license_number VARCHAR(50) COMMENT 'Individual License Number',
    brand VARCHAR(100) COMMENT 'Brand',
    brand_qualification ENUM('direct', 'franchise', 'joint') COMMENT 'Brand Qualification',
    business_format VARCHAR(50) COMMENT 'Business Format',
    business_scope TEXT COMMENT 'Business Scope',
    legal_person_name VARCHAR(50) COMMENT 'Legal Person Name',
    legal_person_phone VARCHAR(20) COMMENT 'Legal Person Phone',
    legal_person_id_card VARCHAR(50) COMMENT 'Legal Person ID Card',
    finance_contact VARCHAR(50) COMMENT 'Finance Contact',
    finance_phone VARCHAR(20) COMMENT 'Finance Phone',
    payer_name VARCHAR(100) COMMENT 'Payer Name',
    payment_account VARCHAR(100) COMMENT 'Payment Account',
    remark TEXT COMMENT 'Remark',
    status TINYINT DEFAULT 1 COMMENT 'Status: 0-Disabled, 1-Enabled',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
    INDEX idx_tenant_name (tenant_name),
    INDEX idx_social_credit_code (social_credit_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tenant Information Table';

-- Insert test data
INSERT INTO tenant (
    tenant_name, tenant_nature, enterprise_nature, social_credit_code, 
    taxpayer_id, business_registration_number, brand, brand_qualification,
    business_format, business_scope, legal_person_name, legal_person_phone,
    legal_person_id_card, finance_contact, finance_phone, payer_name,
    payment_account, remark, status
) VALUES 
(
    'Starbucks Coffee Co Ltd', 'company', 'Limited Liability Company', '91110000123456789A',
    '91110000123456789A', '110000123456789', 'Starbucks', 'direct',
    'Food Service', 'Coffee, Tea, Light Food Sales', 'Zhang San', '13800138001',
    '110101199001011234', 'Li Si', '13800138002', 'Starbucks Coffee Co Ltd',
    '6225881234567890', 'Famous coffee chain brand', 1
),
(
    'McDonalds Restaurant Co Ltd', 'company', 'Foreign Investment Enterprise', '91110000987654321B',
    '91110000987654321B', '110000987654321', 'McDonalds', 'franchise',
    'Fast Food Service', 'Burger, Fries, Beverage Sales', 'Wang Wu', '13800138003',
    '110101199002021234', 'Zhao Liu', '13800138004', 'McDonalds Restaurant Co Ltd',
    '6225889876543210', 'Global famous fast food chain', 1
),
(
    'Zhang Xiaoming', 'individual', NULL, NULL,
    NULL, NULL, 'Zhang Xiaoming Clothing Store', NULL,
    'Clothing Retail', 'Clothing, Shoes, Hats Sales', 'Zhang Xiaoming', '13800138005',
    '110101199003031234', 'Zhang Xiaoming', '13800138005', 'Zhang Xiaoming',
    '6225881111111111', 'Individual clothing operator', 1
),
(
    'Beijing Technology Development Co Ltd', 'company', 'Limited Liability Company', '91110000555666777C',
    '91110000555666777C', '110000555666777', 'Tech Innovation', 'direct',
    'Technology Service', 'Software Development, Technical Consulting', 'Liu Qi', '13800138006',
    '110101199004041234', 'Chen Ba', '13800138007', 'Beijing Technology Development Co Ltd',
    '6225882222222222', 'High-tech enterprise', 1
);