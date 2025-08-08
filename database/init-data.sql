-- 云联智管地产资管系统初始化数据
USE cysz;

-- 清空现有数据（谨慎使用）
-- TRUNCATE TABLE sys_user_role;
-- TRUNCATE TABLE sys_role_permission;
-- TRUNCATE TABLE sys_permission;
-- TRUNCATE TABLE sys_role;
-- TRUNCATE TABLE sys_user;

-- 插入系统权限数据
INSERT INTO sys_permission (id, permission_name, permission_code, parent_id, type, path, component, icon, sort_order) VALUES
(1, '系统管理', 'system', 0, 1, '/system', '', 'Setting', 1),
(2, '用户管理', 'system:user', 1, 1, '/system/user', 'system/user/index', 'User', 1),
(3, '角色管理', 'system:role', 1, 1, '/system/role', 'system/role/index', 'UserFilled', 2),
(4, '资产管理', 'asset', 0, 1, '/asset', '', 'OfficeBuilding', 2),
(5, '项目管理', 'asset:project', 4, 1, '/asset/project', 'asset/project/index', 'House', 1),
(6, '楼栋管理', 'asset:building', 4, 1, '/asset/building', 'asset/building/index', 'Building', 2),
(7, '单元管理', 'asset:unit', 4, 1, '/asset/unit', 'asset/unit/index', 'Grid', 3),
(8, '单元合并拆分', 'asset:merge-split', 4, 1, '/asset/merge-split', 'asset/merge-split/index', 'Connection', 4),
(9, '租户管理', 'tenant', 0, 1, '/tenant', 'tenant/index', 'Avatar', 3),
(10, '合同管理', 'contract', 0, 1, '/contract', 'contract/index', 'Document', 4),
(11, '应收账款管理', 'receivable', 0, 1, '/receivable', 'receivable/index', 'Money', 5),

-- 按钮权限
(101, '用户新增', 'system:user:add', 2, 2, '', '', '', 1),
(102, '用户编辑', 'system:user:edit', 2, 2, '', '', '', 2),
(103, '用户删除', 'system:user:delete', 2, 2, '', '', '', 3),
(104, '用户查询', 'system:user:query', 2, 2, '', '', '', 4),

(201, '角色新增', 'system:role:add', 3, 2, '', '', '', 1),
(202, '角色编辑', 'system:role:edit', 3, 2, '', '', '', 2),
(203, '角色删除', 'system:role:delete', 3, 2, '', '', '', 3),
(204, '角色查询', 'system:role:query', 3, 2, '', '', '', 4),

(301, '项目新增', 'asset:project:add', 5, 2, '', '', '', 1),
(302, '项目编辑', 'asset:project:edit', 5, 2, '', '', '', 2),
(303, '项目删除', 'asset:project:delete', 5, 2, '', '', '', 3),
(304, '项目查询', 'asset:project:query', 5, 2, '', '', '', 4),

(401, '楼栋新增', 'asset:building:add', 6, 2, '', '', '', 1),
(402, '楼栋编辑', 'asset:building:edit', 6, 2, '', '', '', 2),
(403, '楼栋删除', 'asset:building:delete', 6, 2, '', '', '', 3),
(404, '楼栋查询', 'asset:building:query', 6, 2, '', '', '', 4),

(501, '单元新增', 'asset:unit:add', 7, 2, '', '', '', 1),
(502, '单元编辑', 'asset:unit:edit', 7, 2, '', '', '', 2),
(503, '单元删除', 'asset:unit:delete', 7, 2, '', '', '', 3),
(504, '单元查询', 'asset:unit:query', 7, 2, '', '', '', 4),

(601, '单元合并', 'asset:merge-split:merge', 8, 2, '', '', '', 1),
(602, '单元拆分', 'asset:merge-split:split', 8, 2, '', '', '', 2),
(603, '操作撤销', 'asset:merge-split:revert', 8, 2, '', '', '', 3),

(701, '租户新增', 'tenant:add', 9, 2, '', '', '', 1),
(702, '租户编辑', 'tenant:edit', 9, 2, '', '', '', 2),
(703, '租户删除', 'tenant:delete', 9, 2, '', '', '', 3),
(704, '租户查询', 'tenant:query', 9, 2, '', '', '', 4),

(801, '合同新增', 'contract:add', 10, 2, '', '', '', 1),
(802, '合同编辑', 'contract:edit', 10, 2, '', '', '', 2),
(803, '合同删除', 'contract:delete', 10, 2, '', '', '', 3),
(804, '合同查询', 'contract:query', 10, 2, '', '', '', 4),

(901, '账款新增', 'receivable:add', 11, 2, '', '', '', 1),
(902, '账款编辑', 'receivable:edit', 11, 2, '', '', '', 2),
(903, '账款删除', 'receivable:delete', 11, 2, '', '', '', 3),
(904, '账款查询', 'receivable:query', 11, 2, '', '', '', 4),
(905, '账款认领', 'receivable:claim', 11, 2, '', '', '', 5);

-- 插入系统角色
INSERT INTO sys_role (id, role_name, role_code, description) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限'),
(2, '系统管理员', 'SYSTEM_ADMIN', '系统管理员，负责用户和权限管理'),
(3, '资产管理员', 'ASSET_ADMIN', '资产管理员，负责资产相关功能'),
(4, '业务管理员', 'BUSINESS_ADMIN', '业务管理员，负责租户、合同、账款管理'),
(5, '普通用户', 'USER', '普通用户，只有查看权限');

-- 插入默认管理员用户
INSERT INTO sys_user (id, username, password, real_name, email, phone, create_by) VALUES
(1, 'admin', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQsKt5rEcBLer9J3jhHOiDK', '系统管理员', 'admin@cysz.com', '13800138000', 'system'),
(2, 'asset_admin', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQsKt5rEcBLer9J3jhHOiDK', '资产管理员', 'asset@cysz.com', '13800138001', 'admin'),
(3, 'business_admin', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQsKt5rEcBLer9J3jhHOiDK', '业务管理员', 'business@cysz.com', '13800138002', 'admin');

-- 分配用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), -- admin -> 超级管理员
(2, 3), -- asset_admin -> 资产管理员
(3, 4); -- business_admin -> 业务管理员

-- 分配角色权限（超级管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- 系统管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(2, 1), (2, 2), (2, 3), -- 系统管理相关
(2, 101), (2, 102), (2, 103), (2, 104), -- 用户管理
(2, 201), (2, 202), (2, 203), (2, 204); -- 角色管理

-- 资产管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(3, 4), (3, 5), (3, 6), (3, 7), (3, 8), -- 资产管理相关菜单
(3, 301), (3, 302), (3, 303), (3, 304), -- 项目管理
(3, 401), (3, 402), (3, 403), (3, 404), -- 楼栋管理
(3, 501), (3, 502), (3, 503), (3, 504), -- 单元管理
(3, 601), (3, 602), (3, 603); -- 单元合并拆分

-- 业务管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(4, 9), (4, 10), (4, 11), -- 业务管理相关菜单
(4, 701), (4, 702), (4, 703), (4, 704), -- 租户管理
(4, 801), (4, 802), (4, 803), (4, 804), -- 合同管理
(4, 901), (4, 902), (4, 903), (4, 904), (4, 905); -- 应收账款管理

-- 普通用户权限（只有查询权限）
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(5, 4), (5, 5), (5, 6), (5, 7), (5, 9), (5, 10), (5, 11), -- 菜单权限
(5, 304), (5, 404), (5, 504), (5, 704), (5, 804), (5, 904); -- 查询权限

-- 插入示例项目数据
INSERT INTO asset_project (id, project_name, project_type, company_name, rent_bill_company, property_bill_company, property_right_company, building_area, rent_area, property_area, city, address, create_by) VALUES
(1, '万达广场', 'COMPLEX', '万达集团', '万达物业', '万达物业', '万达集团', 50000.00, 48000.00, 49000.00, '北京', '北京市朝阳区建国路93号', 'admin'),
(2, '中关村写字楼', 'OFFICE', '中关村科技', '中关村物业', '中关村物业', '中关村科技', 30000.00, 28500.00, 29000.00, '北京', '北京市海淀区中关村大街1号', 'admin'),
(3, '阳光公寓', 'APARTMENT', '阳光地产', '阳光物业', '阳光物业', '阳光地产', 20000.00, 19000.00, 19500.00, '上海', '上海市浦东新区世纪大道100号', 'admin');

-- 插入示例楼栋数据
INSERT INTO asset_building (id, building_name, building_code, project_id, building_area, rent_area, property_area, usable_area, create_by) VALUES
(1, 'A栋', 'A001', 1, 15000.00, 14200.00, 14500.00, 13800.00, 'admin'),
(2, 'B栋', 'B001', 1, 12000.00, 11400.00, 11600.00, 11000.00, 'admin'),
(3, '主楼', 'M001', 2, 20000.00, 19000.00, 19500.00, 18500.00, 'admin'),
(4, '1号楼', '001', 3, 8000.00, 7600.00, 7800.00, 7400.00, 'admin'),
(5, '2号楼', '002', 3, 7000.00, 6650.00, 6800.00, 6400.00, 'admin');

-- 插入示例单元数据
INSERT INTO asset_unit (id, unit_code, unit_description, project_id, building_id, unit_status, unit_purpose, building_area, rent_area, property_area, create_by) VALUES
(1, 'A001-101', 'A栋1层101室', 1, 1, 'VACANT', '办公', 120.50, 115.00, 118.00, 'admin'),
(2, 'A001-102', 'A栋1层102室', 1, 1, 'OCCUPIED', '办公', 95.80, 90.00, 92.50, 'admin'),
(3, 'A001-201', 'A栋2层201室', 1, 1, 'VACANT', '办公', 150.00, 142.00, 146.00, 'admin'),
(4, 'B001-101', 'B栋1层101室', 1, 2, 'MAINTENANCE', '商铺', 200.00, 190.00, 195.00, 'admin'),
(5, 'M001-1001', '主楼10层1001室', 2, 3, 'OCCUPIED', '办公', 180.00, 170.00, 175.00, 'admin');

-- 插入示例租户数据
INSERT INTO tenant_info (id, tenant_code, tenant_name, tenant_category, project_id, social_credit_code, certificate_type, taxpayer_id, business_license, legal_person, registered_address, contact_phone, contact_email, create_by) VALUES
(1, 'T001', '北京科技有限公司', '企业', 1, '91110000123456789X', '营业执照', '91110000123456789X', 'BL001', '张三', '北京市朝阳区科技园区1号', '13800138001', 'zhangsan@tech.com', 'admin'),
(2, 'T002', '上海贸易公司', '企业', 2, '91310000987654321Y', '营业执照', '91310000987654321Y', 'BL002', '李四', '上海市浦东新区贸易大厦2号', '13800138002', 'lisi@trade.com', 'admin'),
(3, 'T003', '个人租户王五', '个人', 3, '', '身份证', '', '', '王五', '广州市天河区住宅小区3号', '13800138003', 'wangwu@personal.com', 'admin');

-- 插入示例合同数据
INSERT INTO contract_info (id, contract_no, contract_name, project_id, start_date, end_date, signatory, contract_type, contract_status, rent_bill_company, property_bill_company, lease_no, tenant_id, tenant_name, unit_id, unit_description, rent_mode, monthly_rent, deposit, create_by) VALUES
(1, 'HT001', '万达广场租赁合同', 1, '2024-01-01', '2024-12-31', '张经理', '办公租赁', 'ACTIVE', '万达物业', '万达物业', 'L001', 1, '北京科技有限公司', 2, 'A栋1层102室', '月付', 15000.00, 45000.00, 'admin'),
(2, 'HT002', '中关村写字楼租赁合同', 2, '2024-02-01', '2025-01-31', '李经理', '办公租赁', 'ACTIVE', '中关村物业', '中关村物业', 'L002', 2, '上海贸易公司', 5, '主楼10层1001室', '季付', 25000.00, 75000.00, 'admin'),
(3, 'HT003', '阳光公寓租赁合同', 3, '2024-03-01', '2025-02-28', '王经理', '住宅租赁', 'DRAFT', '阳光物业', '阳光物业', 'L003', 3, '个人租户王五', 1, 'A栋1层101室', '月付', 8000.00, 24000.00, 'admin');

-- 插入示例应收账款数据
INSERT INTO receivable_account (id, batch_no, line_no, process_status, company, project_id, payment_method, payee_name, payee_account, payee_bank, payee, payer_name, tenant_name, contract_no, payer_account, payer, payer_bank_code, transaction_time, amount, pending_amount, input_date, claimer, claim_date, debit_credit_flag, summary, remark, create_by) VALUES
(1, 'B202401001', '001', 'COMPLETED', '万达集团', 1, '银行转账', '万达物业', '1234567890', '中国银行', '财务部', '北京科技有限公司', '北京科技有限公司', 'HT001', '0987654321', '张三', 'BOC001', '2024-01-15 10:30:00', 15000.00, 0.00, '2024-01-15', '财务小李', '2024-01-15', 'CREDIT', '2024年1月租金', '正常收款', 'admin'),
(2, 'B202401002', '002', 'PENDING', '中关村科技', 2, '银行转账', '中关村物业', '2345678901', '工商银行', '财务部', '上海贸易公司', '上海贸易公司', 'HT002', '1098765432', '李四', 'ICBC001', '2024-01-16 14:20:00', 75000.00, 75000.00, '2024-01-16', '', '', 'CREDIT', '2024年第一季度租金', '待认领', 'admin'),
(3, 'B202401003', '003', 'PROCESSING', '阳光地产', 3, '现金', '阳光物业', '3456789012', '建设银行', '财务部', '个人租户王五', '个人租户王五', 'HT003', '', '王五', '', '2024-01-17 09:00:00', 8000.00, 8000.00, '2024-01-17', '', '', 'CREDIT', '2024年1月租金', '现金收款，处理中', 'admin');

-- 提交事务
COMMIT;