-- 创建数据库
CREATE DATABASE IF NOT EXISTS property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE property_management;

-- 项目表
CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    name VARCHAR(100) NOT NULL COMMENT '项目名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '项目编码',
    type VARCHAR(20) NOT NULL COMMENT '项目类型：COMMERCIAL-商业地产，RESIDENTIAL-住宅地产，OFFICE-办公地产，INDUSTRIAL-工业地产，MIXED-综合地产',
    status VARCHAR(20) NOT NULL COMMENT '项目状态：PLANNING-规划中，CONSTRUCTION-建设中，COMPLETED-已竣工，DELIVERED-已交付，OPERATION-运营中，SUSPENDED-暂停',
    address VARCHAR(200) COMMENT '项目地址',
    total_area DECIMAL(15,2) COMMENT '总建筑面积（平方米）',
    rentable_area DECIMAL(15,2) COMMENT '可租赁面积（平方米）',
    building_count INT DEFAULT 0 COMMENT '楼栋数量',
    unit_count INT DEFAULT 0 COMMENT '单元数量',
    description TEXT COMMENT '项目描述',
    start_date DATE COMMENT '开工日期',
    completion_date DATE COMMENT '竣工日期',
    delivery_date DATE COMMENT '交付日期',
    developer VARCHAR(100) COMMENT '开发商',
    property_company VARCHAR(100) COMMENT '物业公司',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记'
) COMMENT '项目表';

-- 楼栋表
CREATE TABLE IF NOT EXISTS building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    number VARCHAR(20) NOT NULL COMMENT '楼栋编号',
    name VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    type VARCHAR(20) NOT NULL COMMENT '楼栋类型：RESIDENTIAL-住宅，COMMERCIAL-商业，OFFICE-办公，PARKING-停车场，OTHER-其他',
    status VARCHAR(20) NOT NULL COMMENT '楼栋状态：CONSTRUCTION-建设中，COMPLETED-已竣工，DELIVERED-已交付，OPERATION-运营中',
    floor_count INT DEFAULT 0 COMMENT '楼层数量',
    unit_count INT DEFAULT 0 COMMENT '单元数量',
    total_area DECIMAL(15,2) COMMENT '总建筑面积（平方米）',
    rentable_area DECIMAL(15,2) COMMENT '可租赁面积（平方米）',
    description TEXT COMMENT '楼栋描述',
    completion_date DATE COMMENT '竣工日期',
    delivery_date DATE COMMENT '交付日期',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_project_id (project_id),
    UNIQUE KEY uk_project_number (project_id, number)
) COMMENT '楼栋表';

-- 楼层表
CREATE TABLE IF NOT EXISTS floor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼层ID',
    building_id BIGINT NOT NULL COMMENT '楼栋ID',
    number VARCHAR(20) NOT NULL COMMENT '楼层编号',
    name VARCHAR(100) NOT NULL COMMENT '楼层名称',
    floor_index INT NOT NULL COMMENT '楼层序号（从1开始，负数表示地下层）',
    type VARCHAR(20) NOT NULL COMMENT '楼层类型：RESIDENTIAL-住宅，COMMERCIAL-商业，OFFICE-办公，PARKING-停车场，EQUIPMENT-设备层，OTHER-其他',
    status VARCHAR(20) NOT NULL COMMENT '楼层状态：CONSTRUCTION-建设中，COMPLETED-已竣工，DELIVERED-已交付，OPERATION-运营中',
    unit_count INT DEFAULT 0 COMMENT '单元数量',
    total_area DECIMAL(15,2) COMMENT '总建筑面积（平方米）',
    rentable_area DECIMAL(15,2) COMMENT '可租赁面积（平方米）',
    floor_height DECIMAL(5,2) COMMENT '层高（米）',
    description TEXT COMMENT '楼层描述',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_building_id (building_id),
    UNIQUE KEY uk_building_number (building_id, number)
) COMMENT '楼层表';

-- 单元表
CREATE TABLE IF NOT EXISTS unit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '单元ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    building_id BIGINT NOT NULL COMMENT '楼栋ID',
    floor_id BIGINT NOT NULL COMMENT '楼层ID',
    number VARCHAR(20) NOT NULL COMMENT '单元编号',
    name VARCHAR(100) NOT NULL COMMENT '单元名称',
    type VARCHAR(20) NOT NULL COMMENT '单元类型：APARTMENT-公寓，SHOP-商铺，OFFICE-办公室，WAREHOUSE-仓库，PARKING-车位，OTHER-其他',
    status VARCHAR(20) NOT NULL COMMENT '单元状态：AVAILABLE-可租，RENTED-已租，MAINTENANCE-维护中，RESERVED-预留',
    building_area DECIMAL(10,2) COMMENT '建筑面积（平方米）',
    rentable_area DECIMAL(10,2) COMMENT '可租赁面积（平方米）',
    rent_price DECIMAL(10,2) COMMENT '租金（元/月）',
    orientation VARCHAR(20) COMMENT '朝向：NORTH-北，SOUTH-南，EAST-东，WEST-西，NORTHEAST-东北，NORTHWEST-西北，SOUTHEAST-东南，SOUTHWEST-西南',
    decoration_status VARCHAR(20) COMMENT '装修状态：ROUGH-毛坯，SIMPLE-简装，FINE-精装，LUXURY-豪装',
    room_count INT COMMENT '房间数',
    bathroom_count INT COMMENT '卫生间数',
    balcony_count INT COMMENT '阳台数',
    description TEXT COMMENT '单元描述',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_project_id (project_id),
    INDEX idx_building_id (building_id),
    INDEX idx_floor_id (floor_id),
    INDEX idx_status (status),
    UNIQUE KEY uk_project_number (project_id, number)
) COMMENT '单元表';

-- 租户表
CREATE TABLE IF NOT EXISTS tenant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '租户ID',
    name VARCHAR(100) NOT NULL COMMENT '租户名称',
    number VARCHAR(50) NOT NULL UNIQUE COMMENT '租户编号',
    nature VARCHAR(20) NOT NULL COMMENT '租户性质：INDIVIDUAL-个人，ENTERPRISE-企业',
    business_type VARCHAR(50) COMMENT '业务形态',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '地址',
    id_card VARCHAR(20) COMMENT '身份证号',
    business_license VARCHAR(50) COMMENT '营业执照号',
    credit_code VARCHAR(50) COMMENT '统一社会信用代码',
    credit_level VARCHAR(20) COMMENT '信用等级：AAA-AAA级，AA-AA级，A-A级，BBB-BBB级，BB-BB级，B-B级，CCC-CCC级，CC-CC级，C-C级，D-D级',
    risk_level VARCHAR(20) COMMENT '风险等级：LOW-低风险，MEDIUM-中风险，HIGH-高风险',
    status VARCHAR(20) NOT NULL COMMENT '状态：ACTIVE-正常，INACTIVE-停用，BLACKLIST-黑名单',
    description TEXT COMMENT '备注',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记'
) COMMENT '租户表';

-- 合同表
CREATE TABLE IF NOT EXISTS contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    number VARCHAR(50) NOT NULL UNIQUE COMMENT '合同编号',
    name VARCHAR(100) NOT NULL COMMENT '合同名称',
    type VARCHAR(20) NOT NULL COMMENT '合同类型：LEASE-租赁合同，SERVICE-服务合同，OTHER-其他',
    status VARCHAR(20) NOT NULL COMMENT '合同状态：DRAFT-草稿，PENDING-待审核，ACTIVE-生效中，EXPIRED-已到期，TERMINATED-已终止，CANCELLED-已取消',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    rent_amount DECIMAL(12,2) NOT NULL COMMENT '租金金额（元/月）',
    deposit_amount DECIMAL(12,2) COMMENT '押金金额（元）',
    payment_cycle VARCHAR(20) NOT NULL COMMENT '付款周期：MONTHLY-月付，QUARTERLY-季付，SEMI_ANNUAL-半年付，ANNUAL-年付',
    payment_method VARCHAR(20) NOT NULL COMMENT '付款方式：CASH-现金，TRANSFER-转账，CHECK-支票，OTHER-其他',
    sign_date DATE COMMENT '签署日期',
    effective_date DATE COMMENT '生效日期',
    termination_date DATE COMMENT '终止日期',
    termination_reason VARCHAR(200) COMMENT '终止原因',
    auto_renewal BOOLEAN DEFAULT FALSE COMMENT '自动续约',
    renewal_period INT COMMENT '续约期限（月）',
    contract_content TEXT COMMENT '合同内容',
    attachments TEXT COMMENT '附件',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_project_id (project_id),
    INDEX idx_status (status),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
) COMMENT '合同表';

-- 合同单元关联表
CREATE TABLE IF NOT EXISTS contract_unit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    contract_id BIGINT NOT NULL COMMENT '合同ID',
    unit_id BIGINT NOT NULL COMMENT '单元ID',
    unit_rent DECIMAL(10,2) COMMENT '单元租金（元/月）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_contract_id (contract_id),
    INDEX idx_unit_id (unit_id),
    UNIQUE KEY uk_contract_unit (contract_id, unit_id)
) COMMENT '合同单元关联表';

-- 应收账款表
CREATE TABLE IF NOT EXISTS receivable (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '应收账款ID',
    bill_number VARCHAR(50) NOT NULL UNIQUE COMMENT '账单编号',
    contract_id BIGINT NOT NULL COMMENT '合同ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    bill_type VARCHAR(20) NOT NULL COMMENT '账单类型：RENT-租金，DEPOSIT-押金，PROPERTY_FEE-物业费，UTILITY_FEE-水电费，PARKING_FEE-停车费，OTHER-其他',
    receivable_amount DECIMAL(12,2) NOT NULL COMMENT '应收金额（元）',
    received_amount DECIMAL(12,2) DEFAULT 0 COMMENT '已收金额（元）',
    outstanding_amount DECIMAL(12,2) NOT NULL COMMENT '未收金额（元）',
    bill_date DATE NOT NULL COMMENT '账单日期',
    due_date DATE NOT NULL COMMENT '到期日期',
    status VARCHAR(20) NOT NULL COMMENT '状态：PENDING-待收款，PARTIAL-部分收款，PAID-已收款，OVERDUE-已逾期',
    overdue_days INT DEFAULT 0 COMMENT '逾期天数',
    late_fee DECIMAL(10,2) DEFAULT 0 COMMENT '滞纳金（元）',
    remark TEXT COMMENT '备注',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_contract_id (contract_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_project_id (project_id),
    INDEX idx_bill_type (bill_type),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date)
) COMMENT '应收账款表';

-- 已收款表
CREATE TABLE IF NOT EXISTS received (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '已收款ID',
    transaction_number VARCHAR(50) NOT NULL UNIQUE COMMENT '银行流水号',
    received_amount DECIMAL(12,2) NOT NULL COMMENT '收款金额（元）',
    payment_method VARCHAR(20) NOT NULL COMMENT '收款方式：CASH-现金，TRANSFER-转账，CHECK-支票，ALIPAY-支付宝，WECHAT-微信，OTHER-其他',
    received_date DATE NOT NULL COMMENT '收款日期',
    payer_name VARCHAR(100) COMMENT '付款人',
    payer_account VARCHAR(100) COMMENT '付款账户',
    receiver_account VARCHAR(100) COMMENT '收款账户',
    bill_type VARCHAR(20) COMMENT '账单类型：RENT-租金，DEPOSIT-押金，PROPERTY_FEE-物业费，UTILITY_FEE-水电费，PARKING_FEE-停车费，OTHER-其他',
    match_status VARCHAR(20) NOT NULL COMMENT '匹配状态：UNMATCHED-未匹配，PARTIAL-部分匹配，MATCHED-已匹配',
    matched_amount DECIMAL(12,2) DEFAULT 0 COMMENT '已匹配金额（元）',
    unmatched_amount DECIMAL(12,2) NOT NULL COMMENT '未匹配金额（元）',
    remark TEXT COMMENT '备注',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_payment_method (payment_method),
    INDEX idx_received_date (received_date),
    INDEX idx_match_status (match_status)
) COMMENT '已收款表';

-- 收付款匹配表
CREATE TABLE IF NOT EXISTS payment_match (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    receivable_id BIGINT NOT NULL COMMENT '应收账款ID',
    received_id BIGINT NOT NULL COMMENT '已收款ID',
    match_amount DECIMAL(12,2) NOT NULL COMMENT '匹配金额（元）',
    match_type VARCHAR(20) NOT NULL COMMENT '匹配类型：AUTO-自动匹配，MANUAL-手动匹配',
    match_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '匹配时间',
    INDEX idx_receivable_id (receivable_id),
    INDEX idx_received_id (received_id)
) COMMENT '收付款匹配表';

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(200) COMMENT '头像',
    department_id BIGINT COMMENT '部门ID',
    position VARCHAR(50) COMMENT '职位',
    status VARCHAR(20) NOT NULL COMMENT '状态：ACTIVE-正常，INACTIVE-停用，LOCKED-锁定',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    login_count INT DEFAULT 0 COMMENT '登录次数',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_department_id (department_id)
) COMMENT '用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    type VARCHAR(20) NOT NULL COMMENT '角色类型：SYSTEM-系统角色，CUSTOM-自定义角色',
    status VARCHAR(20) NOT NULL COMMENT '状态：ACTIVE-启用，INACTIVE-禁用',
    description TEXT COMMENT '描述',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记'
) COMMENT '角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    name VARCHAR(100) NOT NULL COMMENT '权限名称',
    type VARCHAR(20) NOT NULL COMMENT '权限类型：MENU-菜单，BUTTON-按钮，API-接口',
    parent_id BIGINT COMMENT '父权限ID',
    path VARCHAR(200) COMMENT '路径',
    component VARCHAR(200) COMMENT '组件',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status VARCHAR(20) NOT NULL COMMENT '状态：ACTIVE-启用，INACTIVE-禁用',
    description TEXT COMMENT '描述',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_parent_id (parent_id),
    INDEX idx_type (type)
) COMMENT '权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) COMMENT '用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id),
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) COMMENT '角色权限关联表';

-- 部门表
CREATE TABLE IF NOT EXISTS department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '部门编码',
    parent_id BIGINT COMMENT '父部门ID',
    level_path VARCHAR(500) COMMENT '层级路径',
    sort_order INT DEFAULT 0 COMMENT '排序',
    manager_id BIGINT COMMENT '部门经理ID',
    status VARCHAR(20) NOT NULL COMMENT '状态：ACTIVE-正常，INACTIVE-停用',
    description TEXT COMMENT '描述',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '删除标记',
    INDEX idx_parent_id (parent_id),
    INDEX idx_manager_id (manager_id)
) COMMENT '部门表';

-- 插入初始数据
-- 插入示例项目
INSERT INTO project (name, code, type, status, address, total_area, rentable_area, building_count, unit_count, description, developer, property_company, contact_person, contact_phone) VALUES
('万达广场', 'WD001', 'COMMERCIAL', 'OPERATION', '北京市朝阳区建国路88号', 50000.00, 45000.00, 3, 150, '大型商业综合体', '万达集团', '万达物业', '张经理', '13800138001'),
('阳光花园', 'YG001', 'RESIDENTIAL', 'OPERATION', '上海市浦东新区世纪大道123号', 80000.00, 72000.00, 5, 200, '高端住宅小区', '绿地集团', '绿地物业', '李经理', '13800138002'),
('科技大厦', 'KJ001', 'OFFICE', 'OPERATION', '深圳市南山区科技园南路456号', 30000.00, 28000.00, 1, 80, '甲级写字楼', '华润置地', '华润物业', '王经理', '13800138003');

-- 插入示例楼栋
INSERT INTO building (project_id, number, name, type, status, floor_count, unit_count, total_area, rentable_area, description) VALUES
(1, 'A', 'A栋', 'COMMERCIAL', 'OPERATION', 5, 50, 16000.00, 14400.00, '主要商业区域'),
(1, 'B', 'B栋', 'COMMERCIAL', 'OPERATION', 4, 40, 12000.00, 10800.00, '餐饮娱乐区域'),
(2, '1', '1号楼', 'RESIDENTIAL', 'OPERATION', 20, 80, 32000.00, 28800.00, '高层住宅'),
(2, '2', '2号楼', 'RESIDENTIAL', 'OPERATION', 18, 72, 28800.00, 25920.00, '高层住宅'),
(3, 'T1', '主楼', 'OFFICE', 'OPERATION', 25, 80, 30000.00, 28000.00, '甲级写字楼主楼');

-- 插入示例租户
INSERT INTO tenant (name, number, nature, business_type, contact_person, contact_phone, email, status, description) VALUES
('星巴克咖啡', 'T001', 'ENTERPRISE', '餐饮服务', '陈店长', '13900139001', 'starbucks@example.com', 'ACTIVE', '知名咖啡连锁品牌'),
('张三', 'T002', 'INDIVIDUAL', '个人租户', '张三', '13900139002', 'zhangsan@example.com', 'ACTIVE', '个人住宅租户'),
('科技有限公司', 'T003', 'ENTERPRISE', '软件开发', '李总', '13900139003', 'tech@example.com', 'ACTIVE', '软件开发公司');

-- 插入管理员用户
INSERT INTO user (username, password, real_name, email, phone, status, create_by) VALUES
('admin', '$2a$10$7JB720yubVSOfvVWdBYoOe.PuiKloYAjFYpTt6eLtLAHQGJuyU7Mm', '系统管理员', 'admin@example.com', '13800000000', 'ACTIVE', 'system');

-- 插入系统角色
INSERT INTO role (code, name, type, status, description, create_by) VALUES
('ADMIN', '系统管理员', 'SYSTEM', 'ACTIVE', '系统管理员角色，拥有所有权限', 'system'),
('MANAGER', '项目经理', 'SYSTEM', 'ACTIVE', '项目经理角色，负责项目管理', 'system'),
('OPERATOR', '操作员', 'SYSTEM', 'ACTIVE', '操作员角色，负责日常操作', 'system');

-- 插入用户角色关联
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);

-- 插入部门
INSERT INTO department (name, code, status, description, create_by) VALUES
('总经理办公室', 'GM', 'ACTIVE', '总经理办公室', 'system'),
('项目管理部', 'PM', 'ACTIVE', '项目管理部门', 'system'),
('财务部', 'FIN', 'ACTIVE', '财务管理部门', 'system'),
('运营部', 'OPS', 'ACTIVE', '运营管理部门', 'system');

COMMIT;