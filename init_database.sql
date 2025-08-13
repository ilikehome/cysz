-- 创建项目表
CREATE TABLE IF NOT EXISTS `project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_name` varchar(200) NOT NULL COMMENT '项目名称',
  `project_type` varchar(50) NOT NULL COMMENT '项目类型',
  `management_org` varchar(200) COMMENT '管理组织',
  `rent_bill_company` varchar(200) COMMENT '租金账单公司',
  `rent_bill_bank_account` varchar(100) COMMENT '租金账单银行账号',
  `city` varchar(100) COMMENT '城市',
  `address` varchar(500) COMMENT '地址',
  `project_manager` varchar(100) COMMENT '项目负责人',
  `contact_phone` varchar(20) COMMENT '联系电话',
  `status` int DEFAULT 1 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目表';

-- 创建楼栋表
CREATE TABLE IF NOT EXISTS `building` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` bigint NOT NULL COMMENT '所属项目ID',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `building_code` varchar(50) NOT NULL COMMENT '楼栋编码',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_code` (`building_code`),
  KEY `idx_project_id` (`project_id`),
  CONSTRAINT `fk_building_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼栋表';

-- 创建楼层表
CREATE TABLE IF NOT EXISTS `floor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `floor_name` varchar(100) NOT NULL COMMENT '楼层名称',
  `floor_code` varchar(50) NOT NULL COMMENT '楼层编码',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_floor_code` (`floor_code`),
  KEY `idx_building_id` (`building_id`),
  CONSTRAINT `fk_floor_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼层表';

-- 创建单元表
CREATE TABLE IF NOT EXISTS `unit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `unit_name` varchar(100) NOT NULL COMMENT '单元名称',
  `unit_code` varchar(50) NOT NULL COMMENT '单元编码',
  `floor_id` bigint NOT NULL COMMENT '所属楼层ID',
  `unit_status` varchar(20) NOT NULL DEFAULT 'VACANT' COMMENT '资产状态',
  `unit_purpose` varchar(100) COMMENT '用途',
  `building_area` decimal(10,2) COMMENT '建筑面积',
  `rent_area` decimal(10,2) COMMENT '计租面积',
  `is_multi_tenant` tinyint(1) DEFAULT 0 COMMENT '是否一位多租',
  `remark` text COMMENT '备注',
  `status` int DEFAULT 1 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_unit_code` (`unit_code`),
  KEY `idx_floor_id` (`floor_id`),
  CONSTRAINT `fk_unit_floor` FOREIGN KEY (`floor_id`) REFERENCES `floor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单元表';

-- 插入测试数据
-- 项目数据
INSERT INTO `project` (`project_name`, `project_type`, `management_org`, `rent_bill_company`, `rent_bill_bank_account`, `city`, `address`, `project_manager`, `contact_phone`) VALUES
('Test Complex', 'COMPLEX', 'Management Co', 'Rent Co', '1234567890123456', 'Beijing', 'Test Road 123, Chaoyang District', 'Zhang San', '13800138000'),
('Office Building', 'OFFICE', 'Property Co', 'Bill Co', '6543210987654321', 'Shanghai', 'Test Avenue 456, Pudong District', 'Li Si', '13900139000');

-- 楼栋数据
INSERT INTO `building` (`project_id`, `building_name`, `building_code`, `remark`) VALUES
(1, 'Building A', 'A001', 'Main Building A'),
(1, 'Building B', 'B001', 'Sub Building B'),
(2, 'Building C', 'C001', 'Office Building C');

-- 楼层数据
INSERT INTO `floor` (`building_id`, `floor_name`, `floor_code`, `remark`) VALUES
(1, 'Floor 1', 'A001-F01', 'Building A Floor 1 Lobby and Office'),
(1, 'Floor 2', 'A001-F02', 'Building A Floor 2 Office'),
(1, 'Floor 3', 'A001-F03', 'Building A Floor 3 Office'),
(2, 'Floor 1', 'B001-F01', 'Building B Floor 1'),
(2, 'Floor 2', 'B001-F02', 'Building B Floor 2'),
(3, 'Floor 1', 'C001-F01', 'Building C Floor 1 Office'),
(3, 'Floor 2', 'C001-F02', 'Building C Floor 2 Office');

-- 单元数据
INSERT INTO `unit` (`unit_name`, `unit_code`, `floor_id`, `unit_status`, `unit_purpose`, `building_area`, `rent_area`, `is_multi_tenant`, `remark`) VALUES
('Room 101', 'A001-F01-101', 1, 'VACANT', 'Office', 120.50, 115.00, 0, 'Building A Floor 1 Room 101, South-facing office'),
('Room 102', 'A001-F01-102', 1, 'OCCUPIED', 'Office', 98.30, 95.00, 0, 'Building A Floor 1 Room 102, North-facing office'),
('Room 103', 'A001-F01-103', 1, 'MAINTENANCE', 'Office', 88.60, 85.00, 0, 'Building A Floor 1 Room 103, Small office'),
('Room 201', 'A001-F02-201', 2, 'VACANT', 'Office', 135.80, 130.00, 1, 'Building A Floor 2 Room 201, Large office area'),
('Room 202', 'A001-F02-202', 2, 'OCCUPIED', 'Office', 110.20, 105.00, 0, 'Building A Floor 2 Room 202, Medium office'),
('Room 301', 'A001-F03-301', 3, 'RESERVED', 'Office', 95.40, 90.00, 0, 'Building A Floor 3 Room 301, Reserved office');
