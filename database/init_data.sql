-- 商业地产租赁管理系统初始化数据
-- 包含基础系统数据和示例业务数据

-- 1. 插入系统角色数据
INSERT INTO roles (role_name, role_code, description, create_by) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 'system'),
('项目经理', 'PROJECT_MANAGER', '项目管理人员，负责项目运营管理', 'system'),
('财务人员', 'FINANCE_STAFF', '财务管理人员，负责账务管理', 'system'),
('租赁专员', 'LEASING_SPECIALIST', '租赁业务专员，负责租赁业务', 'system'),
('普通用户', 'NORMAL_USER', '普通系统用户', 'system');

-- 2. 插入系统用户数据
INSERT INTO users (username, password, real_name, email, phone, create_by) VALUES
('admin', '$2a$10$7JB720yubVSOfvVWdBYoOeymw.HR6dDpb2VcGvVz/3P.YGs2Fqtyy', '系统管理员', 'admin@example.com', '13800138000', 'system'),
('manager', '$2a$10$7JB720yubVSOfvVWdBYoOeymw.HR6dDpb2VcGvVz/3P.YGs2Fqtyy', '项目经理', 'manager@example.com', '13800138001', 'system'),
('finance', '$2a$10$7JB720yubVSOfvVWdBYoOeymw.HR6dDpb2VcGvVz/3P.YGs2Fqtyy', '财务专员', 'finance@example.com', '13800138002', 'system');

-- 3. 插入用户角色关联数据
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin -> SUPER_ADMIN
(2, 2), -- manager -> PROJECT_MANAGER
(3, 3); -- finance -> FINANCE_STAFF

-- 4. 插入短信模板数据
INSERT INTO sms_templates (template_name, template_content, template_type, create_by) VALUES
('租金催收提醒', '尊敬的{tenantName}，您的{contractNo}合同租金{amount}元已逾期{days}天，请及时缴纳。咨询电话：400-123-4567', 'REMINDER', 'system'),
('合同到期提醒', '尊敬的{tenantName}，您的{contractNo}合同将于{expireDate}到期，请提前联系续租事宜。咨询电话：400-123-4567', 'NOTICE', 'system'),
('缴费成功通知', '尊敬的{tenantName}，您的{billType}{amount}元已成功缴纳，感谢您的配合！', 'NOTICE', 'system');

-- 5. 插入合同模板数据
INSERT INTO contract_templates (template_name, template_type, template_content, create_by) VALUES
('商铺租赁合同模板', '商铺', '商铺租赁合同模板内容...', 'system'),
('办公租赁合同模板', '办公', '办公租赁合同模板内容...', 'system'),
('车位租赁合同模板', '车位', '车位租赁合同模板内容...', 'system');

-- 6. 插入示例项目数据
INSERT INTO projects (project_name, project_code, project_type, address, total_area, rentable_area, description, create_by) VALUES
('中央商业广场', 'ZYSYGC001', '商业', '北京市朝阳区建国路88号', 50000.00, 45000.00, '大型综合商业广场，集购物、餐饮、娱乐于一体', 'admin'),
('科技创新大厦', 'KJCXDS001', '办公', '上海市浦东新区张江高科技园区', 80000.00, 72000.00, '高端办公楼宇，主要面向科技企业', 'admin'),
('金融中心写字楼', 'JRZXZXL001', '办公', '深圳市福田区金田路3088号', 120000.00, 108000.00, '甲级写字楼，金融机构聚集地', 'admin');

-- 7. 插入示例楼栋数据
INSERT INTO buildings (project_id, building_name, building_code, building_type, total_floors, underground_floors, building_area, rentable_area, create_by) VALUES
(1, 'A座商业楼', 'A', '商业', 6, 2, 25000.00, 22500.00, 'admin'),
(1, 'B座商业楼', 'B', '商业', 6, 2, 25000.00, 22500.00, 'admin'),
(2, '主楼', 'MAIN', '办公', 30, 3, 80000.00, 72000.00, 'admin'),
(3, '东塔', 'EAST', '办公', 50, 4, 60000.00, 54000.00, 'admin'),
(3, '西塔', 'WEST', '办公', 50, 4, 60000.00, 54000.00, 'admin');

-- 8. 插入示例楼层数据
INSERT INTO floors (building_id, floor_name, floor_number, floor_type, floor_area, rentable_area, create_by) VALUES
-- A座商业楼楼层
(1, 'B2层', -2, 'PARKING', 4000.00, 3600.00, 'admin'),
(1, 'B1层', -1, 'COMMERCIAL', 4000.00, 3600.00, 'admin'),
(1, '1层', 1, 'COMMERCIAL', 4000.00, 3600.00, 'admin'),
(1, '2层', 2, 'COMMERCIAL', 4000.00, 3600.00, 'admin'),
(1, '3层', 3, 'COMMERCIAL', 4000.00, 3600.00, 'admin'),
(1, '4层', 4, 'COMMERCIAL', 4000.00, 3600.00, 'admin'),
-- 科技大厦部分楼层
(3, 'B3层', -3, 'PARKING', 2500.00, 2250.00, 'admin'),
(3, 'B2层', -2, 'PARKING', 2500.00, 2250.00, 'admin'),
(3, 'B1层', -1, 'COMMERCIAL', 2500.00, 2250.00, 'admin'),
(3, '1层', 1, 'OFFICE', 2500.00, 2250.00, 'admin'),
(3, '2层', 2, 'OFFICE', 2500.00, 2250.00, 'admin'),
(3, '3层', 3, 'OFFICE', 2500.00, 2250.00, 'admin');

-- 9. 插入示例单元数据
INSERT INTO units (floor_id, unit_name, unit_code, unit_type, building_area, rentable_area, unit_status, rent_status, monthly_rent, deposit_amount, create_by) VALUES
-- A座1层商铺
(3, 'A-1-001', 'A1001', 'SHOP', 150.00, 150.00, 'OCCUPIED', 'RENTED', 25000.00, 75000.00, 'admin'),
(3, 'A-1-002', 'A1002', 'SHOP', 200.00, 200.00, 'OCCUPIED', 'RENTED', 35000.00, 105000.00, 'admin'),
(3, 'A-1-003', 'A1003', 'SHOP', 120.00, 120.00, 'VACANT', 'AVAILABLE', 20000.00, 60000.00, 'admin'),
(3, 'A-1-004', 'A1004', 'SHOP', 180.00, 180.00, 'OCCUPIED', 'RENTED', 30000.00, 90000.00, 'admin'),
-- A座2层商铺
(4, 'A-2-001', 'A2001', 'SHOP', 100.00, 100.00, 'OCCUPIED', 'RENTED', 15000.00, 45000.00, 'admin'),
(4, 'A-2-002', 'A2002', 'SHOP', 120.00, 120.00, 'VACANT', 'AVAILABLE', 18000.00, 54000.00, 'admin'),
(4, 'A-2-003', 'A2003', 'SHOP', 150.00, 150.00, 'OCCUPIED', 'RENTED', 22000.00, 66000.00, 'admin'),
-- 科技大厦办公单元
(10, 'MAIN-1-A', 'M1A', 'OFFICE', 500.00, 450.00, 'OCCUPIED', 'RENTED', 45000.00, 135000.00, 'admin'),
(10, 'MAIN-1-B', 'M1B', 'OFFICE', 300.00, 270.00, 'VACANT', 'AVAILABLE', 27000.00, 81000.00, 'admin'),
(11, 'MAIN-2-A', 'M2A', 'OFFICE', 800.00, 720.00, 'OCCUPIED', 'RENTED', 72000.00, 216000.00, 'admin');

-- 10. 插入示例租户数据
INSERT INTO tenants (tenant_name, tenant_nature, enterprise_nature, social_credit_code, business_format, business_scope, legal_person_name, legal_person_phone, legal_person_id_card, finance_contact, finance_phone, brand, create_by) VALUES
('北京时尚服饰有限公司', '公司', '有限责任公司', '91110000123456789A', '服装零售', '服装、鞋帽、箱包零售', '张三', '13800138001', '110101199001011234', '李四', '13800138002', '时尚前线', 'admin'),
('上海美食餐饮管理有限公司', '公司', '有限责任公司', '91310000987654321B', '餐饮', '中式快餐、饮品制售', '王五', '13800138003', '310101199002021234', '赵六', '13800138004', '美味轩', 'admin'),
('深圳科技创新有限公司', '公司', '有限责任公司', '91440300555666777C', '科技服务', '软件开发、技术咨询', '刘七', '13800138005', '440301199003031234', '陈八', '13800138006', '创新科技', 'admin'),
('广州金融投资有限公司', '公司', '有限责任公司', '91440100111222333D', '金融服务', '投资咨询、资产管理', '周九', '13800138007', '440101199004041234', '吴十', '13800138008', '金投资本', 'admin'),
('杭州电商运营有限公司', '公司', '有限责任公司', '91330100444555666E', '电商服务', '电子商务、网络营销', '郑十一', '13800138009', '330101199005051234', '孙十二', '13800138010', '云商汇', 'admin');

-- 11. 插入示例合同数据
INSERT INTO contracts (contract_no, contract_name, contract_type, project_id, tenant_id, signatory, signatory_phone, business_brand, business_format, sign_date, start_date, end_date, building_area, rentable_area, contract_area, deposit_amount, fee_company, rent_mode, payment_frequency, latest_payment_day, first_payment_latest_date, period_rent, contract_status, create_by) VALUES
('HT202401001', '时尚服饰店租赁合同', '商铺', 1, 1, '张三', '13800138001', '时尚前线', '服装零售', '2024-01-15', '2024-02-01', '2027-01-31', 150.00, 150.00, 150.00, 75000.00, '中央商业广场管理公司', '固定租金', '月付', 10, '2024-02-10', 25000.00, 'SIGNED_EFFECTIVE', 'admin'),
('HT202401002', '美食餐厅租赁合同', '商铺', 1, 2, '王五', '13800138003', '美味轩', '餐饮', '2024-01-20', '2024-02-01', '2026-01-31', 200.00, 200.00, 200.00, 105000.00, '中央商业广场管理公司', '固定租金', '月付', 10, '2024-02-10', 35000.00, 'SIGNED_EFFECTIVE', 'admin'),
('HT202401003', '科技公司办公租赁合同', '办公', 2, 3, '刘七', '13800138005', '创新科技', '科技服务', '2024-01-25', '2024-02-01', '2026-01-31', 500.00, 450.00, 450.00, 135000.00, '科技创新大厦管理公司', '固定租金', '季付', 15, '2024-02-15', 45000.00, 'SIGNED_EFFECTIVE', 'admin'),
('HT202401004', '金融公司办公租赁合同', '办公', 3, 4, '周九', '13800138007', '金投资本', '金融服务', '2024-02-01', '2024-03-01', '2027-02-28', 800.00, 720.00, 720.00, 216000.00, '金融中心管理公司', '固定租金', '季付', 15, '2024-03-15', 72000.00, 'SIGNED_EFFECTIVE', 'admin');

-- 12. 插入合同单元关联数据
INSERT INTO contract_units (contract_id, unit_id) VALUES
(1, 1), -- 时尚服饰店 -> A-1-001
(2, 2), -- 美食餐厅 -> A-1-002
(3, 8), -- 科技公司 -> MAIN-1-A
(4, 10); -- 金融公司 -> MAIN-2-A

-- 13. 插入示例应收账款数据
INSERT INTO receivables (contract_id, tenant_id, bill_type, bill_period, amount, remaining_amount, due_date, bill_status, create_by) VALUES
-- 时尚服饰店租金
(1, 1, '租金', '2024年2月', 25000.00, 0.00, '2024-02-10', 'COMPLETED', 'admin'),
(1, 1, '租金', '2024年3月', 25000.00, 0.00, '2024-03-10', 'COMPLETED', 'admin'),
(1, 1, '租金', '2024年4月', 25000.00, 25000.00, '2024-04-10', 'PENDING', 'admin'),
-- 美食餐厅租金
(2, 2, '租金', '2024年2月', 35000.00, 0.00, '2024-02-10', 'COMPLETED', 'admin'),
(2, 2, '租金', '2024年3月', 35000.00, 15000.00, '2024-03-10', 'PARTIAL', 'admin'),
(2, 2, '租金', '2024年4月', 35000.00, 35000.00, '2024-04-10', 'PENDING', 'admin'),
-- 科技公司租金（季付）
(3, 3, '租金', '2024年Q1', 135000.00, 0.00, '2024-02-15', 'COMPLETED', 'admin'),
(3, 3, '租金', '2024年Q2', 135000.00, 135000.00, '2024-05-15', 'PENDING', 'admin'),
-- 金融公司租金（季付）
(4, 4, '租金', '2024年Q1', 216000.00, 0.00, '2024-03-15', 'COMPLETED', 'admin'),
(4, 4, '租金', '2024年Q2', 216000.00, 216000.00, '2024-06-15', 'PENDING', 'admin');

-- 14. 插入示例收款记录数据
INSERT INTO received_payments (receivable_id, contract_id, tenant_id, payment_amount, payment_date, payment_method, bank_transaction_no, payment_account, match_status, create_by) VALUES
(1, 1, 1, 25000.00, '2024-02-08', '银行转账', 'TXN202402080001', '招商银行***1234', 'MATCHED', 'admin'),
(2, 1, 1, 25000.00, '2024-03-08', '银行转账', 'TXN202403080001', '招商银行***1234', 'MATCHED', 'admin'),
(4, 2, 2, 35000.00, '2024-02-09', '银行转账', 'TXN202402090001', '工商银行***5678', 'MATCHED', 'admin'),
(5, 2, 2, 20000.00, '2024-03-12', '银行转账', 'TXN202403120001', '工商银行***5678', 'MATCHED', 'admin'),
(7, 3, 3, 135000.00, '2024-02-14', '银行转账', 'TXN202402140001', '建设银行***9012', 'MATCHED', 'admin'),
(9, 4, 4, 216000.00, '2024-03-14', '银行转账', 'TXN202403140001', '中国银行***3456', 'MATCHED', 'admin');

-- 15. 插入示例银行流水数据
INSERT INTO bank_transactions (transaction_no, transaction_date, amount, transaction_type, payer_name, payer_account, payee_name, payee_account, transaction_desc, match_status, matched_receivable_id) VALUES
('TXN202402080001', '2024-02-08', 25000.00, 'INCOME', '北京时尚服饰有限公司', '招商银行***1234', '中央商业广场管理公司', '招商银行***0001', '租金缴费', 'MATCHED', 1),
('TXN202403080001', '2024-03-08', 25000.00, 'INCOME', '北京时尚服饰有限公司', '招商银行***1234', '中央商业广场管理公司', '招商银行***0001', '租金缴费', 'MATCHED', 2),
('TXN202402090001', '2024-02-09', 35000.00, 'INCOME', '上海美食餐饮管理有限公司', '工商银行***5678', '中央商业广场管理公司', '招商银行***0001', '租金缴费', 'MATCHED', 4),
('TXN202403120001', '2024-03-12', 20000.00, 'INCOME', '上海美食餐饮管理有限公司', '工商银行***5678', '中央商业广场管理公司', '招商银行***0001', '租金缴费', 'MATCHED', 5),
('TXN202402140001', '2024-02-14', 135000.00, 'INCOME', '深圳科技创新有限公司', '建设银行***9012', '科技创新大厦管理公司', '建设银行***0002', '季度租金', 'MATCHED', 7),
('TXN202403140001', '2024-03-14', 216000.00, 'INCOME', '广州金融投资有限公司', '中国银行***3456', '金融中心管理公司', '中国银行***0003', '季度租金', 'MATCHED', 9),
('TXN202404050001', '2024-04-05', 50000.00, 'INCOME', '未知付款人', '农业银行***7890', '中央商业广场管理公司', '招商银行***0001', '转账', 'UNMATCHED', NULL);

-- 16. 插入示例催收提醒数据
INSERT INTO collection_reminders (receivable_id, tenant_id, reminder_type, reminder_content, reminder_date, reminder_status, priority, template_id, create_by) VALUES
(3, 1, 'SMS', '尊敬的北京时尚服饰有限公司，您的HT202401001合同租金25000元已逾期5天，请及时缴纳。咨询电话：400-123-4567', '2024-04-15', 'SENT', 'NORMAL', 1, 'admin'),
(5, 2, 'SMS', '尊敬的上海美食餐饮管理有限公司，您的HT202401002合同租金15000元已逾期8天，请及时缴纳。咨询电话：400-123-4567', '2024-03-18', 'SENT', 'HIGH', 1, 'admin'),
(6, 2, 'SMS', '尊敬的上海美食餐饮管理有限公司，您的HT202401002合同租金35000元已逾期5天，请及时缴纳。咨询电话：400-123-4567', '2024-04-15', 'SENT', 'NORMAL', 1, 'admin');

-- 更新应收账款的已收金额和剩余金额
UPDATE receivables SET received_amount = 25000.00, remaining_amount = 0.00 WHERE id = 1;
UPDATE receivables SET received_amount = 25000.00, remaining_amount = 0.00 WHERE id = 2;
UPDATE receivables SET received_amount = 35000.00, remaining_amount = 0.00 WHERE id = 4;
UPDATE receivables SET received_amount = 20000.00, remaining_amount = 15000.00 WHERE id = 5;
UPDATE receivables SET received_amount = 135000.00, remaining_amount = 0.00 WHERE id = 7;
UPDATE receivables SET received_amount = 216000.00, remaining_amount = 0.00 WHERE id = 9;

-- 更新单元状态
UPDATE units SET unit_status = 'OCCUPIED', rent_status = 'RENTED' WHERE id IN (1, 2, 8, 10);
UPDATE units SET unit_status = 'VACANT', rent_status = 'AVAILABLE' WHERE id IN (3, 6, 9);