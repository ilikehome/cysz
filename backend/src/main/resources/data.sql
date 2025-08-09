-- 云联智管地产资管系统初始测试数据

-- 清空现有数据（避免主键冲突）
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM receivable_account;
DELETE FROM contract;
DELETE FROM tenant;
DELETE FROM unit;
DELETE FROM building;
DELETE FROM project;
SET FOREIGN_KEY_CHECKS = 1;

-- 重置自增ID
ALTER TABLE project AUTO_INCREMENT = 1;
ALTER TABLE building AUTO_INCREMENT = 1;
ALTER TABLE unit AUTO_INCREMENT = 1;
ALTER TABLE tenant AUTO_INCREMENT = 1;
ALTER TABLE contract AUTO_INCREMENT = 1;
ALTER TABLE receivable_account AUTO_INCREMENT = 1;

-- 插入项目数据
INSERT INTO `project` (`id`, `project_name`, `project_type`, `company_name`, `rent_bill_company`, `property_bill_company`, `property_right_company`, `building_area`, `rent_area`, `property_area`, `city`, `address`, `status`) VALUES
(1, '万达广场', '商业综合体', '万达集团', '万达物业', '万达物业', '万达集团', 50000.00, 45000.00, 48000.00, '北京', '北京市朝阳区建国路93号', 1),
(2, '中关村写字楼', '办公楼', '中关村科技', '中关村物业', '中关村物业', '中关村科技', 30000.00, 28000.00, 29000.00, '北京', '北京市海淀区中关村大街1号', 1),
(3, '阳光公寓', '住宅', '阳光地产', '阳光物业', '阳光物业', '阳光地产', 20000.00, 18000.00, 19000.00, '上海', '上海市浦东新区世纪大道100号', 1);

-- 插入楼宇数据
INSERT INTO `building` (`id`, `building_code`, `building_name`, `project_id`, `building_type`, `floor_count`, `building_area`, `rent_area`, `property_area`, `building_status`, `description`, `status`) VALUES
(1, 'A001', 'A栋', 1, '商业楼', 20, 25000.00, 22000.00, 23000.00, 'NORMAL', '万达广场A栋商业楼', 1),
(2, 'B001', 'B栋', 1, '商业楼', 20, 25000.00, 23000.00, 24000.00, 'NORMAL', '万达广场B栋商业楼', 1),
(3, 'C001', 'C栋', 2, '办公楼', 15, 15000.00, 14000.00, 14500.00, 'NORMAL', '中关村写字楼C栋', 1),
(4, 'D001', 'D栋', 2, '办公楼', 15, 15000.00, 14000.00, 14500.00, 'NORMAL', '中关村写字楼D栋', 1),
(5, 'E001', 'E栋', 3, '住宅楼', 30, 10000.00, 9000.00, 9500.00, 'NORMAL', '阳光公寓E栋住宅楼', 1),
(6, 'F001', 'F栋', 3, '住宅楼', 30, 10000.00, 9000.00, 9500.00, 'NORMAL', '阳光公寓F栋住宅楼', 1);

-- 插入单元数据
INSERT INTO `unit` (`id`, `unit_code`, `unit_description`, `project_id`, `building_id`, `unit_status`, `unit_purpose`, `building_area`, `rent_area`, `property_area`, `status`) VALUES
(1, 'A001-101', 'A栋1层101室', 1, 1, 'OCCUPIED', '办公', 120.50, 115.00, 118.00, 1),
(2, 'A001-102', 'A栋1层102室', 1, 1, 'VACANT', '办公', 95.80, 90.00, 92.50, 1),
(3, 'A001-201', 'A栋2层201室', 1, 1, 'OCCUPIED', '办公', 130.00, 125.00, 127.50, 1),
(4, 'B001-101', 'B栋1层101室', 1, 2, 'VACANT', '商铺', 200.00, 190.00, 195.00, 1),
(5, 'C001-301', 'C栋3层301室', 2, 3, 'OCCUPIED', '办公', 150.00, 145.00, 147.50, 1),
(6, 'C001-302', 'C栋3层302室', 2, 3, 'VACANT', '办公', 140.00, 135.00, 137.50, 1),
(7, 'E001-1001', 'E栋10层1001室', 3, 5, 'OCCUPIED', '住宅', 85.00, 80.00, 82.50, 1),
(8, 'E001-1002', 'E栋10层1002室', 3, 5, 'VACANT', '住宅', 90.00, 85.00, 87.50, 1);

-- 插入租户数据
INSERT INTO `tenant` (`id`, `tenant_code`, `tenant_name`, `tenant_category`, `legal_person`, `contact_phone`, `social_credit_code`, `contact_address`, `email`, `status`) VALUES
(1, 'T001', '北京科技有限公司', '企业', '张三', '13800138001', '91110000123456789X', '北京市朝阳区科技园区1号', 'zhangsan@bjtech.com', 1),
(2, 'T002', '上海贸易公司', '企业', '李四', '13800138002', '91310000987654321Y', '上海市浦东新区贸易大厦2号', 'lisi@shtrade.com', 1),
(3, 'T003', '广州设计工作室', '个体工商户', '王五', '13800138003', '92440000567890123Z', '广州市天河区设计园3号', 'wangwu@gzdesign.com', 1),
(4, 'T004', '深圳咨询服务公司', '企业', '赵六', '13800138004', '91440300456789012A', '深圳市南山区咨询大厦4号', 'zhaoliu@szconsult.com', 1);

-- 插入合同数据
INSERT INTO `contract` (`id`, `contract_no`, `contract_name`, `project_id`, `start_date`, `end_date`, `signatory`, `contract_type`, `contract_status`, `rent_bill_company`, `property_bill_company`, `lease_no`, `tenant_id`, `tenant_name`, `unit_id`, `unit_description`, `rent_mode`, `monthly_rent`, `deposit`, `status`) VALUES
(1, 'HT001', '万达广场租赁合同', 1, '2024-01-01', '2024-12-31', '张经理', '租赁合同', 'ACTIVE', '万达物业', '万达物业', 'ZL001', 1, '北京科技有限公司', 1, 'A栋1层101室', '月付', 50000.00, 150000.00, 1),
(2, 'HT002', '中关村写字楼租赁合同', 2, '2024-02-01', '2025-01-31', '李经理', '租赁合同', 'ACTIVE', '中关村物业', '中关村物业', 'ZL002', 2, '上海贸易公司', 5, 'C栋3层301室', '季付', 35000.00, 105000.00, 1),
(3, 'HT003', '阳光公寓租赁合同', 3, '2024-03-01', '2025-02-28', '王经理', '租赁合同', 'DRAFT', '阳光物业', '阳光物业', 'ZL003', 3, '广州设计工作室', 7, 'E栋10层1001室', '月付', 8000.00, 24000.00, 1);

-- 插入应收账款数据
INSERT INTO `receivable_account` (`id`, `batch_no`, `line_no`, `process_status`, `company`, `project_id`, `payment_method`, `payee_name`, `payee_account`, `payee_bank`, `payee`, `payer_name`, `tenant_name`, `contract_no`, `payer_account`, `payer`, `payer_bank_code`, `transaction_time`, `amount`, `pending_amount`, `input_date`, `claimer`, `claim_date`, `debit_credit_flag`, `summary`, `remark`, `status`) VALUES
(1, 'B202401001', '001', 'COMPLETED', '万达集团', 1, '银行转账', '万达物业', '6222021234567890', '中国银行', '万达物业', '北京科技有限公司', '北京科技有限公司', 'HT001', '6222021234567891', '北京科技有限公司', '104100000004', '2024-01-15 10:30:00', 50000.00, 0.00, '2024-01-15', '张经理', '2024-01-15', 'CREDIT', '租金收入', '2024年1月租金', 1),
(2, 'B202401002', '002', 'COMPLETED', '中关村科技', 2, '网银', '中关村物业', '6222021234567892', '工商银行', '中关村物业', '上海贸易公司', '上海贸易公司', 'HT002', '6222021234567893', '上海贸易公司', '102100000001', '2024-02-15 14:20:00', 105000.00, 0.00, '2024-02-15', '李经理', '2024-02-15', 'CREDIT', '租金收入', '2024年第一季度租金', 1),
(3, 'B202401003', '003', 'PENDING', '阳光地产', 3, '银行转账', '阳光物业', '6222021234567894', '建设银行', '阳光物业', '广州设计工作室', '广州设计工作室', 'HT003', '6222021234567895', '广州设计工作室', '105100000001', '2024-03-15 16:45:00', 8000.00, 8000.00, '2024-03-15', NULL, NULL, 'CREDIT', '租金收入', '2024年3月租金', 1);