-- 多租户架构扩展 - 租户管理表
-- 创建时间：2025-01-11

-- 1. 租户组织表（公司/单位）
CREATE TABLE IF NOT EXISTS tenant_organization (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '组织ID',
    org_code VARCHAR(50) NOT NULL UNIQUE COMMENT '组织代码',
    org_name VARCHAR(255) NOT NULL COMMENT '组织名称',
    org_type VARCHAR(20) DEFAULT 'COMPANY' COMMENT '组织类型：COMPANY-公司，GOVERNMENT-政府机构，INSTITUTION-事业单位，OTHER-其他',
    contact_person VARCHAR(100) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    address VARCHAR(500) COMMENT '组织地址',
    logo_url VARCHAR(255) COMMENT '组织Logo URL',
    description TEXT COMMENT '组织描述',
    license_number VARCHAR(100) COMMENT '营业执照号/组织机构代码',
    established_date DATE COMMENT '成立日期',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃，SUSPENDED-暂停，DELETED-已删除',
    subscription_plan VARCHAR(20) DEFAULT 'BASIC' COMMENT '订阅计划：BASIC-基础版，STANDARD-标准版，PREMIUM-高级版，ENTERPRISE-企业版',
    subscription_start_date DATE COMMENT '订阅开始日期',
    subscription_end_date DATE COMMENT '订阅结束日期',
    max_users INT DEFAULT 10 COMMENT '最大用户数',
    max_projects INT DEFAULT 5 COMMENT '最大项目数',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_org_code (org_code),
    INDEX idx_org_name (org_name),
    INDEX idx_status (status),
    INDEX idx_subscription_plan (subscription_plan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户组织表';

-- 2. 用户表（扩展原有用户概念）
CREATE TABLE IF NOT EXISTS system_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    tenant_id BIGINT NOT NULL COMMENT '所属租户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    department VARCHAR(100) COMMENT '部门',
    position VARCHAR(100) COMMENT '职位',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色：SUPER_ADMIN-超级管理员，TENANT_ADMIN-租户管理员，ADMIN-管理员，USER-普通用户',
    permissions JSON COMMENT '权限列表',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    login_count INT DEFAULT 0 COMMENT '登录次数',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃，LOCKED-锁定，DELETED-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE,
    UNIQUE KEY uk_tenant_username (tenant_id, username),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 为现有表添加租户ID字段
ALTER TABLE project ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属租户ID' AFTER id;
ALTER TABLE project ADD FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE;
ALTER TABLE project ADD INDEX idx_tenant_id (tenant_id);

ALTER TABLE building ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属租户ID' AFTER id;
ALTER TABLE building ADD FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE;
ALTER TABLE building ADD INDEX idx_tenant_id (tenant_id);

ALTER TABLE unit ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属租户ID' AFTER id;
ALTER TABLE unit ADD FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE;
ALTER TABLE unit ADD INDEX idx_tenant_id (tenant_id);

ALTER TABLE tenant ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属租户ID' AFTER id;
ALTER TABLE tenant ADD FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE;
ALTER TABLE tenant ADD INDEX idx_tenant_id (tenant_id);

ALTER TABLE contract ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属租户ID' AFTER id;
ALTER TABLE contract ADD FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE;
ALTER TABLE contract ADD INDEX idx_tenant_id (tenant_id);

ALTER TABLE receivable_account ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属租户ID' AFTER id;
ALTER TABLE receivable_account ADD FOREIGN KEY (tenant_id) REFERENCES tenant_organization(id) ON DELETE CASCADE;
ALTER TABLE receivable_account ADD INDEX idx_tenant_id (tenant_id);