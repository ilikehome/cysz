-- 云联智管地产资管系统数据库初始化
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
    project_type ENUM('COMPLEX', 'APARTMENT', 'OFFICE', 'COMMERCIAL') NOT NULL COMMENT '项目类型',
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
('资产管理', 'asset', 0, 1, '/asset', '', 'OfficeBuilding', 2),
('项目管理', 'asset:project', 4, 1, '/asset/project', 'asset/project/index', 'Postcard', 1);

-- 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 插入角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id) 
SELECT 1, id FROM sys_permission;