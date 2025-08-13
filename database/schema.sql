-- 商业地产租赁管理系统数据库设计
-- 基于前端API需求分析的完整数据库架构

-- 1. 项目表 (projects)
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    project_code VARCHAR(50) UNIQUE NOT NULL COMMENT '项目编码',
    project_type VARCHAR(20) NOT NULL COMMENT '项目类型：商业/办公/住宅/综合',
    address VARCHAR(200) COMMENT '项目地址',
    total_area DECIMAL(10,2) COMMENT '总建筑面积(平方米)',
    rentable_area DECIMAL(10,2) COMMENT '可租赁面积(平方米)',
    project_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '项目状态：ACTIVE/INACTIVE',
    description TEXT COMMENT '项目描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识'
) COMMENT='项目表';

-- 2. 楼栋表 (buildings)
CREATE TABLE buildings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    building_name VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    building_code VARCHAR(50) NOT NULL COMMENT '楼栋编码',
    building_type VARCHAR(20) COMMENT '楼栋类型',
    total_floors INT COMMENT '总楼层数',
    underground_floors INT DEFAULT 0 COMMENT '地下楼层数',
    building_area DECIMAL(10,2) COMMENT '楼栋建筑面积',
    rentable_area DECIMAL(10,2) COMMENT '可租赁面积',
    building_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '楼栋状态',
    description TEXT COMMENT '楼栋描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    FOREIGN KEY (project_id) REFERENCES projects(id),
    UNIQUE KEY uk_project_building (project_id, building_code)
) COMMENT='楼栋表';

-- 3. 楼层表 (floors)
CREATE TABLE floors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼层ID',
    building_id BIGINT NOT NULL COMMENT '所属楼栋ID',
    floor_name VARCHAR(50) NOT NULL COMMENT '楼层名称',
    floor_number INT NOT NULL COMMENT '楼层号',
    floor_type VARCHAR(20) COMMENT '楼层类型：COMMERCIAL/OFFICE/PARKING',
    floor_area DECIMAL(10,2) COMMENT '楼层面积',
    rentable_area DECIMAL(10,2) COMMENT '可租赁面积',
    floor_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '楼层状态',
    description TEXT COMMENT '楼层描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    FOREIGN KEY (building_id) REFERENCES buildings(id),
    UNIQUE KEY uk_building_floor (building_id, floor_number)
) COMMENT='楼层表';

-- 4. 单元表 (units)
CREATE TABLE units (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '单元ID',
    floor_id BIGINT NOT NULL COMMENT '所属楼层ID',
    unit_name VARCHAR(100) NOT NULL COMMENT '单元名称',
    unit_code VARCHAR(50) NOT NULL COMMENT '单元编码',
    unit_type VARCHAR(20) COMMENT '单元类型：SHOP/OFFICE/APARTMENT/PARKING',
    building_area DECIMAL(10,2) COMMENT '建筑面积(平方米)',
    rentable_area DECIMAL(10,2) COMMENT '计租面积(平方米)',
    unit_status VARCHAR(20) DEFAULT 'VACANT' COMMENT '单元状态：VACANT/OCCUPIED/MAINTENANCE',
    rent_status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '租赁状态：AVAILABLE/RENTED/RESERVED',
    monthly_rent DECIMAL(10,2) COMMENT '月租金',
    deposit_amount DECIMAL(10,2) COMMENT '押金金额',
    description TEXT COMMENT '单元描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    FOREIGN KEY (floor_id) REFERENCES floors(id),
    UNIQUE KEY uk_floor_unit (floor_id, unit_code)
) COMMENT='单元表';

-- 5. 租户表 (tenants)
CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '租户ID',
    tenant_name VARCHAR(200) NOT NULL COMMENT '租户名称',
    tenant_nature VARCHAR(20) NOT NULL COMMENT '租户性质：个人/公司/政府机构',
    enterprise_nature VARCHAR(100) COMMENT '企业性质',
    social_credit_code VARCHAR(50) COMMENT '社会信用代码',
    taxpayer_id VARCHAR(50) COMMENT '纳税人识别号',
    business_registration_number VARCHAR(50) COMMENT '工商注册号',
    individual_license_number VARCHAR(50) COMMENT '个体户证件号',
    brand VARCHAR(100) COMMENT '品牌',
    brand_qualification VARCHAR(50) COMMENT '品牌资质',
    business_format VARCHAR(50) COMMENT '业态',
    business_scope TEXT COMMENT '经营范围',
    legal_person_name VARCHAR(50) COMMENT '法人姓名',
    legal_person_phone VARCHAR(20) COMMENT '法人手机号',
    legal_person_id_card VARCHAR(30) COMMENT '法人身份证',
    finance_contact VARCHAR(50) COMMENT '财务联系人',
    finance_phone VARCHAR(20) COMMENT '财务电话',
    payer_name VARCHAR(100) COMMENT '付款人名称',
    payment_account VARCHAR(100) COMMENT '付款账号',
    remark TEXT COMMENT '备注',
    tenant_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '租户状态：ACTIVE/INACTIVE',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识'
) COMMENT='租户表';

-- 6. 合同表 (contracts)
CREATE TABLE contracts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    contract_no VARCHAR(50) UNIQUE NOT NULL COMMENT '合同编号',
    contract_name VARCHAR(200) NOT NULL COMMENT '合同名称',
    contract_type VARCHAR(20) NOT NULL COMMENT '合同类型：商铺/办公/公寓/酒店/车位/广告/场地/多经点位',
    project_id BIGINT NOT NULL COMMENT '所属项目ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    signatory VARCHAR(50) COMMENT '签订人',
    signatory_phone VARCHAR(20) COMMENT '签订人手机号',
    business_brand VARCHAR(100) COMMENT '经营品牌',
    shop_sign VARCHAR(100) COMMENT '店招',
    business_format VARCHAR(50) COMMENT '业态',
    sign_date DATE COMMENT '签订日期',
    start_date DATE NOT NULL COMMENT '开始时间',
    end_date DATE NOT NULL COMMENT '结束时间',
    building_area DECIMAL(10,2) COMMENT '建筑面积(平方米)',
    rentable_area DECIMAL(10,2) COMMENT '计租面积(平方米)',
    contract_area DECIMAL(10,2) COMMENT '签约面积(平方米)',
    deposit_amount DECIMAL(10,2) COMMENT '保证金含税金额(元)',
    deposit_latest_date DATE COMMENT '保证金最晚收取日期',
    fee_company VARCHAR(100) COMMENT '费项公司',
    rent_mode VARCHAR(20) DEFAULT '固定租金' COMMENT '租金模式：固定租金/提成租金',
    rent_period_setting VARCHAR(100) COMMENT '租金期间设定',
    late_fee_rule VARCHAR(200) COMMENT '滞纳金规则',
    payment_account VARCHAR(100) COMMENT '付款账户',
    payment_frequency VARCHAR(20) COMMENT '付款频率：月付/季付/半年付/年付',
    latest_payment_day INT COMMENT '最晚缴款日',
    first_payment_latest_date DATE COMMENT '首期最晚缴款日',
    first_period_rent DECIMAL(10,2) COMMENT '首期租金(元)',
    period_rent DECIMAL(10,2) COMMENT '每期租金(元)',
    commission_rules JSON COMMENT '提成规则(JSON格式)',
    contract_status VARCHAR(30) DEFAULT 'UNSIGNED_EFFECTIVE' COMMENT '合同状态：UNSIGNED_EFFECTIVE/SIGNED_EFFECTIVE/TERMINATED',
    contract_file_url VARCHAR(500) COMMENT '合同文件URL',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) COMMENT='合同表';

-- 7. 合同单元关联表 (contract_units)
CREATE TABLE contract_units (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    contract_id BIGINT NOT NULL COMMENT '合同ID',
    unit_id BIGINT NOT NULL COMMENT '单元ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (contract_id) REFERENCES contracts(id),
    FOREIGN KEY (unit_id) REFERENCES units(id),
    UNIQUE KEY uk_contract_unit (contract_id, unit_id)
) COMMENT='合同单元关联表';

-- 8. 应收账款表 (receivables)
CREATE TABLE receivables (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '应收账款ID',
    contract_id BIGINT NOT NULL COMMENT '合同ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    bill_type VARCHAR(50) NOT NULL COMMENT '账单类型：租金/物业费/水费/电费/保证金等',
    bill_period VARCHAR(20) COMMENT '账单期间',
    amount DECIMAL(12,2) NOT NULL COMMENT '应收金额',
    received_amount DECIMAL(12,2) DEFAULT 0 COMMENT '已收金额',
    remaining_amount DECIMAL(12,2) NOT NULL COMMENT '剩余金额',
    due_date DATE NOT NULL COMMENT '到期日期',
    bill_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '账单状态：PENDING/PARTIAL/COMPLETED/OVERDUE',
    late_fee DECIMAL(10,2) DEFAULT 0 COMMENT '滞纳金',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    FOREIGN KEY (contract_id) REFERENCES contracts(id),
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) COMMENT='应收账款表';

-- 9. 收款记录表 (received_payments)
CREATE TABLE received_payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收款记录ID',
    receivable_id BIGINT COMMENT '关联应收账款ID',
    contract_id BIGINT NOT NULL COMMENT '合同ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    payment_amount DECIMAL(12,2) NOT NULL COMMENT '收款金额',
    payment_date DATE NOT NULL COMMENT '收款日期',
    payment_method VARCHAR(20) COMMENT '收款方式：银行转账/现金/支票等',
    bank_transaction_no VARCHAR(100) COMMENT '银行流水号',
    payment_account VARCHAR(100) COMMENT '收款账户',
    match_status VARCHAR(20) DEFAULT 'UNMATCHED' COMMENT '匹配状态：UNMATCHED/MATCHED/MANUAL',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    FOREIGN KEY (receivable_id) REFERENCES receivables(id),
    FOREIGN KEY (contract_id) REFERENCES contracts(id),
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) COMMENT='收款记录表';

-- 10. 银行流水表 (bank_transactions)
CREATE TABLE bank_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '银行流水ID',
    transaction_no VARCHAR(100) UNIQUE NOT NULL COMMENT '流水号',
    transaction_date DATE NOT NULL COMMENT '交易日期',
    transaction_time TIME COMMENT '交易时间',
    amount DECIMAL(12,2) NOT NULL COMMENT '交易金额',
    transaction_type VARCHAR(20) NOT NULL COMMENT '交易类型：INCOME/EXPENSE',
    payer_name VARCHAR(100) COMMENT '付款人名称',
    payer_account VARCHAR(100) COMMENT '付款账号',
    payee_name VARCHAR(100) COMMENT '收款人名称',
    payee_account VARCHAR(100) COMMENT '收款账号',
    transaction_desc TEXT COMMENT '交易描述',
    match_status VARCHAR(20) DEFAULT 'UNMATCHED' COMMENT '匹配状态：UNMATCHED/MATCHED/IGNORED',
    matched_receivable_id BIGINT COMMENT '匹配的应收账款ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (matched_receivable_id) REFERENCES receivables(id)
) COMMENT='银行流水表';

-- 11. 催收提醒表 (collection_reminders)
CREATE TABLE collection_reminders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '催收提醒ID',
    receivable_id BIGINT NOT NULL COMMENT '应收账款ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    reminder_type VARCHAR(20) NOT NULL COMMENT '提醒类型：SMS/EMAIL/PHONE',
    reminder_content TEXT COMMENT '提醒内容',
    reminder_date DATE NOT NULL COMMENT '提醒日期',
    reminder_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '提醒状态：PENDING/SENT/FAILED',
    priority VARCHAR(20) DEFAULT 'NORMAL' COMMENT '优先级：LOW/NORMAL/HIGH/URGENT',
    template_id BIGINT COMMENT '短信模板ID',
    send_result TEXT COMMENT '发送结果',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (receivable_id) REFERENCES receivables(id),
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) COMMENT='催收提醒表';

-- 12. 短信模板表 (sms_templates)
CREATE TABLE sms_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_content TEXT NOT NULL COMMENT '模板内容',
    template_type VARCHAR(20) NOT NULL COMMENT '模板类型：REMINDER/NOTICE/MARKETING',
    template_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '模板状态：ACTIVE/INACTIVE',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人'
) COMMENT='短信模板表';

-- 13. 系统用户表 (users)
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(加密)',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    user_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '用户状态：ACTIVE/INACTIVE/LOCKED',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识'
) COMMENT='系统用户表';

-- 14. 角色表 (roles)
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) UNIQUE NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) UNIQUE NOT NULL COMMENT '角色编码',
    description TEXT COMMENT '角色描述',
    role_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '角色状态：ACTIVE/INACTIVE',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识'
) COMMENT='角色表';

-- 15. 用户角色关联表 (user_roles)
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) COMMENT='用户角色关联表';

-- 16. 合同模板表 (contract_templates)
CREATE TABLE contract_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_type VARCHAR(20) NOT NULL COMMENT '模板类型',
    template_content LONGTEXT COMMENT '模板内容',
    template_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '模板状态：ACTIVE/INACTIVE',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识'
) COMMENT='合同模板表';

-- 创建索引
CREATE INDEX idx_projects_code ON projects(project_code);
CREATE INDEX idx_buildings_project ON buildings(project_id);
CREATE INDEX idx_floors_building ON floors(building_id);
CREATE INDEX idx_units_floor ON units(floor_id);
CREATE INDEX idx_units_status ON units(unit_status, rent_status);
CREATE INDEX idx_tenants_name ON tenants(tenant_name);
CREATE INDEX idx_contracts_no ON contracts(contract_no);
CREATE INDEX idx_contracts_tenant ON contracts(tenant_id);
CREATE INDEX idx_contracts_project ON contracts(project_id);
CREATE INDEX idx_contracts_dates ON contracts(start_date, end_date);
CREATE INDEX idx_receivables_contract ON receivables(contract_id);
CREATE INDEX idx_receivables_tenant ON receivables(tenant_id);
CREATE INDEX idx_receivables_due_date ON receivables(due_date);
CREATE INDEX idx_receivables_status ON receivables(bill_status);
CREATE INDEX idx_payments_receivable ON received_payments(receivable_id);
CREATE INDEX idx_payments_date ON received_payments(payment_date);
CREATE INDEX idx_bank_transactions_no ON bank_transactions(transaction_no);
CREATE INDEX idx_bank_transactions_date ON bank_transactions(transaction_date);
CREATE INDEX idx_reminders_receivable ON collection_reminders(receivable_id);
CREATE INDEX idx_reminders_date ON collection_reminders(reminder_date);
CREATE INDEX idx_users_username ON users(username);