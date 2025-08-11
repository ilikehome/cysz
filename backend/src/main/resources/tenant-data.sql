-- 多租户架构测试数据
-- 创建时间：2025-01-11

-- 插入租户组织数据
INSERT INTO tenant_organization (id, org_code, org_name, org_type, contact_person, contact_phone, contact_email, address, description, license_number, established_date, status, subscription_plan, subscription_start_date, subscription_end_date, max_users, max_projects) VALUES
(1, 'ORG001', '万达集团', 'COMPANY', '王健林', '400-888-0000', 'contact@wanda.com', '北京市朝阳区建国路93号万达广场', '中国知名商业地产开发运营企业', '91110000123456789A', '1988-01-01', 'ACTIVE', 'ENTERPRISE', '2024-01-01', '2025-12-31', 500, 100),
(2, 'ORG002', '保利地产', 'COMPANY', '宋广菊', '400-820-8888', 'service@polycn.com', '广州市海珠区保利中心', '中央企业保利集团控股的大型国有房地产企业', '91440000987654321B', '1992-08-01', 'ACTIVE', 'PREMIUM', '2024-01-01', '2025-12-31', 200, 50),
(3, 'ORG003', '绿地控股', 'COMPANY', '张玉良', '021-33398000', 'info@greenland.com', '上海市静安区绿地中心', '中国领先的超大型城市运营商', '91310000456789012C', '1992-07-18', 'ACTIVE', 'STANDARD', '2024-01-01', '2025-12-31', 100, 30),
(4, 'ORG004', '碧桂园集团', 'COMPANY', '杨惠妍', '0757-26677777', 'hr@countrygarden.com.cn', '广东省佛山市顺德区北滘镇碧桂园总部大楼', '中国领先的新型城镇化住宅开发商', '91440000789012345D', '1992-01-01', 'ACTIVE', 'PREMIUM', '2024-01-01', '2025-12-31', 300, 80),
(5, 'ORG005', '华润置地', 'COMPANY', '李欣', '0755-82839999', 'crland@crc.com.hk', '深圳市南山区华润城', '华润集团旗下地产业务旗舰', '91440300567890123E', '1994-01-01', 'ACTIVE', 'ENTERPRISE', '2024-01-01', '2025-12-31', 400, 90);

-- 插入系统用户数据
INSERT INTO system_user (id, tenant_id, username, password, real_name, email, phone, department, position, role, permissions, status) VALUES
-- 万达集团用户
(1, 1, 'admin', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '王健林', 'wjl@wanda.com', '13800000001', '董事会', '董事长', 'TENANT_ADMIN', '["ALL"]', 'ACTIVE'),
(2, 1, 'wanda_manager', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '张经理', 'zhangmgr@wanda.com', '13800000002', '资产管理部', '资产经理', 'ADMIN', '["asset:*", "tenant:*", "contract:*"]', 'ACTIVE'),
(3, 1, 'wanda_user', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '李员工', 'liuser@wanda.com', '13800000003', '运营部', '运营专员', 'USER', '["dashboard:view", "asset:view"]', 'ACTIVE'),

-- 保利地产用户
(4, 2, 'poly_admin', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '宋广菊', 'sgj@polycn.com', '13800000004', '总裁办', '总裁', 'TENANT_ADMIN', '["ALL"]', 'ACTIVE'),
(5, 2, 'poly_manager', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '陈经理', 'chenmgr@polycn.com', '13800000005', '项目管理部', '项目经理', 'ADMIN', '["asset:*", "contract:*"]', 'ACTIVE'),

-- 绿地控股用户
(6, 3, 'greenland_admin', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '张玉良', 'zyl@greenland.com', '13800000006', '董事会', '董事长', 'TENANT_ADMIN', '["ALL"]', 'ACTIVE'),
(7, 3, 'green_user', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '刘员工', 'liuuser@greenland.com', '13800000007', '开发部', '开发专员', 'USER', '["dashboard:view", "asset:view"]', 'ACTIVE'),

-- 碧桂园集团用户
(8, 4, 'country_admin', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '杨惠妍', 'yhy@countrygarden.com.cn', '13800000008', '董事会', '董事长', 'TENANT_ADMIN', '["ALL"]', 'ACTIVE'),

-- 华润置地用户
(9, 5, 'crland_admin', '$2a$10$7JB720yubVSOfvVaMWye2.bDhLiXGdnyiOrqYvDRtoMM/GGYyMBrG', '李欣', 'lixin@crc.com.hk', '13800000009', '总经理办公室', '总经理', 'TENANT_ADMIN', '["ALL"]', 'ACTIVE');

-- 更新现有数据的租户ID
UPDATE project SET tenant_id = 1 WHERE id IN (1, 2);
UPDATE project SET tenant_id = 2 WHERE id = 3;

UPDATE building SET tenant_id = 1 WHERE project_id IN (1, 2);
UPDATE building SET tenant_id = 2 WHERE project_id = 3;

UPDATE unit SET tenant_id = 1 WHERE project_id IN (1, 2);
UPDATE unit SET tenant_id = 2 WHERE project_id = 3;

UPDATE tenant SET tenant_id = 1 WHERE id IN (1, 2);
UPDATE tenant SET tenant_id = 2 WHERE id IN (3, 4);

UPDATE contract SET tenant_id = 1 WHERE project_id IN (1, 2);
UPDATE contract SET tenant_id = 2 WHERE project_id = 3;

UPDATE receivable_account SET tenant_id = 1 WHERE project_id IN (1, 2);
UPDATE receivable_account SET tenant_id = 2 WHERE project_id = 3;