-- 云联智管地产资管系统数据库设计
-- 数据库: cysz
-- 字符集: utf8mb4

CREATE DATABASE IF NOT EXISTS cysz CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cysz;

-- 用户管理表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人'
) COMMENT '用户表';

-- 角色表
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '角色表';

-- 权限表
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    type TINYINT DEFAULT 1 COMMENT '类型(1:菜单,2:按钮)',
    path VARCHAR(200) COMMENT '路由路径',
    component VARCHAR(200) COMMENT '组件路径',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '权限表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id)
) COMMENT '用户角色关联表';

-- 角色权限关联表
CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) COMMENT '角色权限关联表';

-- 项目表
CREATE TABLE asset_project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    project_type ENUM('COMPLEX', 'APARTMENT', 'OFFICE', 'COMMERCIAL') NOT NULL COMMENT '项目类型(综合体,公寓,写字楼,商业)',
    company_name VARCHAR(100) COMMENT '所属公司',
    rent_bill_company VARCHAR(100) COMMENT '租金账单公司',
    property_bill_company VARCHAR(100) COMMENT '物业账单公司',
    property_right_company VARCHAR(100) COMMENT '产权公司',
    building_area DECIMAL(12,2) COMMENT '建筑面积',
    rent_area DECIMAL(12,2) COMMENT '计租面积',
    property_area DECIMAL(12,2) COMMENT '产权面积',
    city VARCHAR(50) COMMENT '城市',
    address VARCHAR(200) COMMENT '地址',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人'
) COMMENT '项目表';

-- 楼栋表
CREATE TABLE asset_building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    building_name VARCHAR(100) NOT NULL COMMENT '楼栋/楼层名',
    building_code VARCHAR(50) NOT NULL COMMENT '楼栋/楼层编号',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    building_area DECIMAL(12,2) COMMENT '建筑面积',
    rent_area DECIMAL(12,2) COMMENT '计租面积',
    property_area DECIMAL(12,2) COMMENT '产权面积',
    usable_area DECIMAL(12,2) COMMENT '实用面积',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (project_id) REFERENCES asset_project(id)
) COMMENT '楼栋表';

-- 单元表
CREATE TABLE asset_unit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '单元ID',
    unit_code VARCHAR(50) NOT NULL UNIQUE COMMENT '单元编码',
    unit_description VARCHAR(200) COMMENT '单元说明',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    building_id BIGINT NOT NULL COMMENT '所属楼栋ID',
    unit_status ENUM('VACANT', 'OCCUPIED', 'MAINTENANCE', 'RESERVED') DEFAULT 'VACANT' COMMENT '单元状态(空置,已租,维修,预留)',
    unit_purpose VARCHAR(100) COMMENT '单元用途',
    building_area DECIMAL(12,2) COMMENT '建筑面积',
    rent_area DECIMAL(12,2) COMMENT '计租面积',
    property_area DECIMAL(12,2) COMMENT '产权面积',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (project_id) REFERENCES asset_project(id),
    FOREIGN KEY (building_id) REFERENCES asset_building(id)
) COMMENT '单元表';

-- 单元合并拆分记录表
CREATE TABLE asset_unit_merge_split_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    operation_type ENUM('MERGE', 'SPLIT') NOT NULL COMMENT '操作类型(合并,拆分)',
    source_unit_ids TEXT COMMENT '源单元ID列表(JSON格式)',
    target_unit_ids TEXT COMMENT '目标单元ID列表(JSON格式)',
    operation_reason VARCHAR(500) COMMENT '操作原因',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    operator VARCHAR(50) COMMENT '操作人'
) COMMENT '单元合并拆分记录表';

-- 租户表
CREATE TABLE tenant_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '租户ID',
    tenant_code VARCHAR(50) NOT NULL UNIQUE COMMENT '租户编码',
    tenant_name VARCHAR(100) NOT NULL COMMENT '租户名称',
    tenant_category VARCHAR(50) COMMENT '租户类别',
    project_id BIGINT COMMENT '所属项目ID',
    social_credit_code VARCHAR(50) COMMENT '社会信用代码',
    certificate_type VARCHAR(50) COMMENT '证件类型',
    taxpayer_id VARCHAR(50) COMMENT '纳税人识别号',
    business_license VARCHAR(50) COMMENT '工商注册号',
    legal_person VARCHAR(50) COMMENT '法人姓名',
    registered_address VARCHAR(200) COMMENT '公司注册地',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (project_id) REFERENCES asset_project(id)
) COMMENT '租户表';

-- 合同表
CREATE TABLE contract_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    contract_no VARCHAR(50) NOT NULL UNIQUE COMMENT '合同编号',
    contract_name VARCHAR(100) NOT NULL COMMENT '合同名称',
    project_id BIGINT NOT NULL COMMENT '签约项目ID',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    signatory VARCHAR(50) COMMENT '签订人',
    contract_type VARCHAR(50) COMMENT '合同类型',
    contract_status ENUM('DRAFT', 'ACTIVE', 'EXPIRED', 'TERMINATED') DEFAULT 'DRAFT' COMMENT '合同状态(草稿,生效,过期,终止)',
    rent_bill_company VARCHAR(100) COMMENT '租金账单公司',
    property_bill_company VARCHAR(100) COMMENT '物业账单公司',
    lease_no VARCHAR(50) COMMENT '租赁号',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    tenant_name VARCHAR(100) COMMENT '租户名称',
    unit_id BIGINT NOT NULL COMMENT '单元ID',
    unit_description VARCHAR(200) COMMENT '单元说明',
    rent_mode VARCHAR(50) COMMENT '租金模式',
    monthly_rent DECIMAL(12,2) COMMENT '月租金',
    deposit DECIMAL(12,2) COMMENT '押金',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (project_id) REFERENCES asset_project(id),
    FOREIGN KEY (tenant_id) REFERENCES tenant_info(id),
    FOREIGN KEY (unit_id) REFERENCES asset_unit(id)
) COMMENT '合同表';

-- 应收账款表
CREATE TABLE receivable_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '应收账款ID',
    batch_no VARCHAR(50) NOT NULL COMMENT '批次号',
    line_no VARCHAR(50) NOT NULL COMMENT '行号',
    process_status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'REJECTED') DEFAULT 'PENDING' COMMENT '处理状态(待处理,处理中,已完成,已拒绝)',
    company VARCHAR(100) COMMENT '公司',
    project_id BIGINT COMMENT '项目ID',
    payment_method VARCHAR(50) COMMENT '缴款方式',
    payee_name VARCHAR(100) COMMENT '收款人名称',
    payee_account VARCHAR(50) COMMENT '收款人账号',
    payee_bank VARCHAR(100) COMMENT '收款人银行',
    payee VARCHAR(50) COMMENT '收款人',
    payer_name VARCHAR(100) COMMENT '付款人名称',
    tenant_name VARCHAR(100) COMMENT '租户名称',
    contract_no VARCHAR(50) COMMENT '合同编号',
    payer_account VARCHAR(50) COMMENT '付款人账号',
    payer VARCHAR(50) COMMENT '付款人',
    payer_bank_code VARCHAR(50) COMMENT '付款人银行行号',
    transaction_time DATETIME COMMENT '交易时间',
    amount DECIMAL(12,2) COMMENT '金额',
    pending_amount DECIMAL(12,2) COMMENT '待认领金额',
    input_date DATE COMMENT '录入日期',
    claimer VARCHAR(50) COMMENT '认领人',
    claim_date DATE COMMENT '认领日期',
    debit_credit_flag ENUM('DEBIT', 'CREDIT') COMMENT '借贷标记(借方,贷方)',
    summary VARCHAR(200) COMMENT '摘要',
    remark VARCHAR(500) COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (project_id) REFERENCES asset_project(id)
) COMMENT '应收账款表';

-- 插入初始数据
-- 插入默认管理员用户
INSERT INTO sys_user (username, password, real_name, email, phone, create_by) 
VALUES ('admin', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQsKt5rEcBLer9J3jhHOiDK', '管理员', 'admin@cysz.com', '13800138000', 'system');

-- 插入默认角色
INSERT INTO sys_role (role_name, role_code, description) 
VALUES ('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限');

-- 插入菜单权限
INSERT INTO sys_permission (permission_name, permission_code, parent_id, type, path, component, icon, sort_order) VALUES
('系统管理', 'system', 0, 1, '/system', '', 'Setting', 1),
('用户管理', 'system:user', 1, 1, '/system/user', 'system/user/index', 'User', 1),
('角色管理', 'system:role', 1, 1, '/system/role', 'system/role/index', 'UserFilled', 2),
('权限管理', 'system:permission', 1, 1, '/system/permission', 'system/permission/index', 'Lock', 3),
('资产管理', 'asset', 0, 1, '/asset', '', 'OfficeBuilding', 2),
('项目管理', 'asset:project', 4, 1, '/asset/project', 'asset/project/index', 'Postcard', 1),
('楼栋管理', 'asset:building', 4, 1, '/asset/building', 'asset/building/index', 'House', 2),
('单元管理', 'asset:unit', 4, 1, '/asset/unit', 'asset/unit/index', 'Grid', 3),
('单元合并拆分', 'asset:merge-split', 4, 1, '/asset/merge-split', 'asset/merge-split/index', 'Connection', 4),
('租户管理', 'tenant', 0, 1, '/tenant', 'tenant/index', 'Avatar', 3),
('合同管理', 'contract', 0, 1, '/contract', 'contract/index', 'Document', 4),
('应收账款管理', 'receivable', 0, 1, '/receivable', 'receivable/index', 'Money', 5);

-- 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 插入角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id) 
SELECT 1, id FROM sys_permission;