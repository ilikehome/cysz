-- 云联智管地产资管系统数据库表结构
-- 创建时间：2025-01-08

-- 组织表
CREATE TABLE IF NOT EXISTS organizations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    org_code VARCHAR(50) NOT NULL UNIQUE COMMENT '组织代码',
    org_name VARCHAR(100) NOT NULL COMMENT '组织名称',
    org_type VARCHAR(20) DEFAULT 'company' COMMENT '组织类型：company-公司，government-政府机构，institution-事业单位',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    address VARCHAR(200) COMMENT '地址',
    description TEXT COMMENT '组织描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    real_name VARCHAR(50),
    org_id BIGINT COMMENT '所属组织ID',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (org_id) REFERENCES organizations(id),
    UNIQUE KEY unique_username_org (username, org_id)
);

-- 1. 项目管理表
CREATE TABLE IF NOT EXISTS project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '项目ID',
    name VARCHAR(255) NOT NULL COMMENT '项目名称',
    description TEXT COMMENT '项目描述',
    location VARCHAR(500) COMMENT '项目位置',
    total_area DECIMAL(15,2) COMMENT '总面积(平方米)',
    total_units INT DEFAULT 0 COMMENT '总单元数',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃，DELETED-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目管理表';

-- 2. 楼宇管理表
CREATE TABLE IF NOT EXISTS building (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '楼宇ID',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    name VARCHAR(255) NOT NULL COMMENT '楼宇名称',
    building_type VARCHAR(50) COMMENT '楼宇类型：RESIDENTIAL-住宅，COMMERCIAL-商业，OFFICE-办公',
    floors INT COMMENT '楼层数',
    total_area DECIMAL(15,2) COMMENT '总面积(平方米)',
    address VARCHAR(500) COMMENT '详细地址',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃，DELETED-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    INDEX idx_project_id (project_id),
    INDEX idx_name (name),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼宇管理表';

-- 3. 单元管理表
CREATE TABLE IF NOT EXISTS unit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '单元ID',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    building_id BIGINT COMMENT '所属楼宇ID',
    unit_number VARCHAR(100) NOT NULL COMMENT '单元编号',
    floor_number INT COMMENT '楼层号',
    unit_type VARCHAR(50) COMMENT '单元类型：APARTMENT-公寓，SHOP-商铺，OFFICE-办公室',
    area DECIMAL(10,2) COMMENT '面积(平方米)',
    rent_price DECIMAL(10,2) COMMENT '租金价格',
    sale_price DECIMAL(12,2) COMMENT '销售价格',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态：AVAILABLE-可用，RENTED-已租，SOLD-已售，MAINTENANCE-维护中，DELETED-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (building_id) REFERENCES building(id) ON DELETE SET NULL,
    INDEX idx_project_id (project_id),
    INDEX idx_building_id (building_id),
    INDEX idx_unit_number (unit_number),
    INDEX idx_status (status),
    UNIQUE KEY uk_project_unit (project_id, unit_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单元管理表';

-- 4. 租户管理表
CREATE TABLE IF NOT EXISTS tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
    name VARCHAR(255) NOT NULL COMMENT '租户姓名/公司名称',
    contact_person VARCHAR(100) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    id_card VARCHAR(50) COMMENT '身份证号/营业执照号',
    address VARCHAR(500) COMMENT '联系地址',
    tenant_type VARCHAR(20) DEFAULT 'INDIVIDUAL' COMMENT '租户类型：INDIVIDUAL-个人，COMPANY-企业',
    credit_rating VARCHAR(10) DEFAULT 'A' COMMENT '信用等级：A-优秀，B-良好，C-一般，D-较差',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃，DELETED-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户管理表';

-- 5. 合同管理表
CREATE TABLE IF NOT EXISTS contract (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '合同ID',
    contract_number VARCHAR(100) NOT NULL UNIQUE COMMENT '合同编号',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    unit_id BIGINT NOT NULL COMMENT '关联单元ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    contract_type VARCHAR(20) DEFAULT 'RENT' COMMENT '合同类型：RENT-租赁，SALE-销售',
    start_date DATE NOT NULL COMMENT '合同开始日期',
    end_date DATE NOT NULL COMMENT '合同结束日期',
    monthly_rent DECIMAL(10,2) COMMENT '月租金',
    deposit DECIMAL(10,2) COMMENT '押金',
    total_amount DECIMAL(12,2) COMMENT '合同总金额',
    payment_method VARCHAR(50) COMMENT '付款方式',
    payment_cycle VARCHAR(20) DEFAULT 'MONTHLY' COMMENT '付款周期：MONTHLY-月付，QUARTERLY-季付，YEARLY-年付',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-生效，EXPIRED-过期，TERMINATED-终止，DELETED-已删除',
    notes TEXT COMMENT '备注信息',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (unit_id) REFERENCES unit(id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenant(id) ON DELETE CASCADE,
    INDEX idx_contract_number (contract_number),
    INDEX idx_project_id (project_id),
    INDEX idx_unit_id (unit_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_status (status),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同管理表';

-- 6. 应收账款表
CREATE TABLE IF NOT EXISTS receivable_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '应收账款ID',
    contract_id BIGINT NOT NULL COMMENT '关联合同ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    bill_period VARCHAR(20) NOT NULL COMMENT '账单周期(如：2025-01)',
    bill_type VARCHAR(20) DEFAULT 'RENT' COMMENT '账单类型：RENT-租金，DEPOSIT-押金，UTILITY-水电费，MANAGEMENT-管理费',
    amount DECIMAL(10,2) NOT NULL COMMENT '应收金额',
    paid_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '已付金额',
    outstanding_amount DECIMAL(10,2) NOT NULL COMMENT '未付金额',
    due_date DATE NOT NULL COMMENT '到期日期',
    payment_date DATE COMMENT '实际付款日期',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待付，PAID-已付，OVERDUE-逾期，PARTIAL-部分付款，DELETED-已删除',
    late_fee DECIMAL(8,2) DEFAULT 0.00 COMMENT '滞纳金',
    notes TEXT COMMENT '备注信息',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (contract_id) REFERENCES contract(id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenant(id) ON DELETE CASCADE,
    INDEX idx_contract_id (contract_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_bill_period (bill_period),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date),
    UNIQUE KEY uk_contract_period_type (contract_id, bill_period, bill_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应收账款表';