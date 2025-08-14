-- 根据Vue界面字段重新设计数据库表结构
-- 执行时间：2025-01-08

-- 删除现有表（如果存在）
DROP TABLE IF EXISTS unit;
DROP TABLE IF EXISTS floor;
DROP TABLE IF EXISTS building;
DROP TABLE IF EXISTS project;

-- 1. 项目表 - 根据Vue界面字段设计
CREATE TABLE project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '项目ID',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    project_type ENUM('COMPLEX', 'COMMERCIAL_DISTRICT', 'HOTEL', 'APARTMENT', 'OFFICE') NOT NULL COMMENT '项目类型',
    management_org VARCHAR(100) NOT NULL COMMENT '管理组织',
    rent_bill_company VARCHAR(100) NOT NULL COMMENT '租金账单公司',
    rent_bill_bank_account VARCHAR(100) COMMENT '租金账单银行账号',
    city VARCHAR(50) NOT NULL COMMENT '城市',
    address VARCHAR(200) NOT NULL COMMENT '地址',
    project_manager VARCHAR(50) COMMENT '项目经理',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_project_name (project_name),
    INDEX idx_project_type (project_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目管理表';

-- 2. 楼栋表 - 根据Vue界面字段设计
CREATE TABLE building (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '楼栋ID',
    building_name VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    building_code VARCHAR(50) NOT NULL COMMENT '楼栋编码',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    building_area DECIMAL(12,2) COMMENT '建筑面积',
    rent_area DECIMAL(12,2) COMMENT '计租面积',
    property_area DECIMAL(12,2) COMMENT '产权面积',
    usable_area DECIMAL(12,2) COMMENT '实用面积',
    remark TEXT COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    INDEX idx_project_id (project_id),
    INDEX idx_building_name (building_name),
    INDEX idx_building_code (building_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼栋管理表';

-- 3. 楼层表 - 根据Vue界面字段设计
CREATE TABLE floor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '楼层ID',
    floor_name VARCHAR(100) NOT NULL COMMENT '楼层名称',
    floor_code VARCHAR(50) NOT NULL COMMENT '楼层编码',
    building_id BIGINT NOT NULL COMMENT '所属楼栋ID',
    remark TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (building_id) REFERENCES building(id) ON DELETE CASCADE,
    INDEX idx_building_id (building_id),
    INDEX idx_floor_name (floor_name),
    INDEX idx_floor_code (floor_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼层管理表';

-- 4. 单元表 - 根据Vue界面字段设计
CREATE TABLE unit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '单元ID',
    unit_name VARCHAR(100) NOT NULL COMMENT '单元名称',
    unit_code VARCHAR(50) NOT NULL COMMENT '单元编码',
    floor_id BIGINT NOT NULL COMMENT '所属楼层ID',
    unit_status ENUM('RENTABLE', 'SELF_USE', 'PUBLIC_USE', 'LEASE_BACK', 'DISABLED', 'SELF_RENTAL') DEFAULT 'RENTABLE' COMMENT '单元状态',
    unit_purpose VARCHAR(100) COMMENT '单元用途：OFFICE-办公，RETAIL-零售，WAREHOUSE-仓储，RESTAURANT-餐饮',
    building_area DECIMAL(10,2) COMMENT '建筑面积',
    rent_area DECIMAL(10,2) COMMENT '计租面积',
    is_multi_tenant BOOLEAN DEFAULT FALSE COMMENT '是否一位多租',
    remark TEXT COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (floor_id) REFERENCES floor(id) ON DELETE CASCADE,
    INDEX idx_floor_id (floor_id),
    INDEX idx_unit_name (unit_name),
    INDEX idx_unit_code (unit_code),
    INDEX idx_unit_status (unit_status),
    INDEX idx_status (status),
    UNIQUE KEY uk_unit_code (unit_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单元管理表';

-- 5. 租户表 - 保持原有结构
CREATE TABLE tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
    tenant_name VARCHAR(255) NOT NULL COMMENT '租户姓名/公司名称',
    contact_person VARCHAR(100) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    id_card VARCHAR(50) COMMENT '身份证号/营业执照号',
    address VARCHAR(500) COMMENT '联系地址',
    tenant_type VARCHAR(20) DEFAULT 'INDIVIDUAL' COMMENT '租户类型：INDIVIDUAL-个人，COMPANY-企业',
    credit_rating VARCHAR(10) DEFAULT 'A' COMMENT '信用等级：A-优秀，B-良好，C-一般，D-较差',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃，DELETED-已删除',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tenant_name (tenant_name),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户管理表';

-- 6. 合同表 - 保持原有结构
CREATE TABLE contract (
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
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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

-- 7. 应收账款表 - 保持原有结构
CREATE TABLE receivable_account (
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
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (contract_id) REFERENCES contract(id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenant(id) ON DELETE CASCADE,
    INDEX idx_contract_id (contract_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_bill_period (bill_period),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date),
    UNIQUE KEY uk_contract_period_type (contract_id, bill_period, bill_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应收账款表';

-- 插入测试数据
-- 插入项目数据
INSERT INTO project (project_name, project_type, management_org, rent_bill_company, rent_bill_bank_account, city, address, project_manager, contact_phone) VALUES
('云联智管大厦', 'OFFICE', '云联智管物业', '云联智管租金公司', '6228480402564890018', '深圳', '深圳市南山区科技园南区', '张经理', '13800138001'),
('智慧商业广场', 'COMMERCIAL_DISTRICT', '智慧物业管理', '智慧租金管理公司', '6228480402564890019', '广州', '广州市天河区珠江新城', '李经理', '13800138002'),
('科技创新园', 'COMPLEX', '创新园物业', '创新园租金公司', '6228480402564890020', '北京', '北京市海淀区中关村', '王经理', '13800138003');

-- 插入楼栋数据
INSERT INTO building (building_name, building_code, project_id, building_area, rent_area, property_area, usable_area, remark) VALUES
('A栋', 'A001', 1, 15000.00, 12000.00, 14000.00, 11000.00, '主楼栋'),
('B栋', 'B001', 1, 12000.00, 10000.00, 11000.00, 9000.00, '副楼栋'),
('C栋', 'C001', 2, 20000.00, 18000.00, 19000.00, 17000.00, '商业楼栋'),
('D栋', 'D001', 3, 25000.00, 22000.00, 24000.00, 21000.00, '办公楼栋');

-- 插入楼层数据
INSERT INTO floor (floor_name, floor_code, building_id, remark) VALUES
('1层', 'F001', 1, '一层大厅'),
('2层', 'F002', 1, '二层办公区'),
('3层', 'F003', 1, '三层办公区'),
('1层', 'F001', 2, 'B栋一层'),
('2层', 'F002', 2, 'B栋二层'),
('1层', 'F001', 3, 'C栋商业层'),
('2层', 'F002', 3, 'C栋办公层'),
('1层', 'F001', 4, 'D栋大厅'),
('2层', 'F002', 4, 'D栋办公区'),
('3层', 'F003', 4, 'D栋会议区');

-- 插入单元数据
INSERT INTO unit (unit_name, unit_code, floor_id, unit_status, unit_purpose, building_area, rent_area, is_multi_tenant, remark) VALUES
('A101', 'A101', 1, 'RENTABLE', 'OFFICE', 120.50, 100.00, FALSE, 'A栋1层01单元'),
('A102', 'A102', 1, 'RENTABLE', 'OFFICE', 150.00, 130.00, FALSE, 'A栋1层02单元'),
('A201', 'A201', 2, 'RENTABLE', 'OFFICE', 200.00, 180.00, TRUE, 'A栋2层01单元'),
('A202', 'A202', 2, 'SELF_USE', 'OFFICE', 180.00, 160.00, FALSE, 'A栋2层02单元'),
('A301', 'A301', 3, 'RENTABLE', 'OFFICE', 220.00, 200.00, FALSE, 'A栋3层01单元'),
('B101', 'B101', 4, 'RENTABLE', 'OFFICE', 100.00, 85.00, FALSE, 'B栋1层01单元'),
('B201', 'B201', 5, 'RENTABLE', 'OFFICE', 150.00, 130.00, FALSE, 'B栋2层01单元'),
('C101', 'C101', 6, 'RENTABLE', 'RETAIL', 300.00, 280.00, FALSE, 'C栋商业单元'),
('C201', 'C201', 7, 'RENTABLE', 'OFFICE', 250.00, 220.00, FALSE, 'C栋办公单元'),
('D101', 'D101', 8, 'PUBLIC_USE', 'OFFICE', 500.00, 450.00, FALSE, 'D栋大厅');

-- 插入租户数据
INSERT INTO tenant (tenant_name, contact_person, phone, email, id_card, address, tenant_type, credit_rating) VALUES
('深圳科技有限公司', '张总', '13800138001', 'zhang@tech.com', '91440300123456789X', '深圳市南山区', 'COMPANY', 'A'),
('广州贸易公司', '李总', '13800138002', 'li@trade.com', '91440100987654321Y', '广州市天河区', 'COMPANY', 'B'),
('北京创新企业', '王总', '13800138003', 'wang@innovation.com', '91110100456789123Z', '北京市海淀区', 'COMPANY', 'A'),
('个人租户张三', '张三', '13800138004', 'zhangsan@email.com', '440300199001011234', '深圳市福田区', 'INDIVIDUAL', 'A');

-- 插入合同数据
INSERT INTO contract (contract_number, project_id, unit_id, tenant_id, contract_type, start_date, end_date, monthly_rent, deposit, total_amount, payment_method, payment_cycle, status) VALUES
('HT2025001', 1, 1, 1, 'RENT', '2025-01-01', '2025-12-31', 8000.00, 16000.00, 96000.00, '银行转账', 'MONTHLY', 'ACTIVE'),
('HT2025002', 1, 3, 2, 'RENT', '2025-01-01', '2026-12-31', 15000.00, 30000.00, 360000.00, '银行转账', 'QUARTERLY', 'ACTIVE'),
('HT2025003', 2, 8, 3, 'RENT', '2025-02-01', '2027-01-31', 25000.00, 50000.00, 600000.00, '银行转账', 'MONTHLY', 'ACTIVE'),
('HT2025004', 1, 6, 4, 'RENT', '2025-01-15', '2025-07-14', 6000.00, 12000.00, 36000.00, '现金', 'MONTHLY', 'ACTIVE');

-- 插入应收账款数据
INSERT INTO receivable_account (contract_id, tenant_id, bill_period, bill_type, amount, paid_amount, outstanding_amount, due_date, status) VALUES
(1, 1, '2025-01', 'RENT', 8000.00, 8000.00, 0.00, '2025-01-31', 'PAID'),
(1, 1, '2025-02', 'RENT', 8000.00, 0.00, 8000.00, '2025-02-28', 'PENDING'),
(2, 2, '2025-Q1', 'RENT', 45000.00, 45000.00, 0.00, '2025-03-31', 'PAID'),
(2, 2, '2025-Q2', 'RENT', 45000.00, 0.00, 45000.00, '2025-06-30', 'PENDING'),
(3, 3, '2025-02', 'RENT', 25000.00, 0.00, 25000.00, '2025-02-28', 'PENDING'),
(4, 4, '2025-01', 'RENT', 6000.00, 6000.00, 0.00, '2025-01-31', 'PAID');