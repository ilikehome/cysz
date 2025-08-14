-- 创业园租赁管理系统数据库结构
-- Database: cysz
-- 从阿里云MySQL数据库导出的真实表结构
-- 导出时间: 2025-08-14

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `cysz` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cysz`;

-- 1. 组织表 (organizations)
CREATE TABLE `organizations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `org_code` varchar(50) NOT NULL COMMENT '组织代码',
  `org_name` varchar(100) NOT NULL COMMENT '组织名称',
  `org_type` varchar(20) DEFAULT 'company' COMMENT '组织类型：company-公司，government-政府机构，institution-事业单位',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `description` text COMMENT '组织描述',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_code` (`org_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- 2. 用户表 (users)
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `org_id` bigint DEFAULT NULL COMMENT '所属组织ID',
  `role` varchar(20) DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username_org` (`username`, `org_id`),
  KEY `org_id` (`org_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organizations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- 3. 项目表 (project)
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_name` varchar(100) NOT NULL,
  `project_type` enum('COMPLEX','COMMERCIAL_DISTRICT','HOTEL','APARTMENT','OFFICE') NOT NULL,
  `management_org` varchar(100) NOT NULL,
  `rent_bill_company` varchar(100) NOT NULL,
  `rent_bill_bank_account` varchar(100) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `address` varchar(200) NOT NULL,
  `project_manager` varchar(50) DEFAULT NULL,
  `contact_phone` varchar(20) NOT NULL,
  `status` tinyint DEFAULT '1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_project_name` (`project_name`),
  KEY `idx_project_type` (`project_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. 楼栋表 (building)
CREATE TABLE `building` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `building_name` varchar(100) NOT NULL,
  `building_code` varchar(50) NOT NULL,
  `project_id` bigint NOT NULL,
  `building_area` decimal(12,2) DEFAULT NULL,
  `rent_area` decimal(12,2) DEFAULT NULL,
  `property_area` decimal(12,2) DEFAULT NULL,
  `usable_area` decimal(12,2) DEFAULT NULL,
  `remark` text,
  `status` tinyint DEFAULT '1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_building_name` (`building_name`),
  KEY `idx_building_code` (`building_code`),
  KEY `idx_status` (`status`),
  CONSTRAINT `building_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. 楼层表 (floor)
CREATE TABLE `floor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `floor_name` varchar(100) NOT NULL,
  `floor_code` varchar(50) NOT NULL,
  `building_id` bigint NOT NULL,
  `remark` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_floor_name` (`floor_name`),
  KEY `idx_floor_code` (`floor_code`),
  CONSTRAINT `floor_ibfk_1` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 6. 单元表 (unit)
CREATE TABLE `unit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `unit_name` varchar(100) NOT NULL,
  `unit_code` varchar(50) NOT NULL,
  `floor_id` bigint NOT NULL,
  `unit_status` enum('RENTABLE','SELF_USE','PUBLIC_USE','LEASE_BACK','DISABLED','SELF_RENTAL') DEFAULT 'RENTABLE',
  `unit_purpose` varchar(100) DEFAULT NULL,
  `building_area` decimal(10,2) DEFAULT NULL,
  `rent_area` decimal(10,2) DEFAULT NULL,
  `is_multi_tenant` tinyint(1) DEFAULT '0',
  `remark` text,
  `status` tinyint DEFAULT '1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_unit_code` (`unit_code`),
  KEY `idx_floor_id` (`floor_id`),
  KEY `idx_unit_name` (`unit_name`),
  KEY `idx_unit_code` (`unit_code`),
  KEY `idx_unit_status` (`unit_status`),
  KEY `idx_status` (`status`),
  CONSTRAINT `unit_ibfk_1` FOREIGN KEY (`floor_id`) REFERENCES `floor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 7. 租户表 (tenant)
CREATE TABLE `tenant` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Tenant ID',
  `tenant_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Tenant Name',
  `tenant_nature` enum('individual','company','government') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Tenant Nature',
  `enterprise_nature` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Enterprise Nature',
  `social_credit_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Social Credit Code',
  `taxpayer_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Taxpayer ID',
  `business_registration_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Business Registration Number',
  `individual_license_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Individual License Number',
  `brand` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Brand',
  `brand_qualification` enum('direct','franchise','joint') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Brand Qualification',
  `business_format` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Business Format',
  `business_scope` text COLLATE utf8mb4_unicode_ci COMMENT 'Business Scope',
  `legal_person_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Legal Person Name',
  `legal_person_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Legal Person Phone',
  `legal_person_id_card` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Legal Person ID Card',
  `finance_contact` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Finance Contact',
  `finance_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Finance Phone',
  `payer_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Payer Name',
  `payment_account` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Payment Account',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT 'Remark',
  `status` tinyint DEFAULT '1' COMMENT 'Status: 0-Disabled, 1-Enabled',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_name` (`tenant_name`),
  KEY `idx_social_credit_code` (`social_credit_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tenant Information Table';

-- 8. 合同表 (contract)
CREATE TABLE `contract` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contract_number` varchar(100) NOT NULL,
  `project_id` bigint NOT NULL,
  `unit_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  `contract_type` varchar(20) DEFAULT 'RENT',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `monthly_rent` decimal(10,2) DEFAULT NULL,
  `deposit` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `payment_cycle` varchar(20) DEFAULT 'MONTHLY',
  `status` varchar(20) DEFAULT 'ACTIVE',
  `notes` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contract_number` (`contract_number`),
  KEY `idx_contract_number` (`contract_number`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_unit_id` (`unit_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_end_date` (`end_date`),
  CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `contract_ibfk_2` FOREIGN KEY (`unit_id`) REFERENCES `unit` (`id`) ON DELETE CASCADE,
  CONSTRAINT `contract_ibfk_3` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 9. 应收账款表 (receivable_account)
CREATE TABLE `receivable_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contract_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  `bill_period` varchar(20) NOT NULL,
  `bill_type` varchar(20) DEFAULT 'RENT',
  `amount` decimal(10,2) NOT NULL,
  `paid_amount` decimal(10,2) DEFAULT '0.00',
  `outstanding_amount` decimal(10,2) NOT NULL,
  `due_date` date NOT NULL,
  `payment_date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT 'PENDING',
  `late_fee` decimal(8,2) DEFAULT '0.00',
  `notes` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contract_period_type` (`contract_id`, `bill_period`, `bill_type`),
  KEY `idx_contract_id` (`contract_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_bill_period` (`bill_period`),
  KEY `idx_status` (`status`),
  KEY `idx_due_date` (`due_date`),
  CONSTRAINT `receivable_account_ibfk_1` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`) ON DELETE CASCADE,
  CONSTRAINT `receivable_account_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 10. 已收款表 (received)
CREATE TABLE `received` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '批次号',
  `line_no` varchar(50) DEFAULT NULL COMMENT '行号',
  `company` varchar(255) DEFAULT NULL COMMENT '公司',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payment_method` varchar(50) DEFAULT NULL COMMENT '付款方式',
  `payee_name` varchar(255) DEFAULT NULL COMMENT '收款人姓名',
  `payee_account` varchar(100) DEFAULT NULL COMMENT '收款人账户',
  `payee_bank` varchar(255) DEFAULT NULL COMMENT '收款人银行',
  `payer_name` varchar(255) DEFAULT NULL COMMENT '付款人姓名',
  `payer_account` varchar(100) DEFAULT NULL COMMENT '付款人账户',
  `payer_bank` varchar(255) DEFAULT NULL COMMENT '付款人银行',
  `transaction_time` datetime DEFAULT NULL COMMENT '交易时间',
  `status` varchar(50) DEFAULT 'UNMATCHED' COMMENT '状态：UNMATCHED-未匹配，MATCHED-已匹配，PROCESSING-处理中',
  `match_status` varchar(50) DEFAULT 'PENDING' COMMENT '匹配状态：PENDING-待匹配，MATCHED-已匹配，MANUAL-手动匹配',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_status` (`status`),
  KEY `idx_match_status` (`match_status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已收款表';

-- 创建索引优化查询性能
CREATE INDEX `idx_tenant_nature` ON `tenant` (`tenant_nature`);
CREATE INDEX `idx_contract_dates` ON `contract` (`start_date`, `end_date`);
CREATE INDEX `idx_receivable_due_date` ON `receivable_account` (`due_date`);
CREATE INDEX `idx_received_transaction_time` ON `received` (`transaction_time`);
